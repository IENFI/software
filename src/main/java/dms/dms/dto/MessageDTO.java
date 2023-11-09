package dms.dms.dto;

import dms.dms.domain.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    private String title;
    private String content;
    private String senderName;
    private String receiverName;

    public static MessageDTO toMessageDTO(Message message){
        return new MessageDTO(
                message.getTitle(),
                message.getContent(),
                message.getSender().getMemberName(),
                message.getReceiver().getMemberName()
        );
    }
}
