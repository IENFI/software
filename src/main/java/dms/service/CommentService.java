package dms.service;

import dms.domain.Board;
import dms.domain.Comment;
import dms.domain.MemberEntity;
import dms.dto.CommentDto;
import dms.repository.BoardRepository;
import dms.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public Page<Comment> getComments(Long boardId, Pageable pageable) {
        Board board = boardRepository.findByBoardId(boardId);
        Page<Comment> commentPage = commentRepository.findCommentsByBoard(board, pageable);
        return commentPage;
    }

    @Transactional
    public CommentDto commentWrite(Long boardId, CommentDto commentDto, MemberEntity memberEntity) {
        Board board = boardRepository.findById(boardId).get();
        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());
        comment.setMemberEntity(memberEntity);
        comment.setBoard(board);
        comment.setCreatedDate(commentDto.getCreatedDate());
        comment.setModifiedDate(commentDto.getModifiedDate());
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
