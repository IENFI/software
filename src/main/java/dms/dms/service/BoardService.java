package dms.dms.service;

import dms.dms.domain.Board;
import dms.dms.domain.MemberEntity;
import dms.dms.dto.BoardDto;
import dms.dms.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public Page<Board> getBoards(String category, Pageable pageable) {
        Page<Board> boardPage = boardRepository.findBoardsByCategory(category, pageable);
        return boardPage;
    }

//    @Transactional(readOnly = true)
//    public List<BoardDto> getBoards(String category) {
//        List<Board> boards = boardRepository.findByCategory(category);
//        List<BoardDto> boardDtos = new ArrayList<>();
//        boards.forEach(s -> boardDtos.add(BoardDto.toDto(s)));
//        return boardDtos;
//    }// 전체 게시글 조회



    @Transactional(readOnly = true)
    public BoardDto getBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).get();
        BoardDto boardDto = BoardDto.toDto(board);
        return boardDto;
    }// 개별 게시글 조회

    @Transactional
    public BoardDto boardWrite(BoardDto boardDto, MemberEntity memberEntity) {
        Board board = new Board();
        board.setTitle(boardDto.getTitle());
        board.setCategory(boardDto.getCategory());
        board.setContent(boardDto.getContent());
        board.setMemberEntity(memberEntity);
        board.setCreatedDate(boardDto.getCreatedDate());
        board.setModifiedDate(boardDto.getModifiedDate());
        board.setFileId(boardDto.getFileId());
        board.setUrl(boardDto.getUrl());
        boardRepository.save(board);
        return BoardDto.toDto(board);
    }// 게시글 작성

    @Transactional
    public BoardDto boardEdit(Long boardId, BoardDto boardDto) {
        System.out.println("BoardService.boardEdit");
        Board board = boardRepository.findById(boardId).orElseThrow(() -> {
            return new IllegalArgumentException("게시글이 없습니다.");
        });
        board.setTitle(boardDto.getTitle());
        board.setCategory(boardDto.getCategory());
        board.setContent(boardDto.getContent());
        board.setModifiedDate(boardDto.getModifiedDate());
        board.setFileId(boardDto.getFileId());
        board.setUrl(boardDto.getUrl());
        return BoardDto.toDto(board);
    }// 게시글 수정

    @Transactional
    public String boardDelete(Long boardId, Long fileId) {
        Board board = boardRepository.findById(boardId).get();

        boardRepository.deleteById(boardId);

        Optional<Board> b = boardRepository.findById(boardId);
        if (b.isPresent()) {
            return "fail";
        }
        else {
            return "success";
        }
    }// 게시글 삭제
}
