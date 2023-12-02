package dms.dms.service;

import dms.dms.domain.Board;
import dms.dms.domain.Comment;
import dms.dms.domain.MemberEntity;
import dms.dms.dto.CommentDto;
import dms.dms.repository.BoardRepository;
import dms.dms.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public List<CommentDto> getComments(Long commentId) {
        List<Comment> comments = commentRepository.findByCommentId(commentId);
        List<CommentDto> commentDtos = new ArrayList<>();
        comments.forEach(s -> commentDtos.add(CommentDto.toDto(s)));
        return commentDtos;
    }

    @Transactional
    public CommentDto getComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).get();
        CommentDto commentDto = CommentDto.toDto(comment);
        return commentDto;
    }

    @Transactional
    public CommentDto commentWrite(Long boardId, CommentDto commentDto, MemberEntity memberEntity) {
        Board board = boardRepository.findById(boardId).get();
        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());
        comment.setMemberEntity(memberEntity);
        comment.setBoard(board);
        commentRepository.save(comment);
        return CommentDto.toDto(comment);
    }

    @Transactional
    public CommentDto commentEdit(Long boardId, Long commentId, CommentDto commentDto) {
        Board board = boardRepository.findById(boardId).get();
        Comment comment = commentRepository.findById(commentId).get();
        comment.setContent(commentDto.getContent());
        comment.setBoard(board);
        return CommentDto.toDto(comment);
    }

    @Transactional
    public void commentDelete(Long commentId) {
        Comment comment = commentRepository.findById(commentId).get();
        commentRepository.deleteById(commentId);
    }
}
