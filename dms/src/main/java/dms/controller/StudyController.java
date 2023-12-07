package dms.controller;

import dms.domain.Study;
import dms.dto.AlertDTO;
import dms.service.StudyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

@Controller
public class StudyController {
    private final String fileDir = "/aien0118/tomcat/webapps/download/";
//    private final String fileDir = "C:/Users/SEC/Desktop/dms";
//    private final String fileDir = "D:/YU/3-2/SE/project/dms/dms_file";
    //private final String fileDir = "C:/Users/user/dms_file/";
    //    각자 변경해야할 부분4
    private final StudyService studyService;

    @Autowired
    public StudyController(StudyService studyService) {
        this.studyService = studyService;
    }

    @GetMapping(value="/study/studyHome")
    public String studyListByMemberID(@SessionAttribute(name = "memberId", required = false) String memberId, Model model, @PageableDefault(page = 0, size = 10, sort="id", direction = Sort.Direction.ASC) Pageable pageable) { // 홈 화면 띄우기

        if(memberId==null) {
            return "redirect:/";
        }

        List<Study> studyList = studyService.findStudiesByMemberID(memberId);
        Page<Study> list = studyService.findStudiesByMemberID(memberId, pageable);
        int nowPage = list.getPageable().getPageNumber()+1;
        int startPage = Math.max(nowPage-4,1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());

        model.addAttribute("studies", list);
        model.addAttribute("studyList", studyList);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "/study/studyHome";
    }

    @GetMapping(value="/study/studyCreate")
    public String studyInsert(@SessionAttribute(name = "memberId", required = false) String memberId, Model model) { // 추가 화면 띄우기

        if(memberId==null) {
            return "redirect:/";
        }

        model.addAttribute("memberID", memberId);
        return "/study/studyCreate";
    }

    @PostMapping(value="/study/studyCreates")
    public String insert(@SessionAttribute(name = "memberId", required = false) String memberId, Study studyInfo, MultipartFile files) throws IOException { // 공부 기록 추가 메소드

        if(memberId==null) {
            return "redirect:/";
        }

        Study study = new Study();
        study.setMemberId(memberId);
        study.setTitle(studyInfo.getTitle());
        study.setContent(studyInfo.getContent());

        Long time = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = dateFormat.format(time);
        study.setDate(date);

        if(!studyInfo.getUrl().isEmpty()) {
            study.setUrl(studyInfo.getUrl());
        }

        String originalName = studyInfo.getFileoriginname();
        if(!originalName.isEmpty()) {
            String uuid = UUID.randomUUID().toString();
            String saveName = uuid + originalName;
            String savePath = fileDir + saveName;
            //String savePath = saveName;
            study.setFileoriginname(originalName);
            study.setFilesavename(saveName);
            study.setFilesavepath(savePath);

            File saveFile = new File(savePath);
            files.transferTo(saveFile);
        } else {
            study.setFileoriginname("");
            study.setFilesavename("");
            study.setFilesavepath("");
        }

        studyService.saveStudy(study);

        return "redirect:/study/studyHome";
    }

    @GetMapping(value="/study/studyContent")
    public String studyContent(@SessionAttribute(name = "memberId", required = false) String memberId, @RequestParam("id") Long id, Model model) { // 공부 기록 상세 보기

        if(memberId==null) {
            return "redirect:/";
        }

        Study study = studyService.findOneStudy(id)
                .orElseThrow(NullPointerException::new);
        model.addAttribute("studyId", study.getId());
        model.addAttribute("studyTitle", study.getTitle());
        model.addAttribute("studyContent", study.getContent());
        model.addAttribute("studyDate", study.getDate());
        model.addAttribute("studyUrl", study.getUrl());
        model.addAttribute("studyFileSaveName", study.getFilesavepath());
        model.addAttribute("studyFileName", study.getFileoriginname());

        if(study.getMemberId().equals(memberId)) {
            return "/study/studyContent";
        }
        else {
            AlertDTO message = new AlertDTO("ERROR", "/study/studyHome", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
    }

    @GetMapping("/study/downloads/{studyId}")
    public ResponseEntity<InputStreamResource> downloadFile(@SessionAttribute(name = "memberId", required = false) String memberId, @PathVariable("studyId") Long id) throws IOException {

        Study study = studyService.findOneStudy(id)
                .orElseThrow(NullPointerException::new);

        Path path = Paths.get(study.getFilesavepath());
        InputStreamResource resource = new InputStreamResource(Files.newInputStream(path));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + study.getFileoriginname() + "\"")
                .body(resource);
    }

    @GetMapping(value="/study/studyDelete")
    public String studyDelete(@SessionAttribute(name = "memberId", required = false) String memberId, @RequestParam("id") Long id, Model model) { // 공부 기록 삭제

        if(memberId==null) {
            return "redirect:/";
        }

        String deleteCheck = studyService.deleteStudy(id);
        if(deleteCheck == "success") {
            AlertDTO message = new AlertDTO("공부기록 삭제가 완료되었습니다.", "/study/studyHome", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
        else {
            AlertDTO message = new AlertDTO("공부기록 삭제에 실패했습니다.", "/study/studyHome", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
    }

    @GetMapping(value="/study/studyUpdate")
    public String studyUpdate(@SessionAttribute(name = "memberId", required = false) String memberId, @RequestParam("id") Long id, Model model) { // 공부 기록 수정 화면 띄우기

        if(memberId==null) {
            return "redirect:/";
        }

        Study study = studyService.findOneStudy(id)
                .orElseThrow(NullPointerException::new);

        model.addAttribute("studyId", study.getId());
        model.addAttribute("studyTitle", study.getTitle());
        model.addAttribute("studyContent", study.getContent());
        model.addAttribute("studyDate", study.getDate());
        model.addAttribute("studyUrl", study.getUrl());
        model.addAttribute("studyFileSavePath", study.getFilesavepath());
        model.addAttribute("studyFileSaveName", study.getFilesavename());
        model.addAttribute("studyOriginName", study.getFileoriginname());
        model.addAttribute("studyMemberID", study.getMemberId());

        if(study.getMemberId().equals(memberId)) {
            return "/study/studyUpdate";
        }
        else {

            return "redirect:/study/studyHome";
        }
    }

    @PostMapping(value="/studyUpdateComplete")
    public String update(@SessionAttribute(name = "memberId", required = false) String memberId, Study studyInfo,  MultipartFile files, Model model) throws IOException { // 공부 기록 추가 메소드

        if(memberId==null) {
            return "redirect:/";
        }

        Study study = new Study();
        study.setId(studyInfo.getId());
        study.setMemberId(studyInfo.getMemberId());
        study.setTitle(studyInfo.getTitle());
        study.setContent(studyInfo.getContent());
        study.setDate(studyInfo.getDate());
        study.setUrl(studyInfo.getUrl());

        String originalName = studyInfo.getFileoriginname();
        if(!originalName.isEmpty()) {
            String uuid = UUID.randomUUID().toString();
            String saveName = uuid + originalName;
            String savePath = fileDir + saveName;
            //String savePath = saveName;
            study.setFileoriginname(originalName);
            study.setFilesavename(saveName);
            study.setFilesavepath(savePath);

            File saveFile = new File(savePath);
            files.transferTo(saveFile);
        } else {
            study.setFileoriginname("");
            study.setFilesavename("");
            study.setFilesavepath("");
        }

        String updateCheck = studyService.updateStudy(study);
        if (updateCheck == "success") {
            AlertDTO message = new AlertDTO("공부기록 수정이 완료되었습니다.", "/study/studyContent?id="+studyInfo.getId(), RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        } else {
            AlertDTO message = new AlertDTO("공부기록 수정에 실패했습니다.", "/study/studyContent?id="+studyInfo.getId(), RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }

    }

    private String showMessageAndRedirect(final AlertDTO params, Model model) {
        model.addAttribute("params", params);
        return "alert/alertRedirect";
    }

}