package dms.dms.repository;

import dms.dms.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByCategory(String category);
    Page<Board> findBoardsByCategory(String category, Pageable pageable);
    Board findByBoardId(Long boardId);
    Optional<Board> findByFileId(Long fileId);
}
