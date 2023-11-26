package dms.dms.service;

import dms.dms.domain.MemberEntity;
import dms.dms.domain.Message;
import dms.dms.dto.MemberDTO;
import dms.dms.dto.MessageDTO;
import dms.dms.repository.MemberRepository;
import dms.dms.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public MessageDTO write(MessageDTO messageDTO){
        MemberEntity receiver = memberRepository.findByMemberName(messageDTO.getReceiverName());
        MemberEntity sender = memberRepository.findByMemberName(messageDTO.getSenderName());

        Message message = new Message();
        message.setReceiver(receiver);
        message.setSender(sender);

        message.setTitle(messageDTO.getTitle());
        message.setContent(message.getContent());
        message.setDeletedByReceiver(false);
        message.setDeletedBySender(false);
        messageRepository.save(message);

        return MessageDTO.toMessageDTO(message);
    }

    @Transactional(readOnly = true)
    public List<MessageDTO> receivedMessage(MemberEntity memberEntity){
        // 받은 편지함 불러오기
        // 한 명의 유저가 받은 모든 메시지
        // 추후 JWT를 이용해서 재구현 예정
        List<Message> messages = messageRepository.findAllByReceiver(memberEntity);
        List<MessageDTO> messageDTOS = new ArrayList<>();

        for (Message message : messages) {
            // message에서 받은 편지함에서 삭제하지 않았으면 보낼 때 추가해서 보내줌
            if(!message.isDeletedByReceiver()){
                messageDTOS.add(MessageDTO.toMessageDTO(message));
            }
        }
        return messageDTOS;
    }

    // 받은 편지 삭제
    @Transactional
    public Object deleteMessageByReceiver(Long messageId, MemberEntity memberEntity){
        Message message = messageRepository.findById(messageId).orElseThrow(() -> {
            return new IllegalArgumentException("메시지를 찾을 수 없습니다.");
                });

        if (memberEntity == message.getSender()){
            message.deleteByReceiver(); // 받은 사람에게 메시지 삭제
            if (message.isDeleted()){
                // 받은 사람과 보낸 사람 모두 삭제했으면, 데이터베이스에서 삭제 요청
                messageRepository.delete(message);
                return "양쪽 모두 삭제";
            }
            return "한쪽만 삭제";
        } else {
            return new IllegalArgumentException("유저 정보가 일치하지 않습니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<MessageDTO> sentMessage(MemberEntity memberEntity){
        // 보낸 편지함 불러오기
        // 한 명의 유저가 받은 모든 메시지
        // 추후 JWT를 이용해서 재구현 예정
        List<Message> messages = messageRepository.findAllBySender(memberEntity);
        List<MessageDTO> messageDTOS = new ArrayList<>();

        for(Message message : messages){
            // message 에서 받은 편지함에서 삭제하지 않았으면 보낼 때 추가해서 보내줌
            if(!message.isDeletedBySender()){
                messageDTOS.add(MessageDTO.toMessageDTO(message));
            }
        }
        return messageDTOS;
    }

    // 보낸 편지 삭제
    @Transactional
    public Object deleteMessageBySender(Long messageId, MemberEntity memberEntity){
        Message message = messageRepository.findById(messageId).orElseThrow(()->{
            return new IllegalArgumentException("데이터를 찾을 수 없습니다.");
        });

        if(memberEntity == message.getSender()){
            message.deleteBySender(); // 받은 사람에게 메시지 삭제
            if (message.isDeleted()){
                // 받은 사람과 보낸 사람 모두 삭제했으면 데이터베이스에서 삭제 요청
                messageRepository.delete(message);
                return "양쪽 모두 삭제";
            }
            return "한쪽만 삭제";
        } else {
            return new IllegalArgumentException("유저 정보가 일치하지 않습니다.");
        }
    }
}