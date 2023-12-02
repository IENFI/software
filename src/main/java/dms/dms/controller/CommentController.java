package dms.dms.controller;


import dms.dms.domain.MemberEntity;
import dms.dms.dto.BoardDto;
import dms.dms.dto.CommentDto;
import dms.dms.repository.BoardRepository;
import dms.dms.repository.MemberRepository;
import dms.dms.service.BoardService;
import dms.dms.service.CommentService;
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

    @PostMapping("/board/{boardId}/comment")
    public String commentWrite(@ModelAttribute CommentDto commentDto, @PathVariable("boardId") Long boardId, @SessionAttribute(name = "memberId", required = false) String memberId, Model model) {
        model.addAttribute("comment", commentDto);

        BoardDto boardDto = new BoardDto();
        boardDto.toDto(boardRepository.findById(boardId).get());
        MemberEntity memberEntity = memberRepository.findByMemberId(memberId).orElse(null);
        commentService.commentWrite(boardId, commentDto, memberEntity);
        return "redirect:/board/{boardId}";
    }// 댓글 작성

    @PutMapping("/board/{boardId}/comment/edit")
    public String commentEdit(@ModelAttribute CommentDto commentDto, Long boardId, Long commentId, Model model, @SessionAttribute(name = "memberId", required = false) String memberId) {
        MemberEntity memberEntity = memberRepository.findByMemberId(memberId).get();// 현재 로그인한 아이디
        BoardDto boardDto = new BoardDto();
        boardDto.toDto(boardRepository.findById(boardId).get());// null 고려해야함
        commentService.commentEdit(boardId, commentId, commentDto);
        return "redirect:/board/{boardId}";
    }// 댓글 수정

    @GetMapping("/board/{boardId}/comment/delete")
    public String commentDelete(@PathVariable("commentId") Long commentId) {
        commentService.commentDelete(commentId);
        return "redirect:/board/{boardId}";
    }// 댓글 삭제
}
