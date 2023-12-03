package dms.dms.repository;

import dms.dms.domain.Board;
import dms.dms.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByCommentId(Long commentId);
    Page<Comment> findCommentsByBoard(Board board, Pageable pageable);
}
