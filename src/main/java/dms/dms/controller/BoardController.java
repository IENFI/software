package dms.dms.controller;

import dms.dms.domain.MemberEntity;
import dms.dms.domain.MemberRole;
import dms.dms.dto.AlertDTO;
import dms.dms.dto.BoardDto;
import dms.dms.dto.CommentDto;
import dms.dms.dto.FileDto;
import dms.dms.repository.FileRepository;
import dms.dms.repository.MemberRepository;
import dms.dms.service.BoardService;
import dms.dms.service.FileService;
import dms.dms.service.MemberService;
import dms.dms.util.MD5Generator;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
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

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final String fileDir = "C:/Users/SEC/Desktop/DMS/dms_file";

    private final BoardService boardService;
    private final FileService fileService;
    private final MemberRepository memberRepository;
    private final FileRepository fileRepository;
    private final MemberService memberService;

    @GetMapping("/board/notice")
    public String getBoardsNotice(@SessionAttribute(name = "memberId", required = false) String memberId, Model model,
                                  @PageableDefault(page = 0, size = 10, sort = "createdDate", direction = Sort.Direction.DESC)
                                  Pageable pageable) {
        MemberEntity loginMember = memberService.getLoginUserByLoginId(memberId);

        if (loginMember == null){
            return "redirect:/";
        }

        Page<BoardDto> list = boardService.getBoards("notice", pageable).map(board -> {
            BoardDto boardDto = new BoardDto();
            boardDto.setBoardId(board.getBoardId());
            boardDto.setTitle(board.getTitle());
            boardDto.setCategory(board.getCategory());
            boardDto.setContent(board.getContent());
            boardDto.setWriter(board.getMemberEntity().getMemberId());
            boardDto.setCreatedDate(board.getCreatedDate());
            boardDto.setModifiedDate(board.getModifiedDate());
            boardDto.setFileId(board.getFileId());
            boardDto.setUrl(board.getUrl());
            return boardDto;
        });

        int nowPage = list.getPageable().getPageNumber()+1;
        int startPage = Math.max(nowPage-4,1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());

        model.addAttribute("boardList", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "/board/boardlist-notice";
    }// 공지사항 게시글 조회

    @GetMapping("/board/contest")
    public String getBoardsContest(@SessionAttribute(name = "memberId", required = false) String memberId, Model model,
                                  @PageableDefault(page = 0, size = 10, sort = "createdDate", direction = Sort.Direction.DESC)
                                  Pageable pageable) {
        MemberEntity loginMember = memberService.getLoginUserByLoginId(memberId);

        if (loginMember == null){
            return "redirect:/";
        }

        Page<BoardDto> list = boardService.getBoards("contest", pageable).map(board -> {
            BoardDto boardDto = new BoardDto();
            boardDto.setBoardId(board.getBoardId());
            boardDto.setTitle(board.getTitle());
            boardDto.setCategory(board.getCategory());
            boardDto.setContent(board.getContent());
            boardDto.setWriter(board.getMemberEntity().getMemberId());
            boardDto.setCreatedDate(board.getCreatedDate());
            boardDto.setModifiedDate(board.getModifiedDate());
            boardDto.setFileId(board.getFileId());
            boardDto.setUrl(board.getUrl());
            return boardDto;
        });

        int nowPage = list.getPageable().getPageNumber()+1;
        int startPage = Math.max(nowPage-4,1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());

        model.addAttribute("boardList", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "/board/boardlist-contest";
    }// 공모전 게시글 조회

    @GetMapping("/board/free")
    public String getBoardsFree(@SessionAttribute(name = "memberId", required = false) String memberId, Model model,
                                   @PageableDefault(page = 0, size = 10, sort = "createdDate", direction = Sort.Direction.DESC)
                                   Pageable pageable) {
        MemberEntity loginMember = memberService.getLoginUserByLoginId(memberId);

        if (loginMember == null){
            return "redirect:/";
        }

        Page<BoardDto> list = boardService.getBoards("free", pageable).map(board -> {
            BoardDto boardDto = new BoardDto();
            boardDto.setBoardId(board.getBoardId());
            boardDto.setTitle(board.getTitle());
            boardDto.setCategory(board.getCategory());
            boardDto.setContent(board.getContent());
            boardDto.setWriter(board.getMemberEntity().getMemberId());
            boardDto.setCreatedDate(board.getCreatedDate());
            boardDto.setModifiedDate(board.getModifiedDate());
            boardDto.setFileId(board.getFileId());
            boardDto.setUrl(board.getUrl());
            return boardDto;
        });

        int nowPage = list.getPageable().getPageNumber()+1;
        int startPage = Math.max(nowPage-4,1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());

        model.addAttribute("boardList", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "/board/boardlist-free";
    }// 자유게시판 게시글 조회

    @GetMapping("/board/{boardId}")
    public String getBoard(@PathVariable("boardId") Long boardId, Model model, @SessionAttribute(name = "memberId", required = false) String memberId) {
        MemberEntity loginMember = memberService.getLoginUserByLoginId(memberId);

        if (loginMember == null){
            return "redirect:/";
        }


        BoardDto boardDto = boardService.getBoard(boardId);
        model.addAttribute("board", boardDto);
        model.addAttribute("comment",new CommentDto());

        Long fileId = boardDto.getFileId();
        if (fileId != null) {
            FileDto fileDto = FileDto.toDto(fileRepository.findByFileId(fileId).get());
            model.addAttribute("filename", fileDto.getOriginFilename());
        }

        MemberEntity memberEntity = memberRepository.findByMemberId(memberId).get();
        if (loginMember.getMemberRole()== MemberRole.USER) {
            if (memberId.equals(boardDto.getWriter())) {
                return "/board/boardcontent-my";
            }
            else {
                return "/board/boardcontent";
            }
        }
        else {
            if (memberId.equals(boardDto.getWriter())) {
                return "/board/boardcontent-my";
            }
            else {
                return "/board/boardcontent-admin";
            }
        }
    }// 개별 게시글 조회

    @GetMapping("/board/write")
    public String writeBoard(@SessionAttribute(name = "memberId", required = false) String memberId) {
        MemberEntity loginMember = memberService.getLoginUserByLoginId(memberId);

        if (loginMember == null){
            return "redirect:/";
        }

        return "/board/boardwrite";
    }

    @PostMapping("/board/write")
    public String boardWrite(@RequestParam(value = "file", required = false) MultipartFile files,
                             @ModelAttribute BoardDto boardDto,
                             @SessionAttribute(name = "memberId", required = false) String memberId, Model model) {
        BoardDto newBoardDto = null;
        Long fileId = null;
        try {
            if (files != null && !files.isEmpty()) {
                String originFilename = files.getOriginalFilename();
                String filename = new MD5Generator(originFilename).toString();
                String savePath = fileDir;

                if (!new File(savePath).exists()) {
                    try {
                        new File(savePath).mkdir();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                String filePath = savePath + "/" + originFilename;
                files.transferTo(new File(filePath));

                FileDto fileDto = new FileDto();
                fileDto.setOriginFilename(originFilename);
                fileDto.setFilename(filename);
                fileDto.setFilePath(filePath);

                fileId = fileService.uploadFile(fileDto);
                boardDto.setFileId(fileId);
            }

            if (boardDto.getContent().isEmpty()) {
                AlertDTO message = new AlertDTO("본문 내용을 적어야합니다.", "/board/write", RequestMethod.GET, null);
                return showMessageAndRedirect(message, model);
            }

            MemberEntity memberEntity = memberRepository.findByMemberId(memberId).orElse(null);
            newBoardDto = boardService.boardWrite(boardDto, memberEntity);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fileId == null) {
            return "redirect:/board/notice";
        }
        // 새로 생긴 file과 board를 엮는 함수
        fileService.fetchBoard(fileId, newBoardDto.getBoardId());
        return "redirect:/board/notice";
    }// 게시글 작성



    @GetMapping("/board/edit/{boardId}")
    public String editBoard(@PathVariable("boardId") Long boardId, Model model, @SessionAttribute(name = "memberId", required = false) String memberId) {
        MemberEntity loginMember = memberService.getLoginUserByLoginId(memberId);

        if (loginMember == null){
            return "redirect:/";
        }

        BoardDto boardDto = boardService.getBoard(boardId);
        model.addAttribute("board", boardDto);

        Long fileId = boardDto.getFileId();
        if (fileId != null) {
            FileDto fileDto = FileDto.toDto(fileRepository.findByFileId(fileId).get());
            model.addAttribute("filename", fileDto.getOriginFilename());
            model.addAttribute("fileURL",fileDto.getFilename());
        }
        return "/board/boardupdate";
    }

    @PutMapping("/board/edit/{boardId}")
    public String boardEdit(@RequestParam(value = "file", required = false) MultipartFile files,
                            @ModelAttribute BoardDto boardDto, Long boardId, @RequestParam(name = "fileId", required = false) Long originFileId,
                            @SessionAttribute(name = "memberId", required = false) String memberId, Model model) {
        BoardDto newBoardDto = null;
        Long fileId = null;
        if (files==null){
            System.out.println("안수정진입");
            fileId=originFileId;
            boardDto.setFileId(fileId);

            String URL = "/board/edit/"+boardId;
            if (boardDto.getContent().isEmpty()) {
                AlertDTO message = new AlertDTO("본문 내용을 적어야합니다.", URL, RequestMethod.GET, null);
                return showMessageAndRedirect(message, model);
            }

            MemberEntity memberEntity = memberRepository.findByMemberId(memberId).orElse(null);
            newBoardDto = boardService.boardEdit(boardDto.getBoardId(), boardDto);

        }
        else {
            try {
                if (files != null && !files.isEmpty()) {
                    System.out.println("수정진입");
                    String originFilename = files.getOriginalFilename();
                    String filename = new MD5Generator(originFilename).toString();
                    String savePath = fileDir;

                    if (!new File(savePath).exists()) {
                        try {
                            new File(savePath).mkdir();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    String filePath = savePath + "/" + originFilename;
                    files.transferTo(new File(filePath));

                    FileDto fileDto = new FileDto();
                    fileDto.setOriginFilename(originFilename);
                    fileDto.setFilename(filename);
                    fileDto.setFilePath(filePath);

                    fileId = fileService.uploadFile(fileDto);
                    boardDto.setFileId(fileId);
                }

                String URL = "/board/edit/"+boardId;
                if (boardDto.getContent().isEmpty()) {
                    AlertDTO message = new AlertDTO("본문 내용을 적어야합니다.", URL, RequestMethod.GET, null);
                    return showMessageAndRedirect(message, model);
                }

//                MemberEntity memberEntity = memberRepository.findByMemberId(memberId).orElse(null);
                fileService.fetchBoard(fileId, boardId);
                boardService.boardEdit(boardId, boardDto);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }


//        if (fileId == null) {
//            return "redirect:/board/{boardId}";
//        }

        // 새로 생긴 file과 board를 엮는 함수

        return "redirect:/board/{boardId}";
    }// 게시글 수정

    @GetMapping(value = "/board/delete")
    public String boardDelete(@RequestParam("boardId") Long boardId, Long fileId, Model model) {
        String delete = boardService.boardDelete(boardId, fileId);
        if (delete.equals("success")) {
            AlertDTO message = new AlertDTO("삭제되었습니다.", "/board/notice", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
        else {
            AlertDTO message = new AlertDTO("삭제를 실패했습니다..", "/board/notice", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
    }// 게시글 삭제

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> fileDownload(@PathVariable("fileId") Long fileId) throws IOException {
        FileDto fileDto = fileService.getFile(fileId);
        Path path = Paths.get(fileDto.getFilePath());
        Resource resource = new InputStreamResource(Files.newInputStream(path));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("applicaton/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=/"+fileDto.getOriginFilename()+"/")
                .body(resource);
    }

    private String showMessageAndRedirect(final AlertDTO params, Model model) {
        model.addAttribute("params", params);
        return "alert/alertRedirect";
    }
}