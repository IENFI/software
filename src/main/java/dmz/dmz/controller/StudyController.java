package dmz.dmz.controller;

import dmz.dmz.domain.Study;
import dmz.dmz.service.StudyService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class StudyController {
    private final String fileDir = "C:/Users/user/dms_file/";
    private final StudyService studyService;

    @Autowired
    public StudyController(StudyService studyService) {
        this.studyService = studyService;
    }

    @GetMapping(value="/study/studyHome")
    public String studyListByUserId(Model model, @PageableDefault(page = 0, size = 10, sort="id", direction = Sort.Direction.ASC) Pageable pageable) { // 홈 화면 띄우기

        Page<Study> list = studyService.findByUserIdStudies(pageable);
        int nowPage = list.getPageable().getPageNumber()+1;
        int startPage = Math.max(nowPage-4,1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());

        model.addAttribute("studys", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "/study/studyHome";
    }

    // 목록 보기 제목순 정렬
//    @GetMapping(value="/study/studyHome/sortByTitle")
//    public String studyListSortByTitle(Model model, @PageableDefault(page = 0, size = 10, sort="title", direction = Sort.Direction.ASC) Pageable pageable) { // 홈 화면 띄우기
//
//        Page<Study> list = studyService.findStudies(pageable);
//        int nowPage = list.getPageable().getPageNumber()+1;
//        int startPage = Math.max(nowPage-4,1);
//        int endPage = Math.min(nowPage + 5, list.getTotalPages());
//
//        model.addAttribute("studys", list);
//        model.addAttribute("nowPage", nowPage);
//        model.addAttribute("startPage", startPage);
//        model.addAttribute("endPage", endPage);
//
//        return "/study/studyHome";
//    }

    @GetMapping(value="/study/studyCreate")
    public String studyInsert() { // 추가 화면 띄우기
        return "/study/studyCreate";
    }

    @PostMapping(value="/study/studyCreates")
    public String insert(Study studyInfo, MultipartFile files) throws IOException { // 공부 기록 추가 메소드

        Study study = new Study();
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
            //String extension = originalName.substring(originalName.lastIndexOf("."));
            String saveName = uuid + originalName;
            String savePath = fileDir + saveName;
            study.setFileoriginname(originalName);
            study.setFilesavename(saveName);
            study.setFilesavepath(savePath);

            File saveFile = new File(savePath);
            files.transferTo(saveFile);
        }

        studyService.saveStudy(study);

        return "redirect:/study/studyHome";
    }

    @GetMapping(value="/study/studyContent")
    public String studyContent(@RequestParam("id") Long id, Model model) { // 공부 기록 상세 보기
        Study study = studyService.findOneStudy(id)
                .orElseThrow(NullPointerException::new);
        model.addAttribute("studyId", study.getId());
        model.addAttribute("studyTitle", study.getTitle());
        model.addAttribute("studyContent", study.getContent());
        model.addAttribute("studyDate", study.getDate());
        model.addAttribute("studyUrl", study.getUrl());
        model.addAttribute("studyFileSaveName", study.getFilesavepath());
        model.addAttribute("studyFileName", study.getFileoriginname());
        return "/study/studyContent";
    }

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadFile(@RequestParam("studyId") Long id) throws IOException {
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
    public String studyDelete(@RequestParam("id") Long id) { // 공부 기록 삭제
        studyService.deleteStudy(id);
        return "redirect:/study/studyHome";
    }

    @GetMapping(value="/study/studyUpdate")
    public String studyUpdate(@RequestParam("id") Long id, Model model) { // 공부 기록 수정 화면 띄우기
        Study study = studyService.findOneStudy(id)
                .orElseThrow(NullPointerException::new);
        model.addAttribute("studyId", study.getId());
        model.addAttribute("studyTitle", study.getTitle());
        model.addAttribute("studyContent", study.getContent());
        model.addAttribute("studyDate", study.getDate());
        model.addAttribute("studyUrl", study.getUrl());
        model.addAttribute("studyFile", study.getFilesavepath());
        return "/study/studyUpdate";
    }

    @PostMapping(value="/studyUpdateComplete")
    public String update(Study studyInfo) { // 공부 기록 추가 메소드
//파일 수정 구현
        Study study = new Study();
        study.setId(studyInfo.getId());
        study.setTitle(studyInfo.getTitle());
        study.setContent(studyInfo.getContent());

        studyService.saveStudy(study);

        return "redirect:/study/studyHome";
    }

}
