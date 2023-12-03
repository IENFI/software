package dms.dms.dto;

import dms.dms.domain.Message;
import dms.dms.domain.QnA;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QnADTO {
    private Long qnaId;
    private String title;
    private String questionContent;
    private String answerContent;
    private String userId;
    private String adminId;
    private String date;

    public static QnADTO toQnADTO(QnA qna){
        return new QnADTO(
                qna.getQnaId(),
                qna.getTitle(),
                qna.getQuestionContent(),
                qna.getAnswerContent(),
                qna.getUser().getMemberId(),
                qna.getAdmin().getMemberId(),
                qna.getDate()
        );
    }
}
