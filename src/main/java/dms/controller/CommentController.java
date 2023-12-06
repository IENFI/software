package dms.controller;

import dms.domain.MemberEntity;
import dms.dto.AlertDTO;
import dms.dto.BoardDto;
import dms.dto.CommentDto;
import dms.repository.BoardRepository;
import dms.repository.MemberRepository;
import dms.service.BoardService;
import dms.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final BoardService boardService;

    private String showMessageAndRedirect(final AlertDTO params, Model model) {
        model.addAttribute("params", params);
        return "alert/alertRedirect";
    }

    @PostMapping("/board/{boardId}/comment")
    public String commentWrite(@ModelAttribute CommentDto commentDto, @PathVariable("boardId") Long boardId, @SessionAttribute(name = "memberId", required = false) String memberId, Model model) {
        model.addAttribute("comment", commentDto);

//        BoardDto boardDto = new BoardDto();
//        boardDto.toDto(boardRepository.findById(boardId).get());

        MemberEntity memberEntity = memberRepository.findByMemberId(memberId).orElse(null);

        String URL = "/board/" + boardId;
        if (commentService.commentWrite(boardId, commentDto, memberEntity)!=null){
            AlertDTO message = new AlertDTO("댓글이 등록되었습니다.", URL, RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }

        AlertDTO message = new AlertDTO("댓글이 등록되지 않았습니다.", URL, RequestMethod.GET, null);
        return showMessageAndRedirect(message, model);
    }// 댓글 작성

    @PostMapping("/board/{boardId}/comment/{commentId}/edit")
    public String commentEdit(@ModelAttribute CommentDto commentDto, @PathVariable("boardId") Long boardId, @PathVariable("commentId") Long commentId, @SessionAttribute(name = "memberId", required = false) String memberId) {
        MemberEntity memberEntity = memberRepository.findByMemberId(memberId).get();// 현재 로그인한 아이디
        BoardDto boardDto = new BoardDto();
        boardDto.toDto(boardRepository.findById(boardId).get());// null 고려해야함
        commentService.commentEdit(boardId, commentId, commentDto);
        return "redirect:/board/{boardId}";
    }// 댓글 수정

    @GetMapping("/board/{boardId}/comment/{commentId}/delete")
    public String commentDelete(@PathVariable("boardId") Long boardId, @PathVariable("commentId") Long commentId) {
        commentService.commentDelete(commentId);
        return "redirect:/board/{boardId}";
    }// 댓글 삭제
}
