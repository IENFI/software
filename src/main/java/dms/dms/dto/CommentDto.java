package dms.dms.dto;

import dms.dms.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long commentId;
    private String content;
    private String writer;
    private Long boardId;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public static CommentDto toDto(Comment comment) {
        return new CommentDto(
                comment.getCommentId(),
                comment.getContent(),
                comment.getMemberEntity().getMemberId(),
                comment.getBoard().getBoardId(),
                comment.getCreatedDate(),
                comment.getModifiedDate()
        );
    }
}
