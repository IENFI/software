package dms.dto;

import dms.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {
    private Long boardId;
    private String title;
    private String category;
    private String content;
    private String writer;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private Long fileId;
    private String url;

    public static BoardDto toDto(Board board) {
        return new BoardDto(
                board.getBoardId(),
                board.getTitle(),
                board.getCategory(),
                board.getContent(),
                board.getMemberEntity().getMemberId(),
                board.getCreatedDate(),
                board.getModifiedDate(),
                board.getFileId(),
                board.getUrl()
        );
    }
}
