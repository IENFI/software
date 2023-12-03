package dms.dms.service;

import dms.dms.domain.MemberEntity;
import dms.dms.domain.Message;
import dms.dms.dto.MessageDTO;
import dms.dms.repository.MemberRepository;
import dms.dms.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Message findMessageByMessageId(Long messageId){
        return messageRepository.findMessageByMessageId(messageId);
    }

    @Transactional
    public MessageDTO write(MessageDTO messageDTO){
        MemberEntity receiver = memberRepository.findByMemberId(messageDTO.getReceiverId()).get();
        MemberEntity sender = memberRepository.findByMemberId(messageDTO.getSenderId()).get();

        if (receiver==sender){
            return null;
        }

        Message message = new Message();
        message.setReceiver(receiver);
        message.setSender(sender);

        message.setTitle(messageDTO.getTitle());
        message.setContent(messageDTO.getContent());
        message.setDeletedByReceiver(false);
        message.setDeletedBySender(false);
        message.setDate(messageDTO.getDate());
        messageRepository.save(message);

        return MessageDTO.toMessageDTO(message);
    }

    @Transactional
    public Page<Message> findMessageReceiverByMemberId(String memberID, Pageable pageable) {
        Optional<MemberEntity> memberEntityOptional = memberRepository.findByMemberId(memberID);

        if (memberEntityOptional.isPresent()) {
            return messageRepository.findMessagesByReceiverAndDeletedByReceiverFalse(memberEntityOptional.get(), pageable);
        } else {
            // 멤버가 존재하지 않을 때의 처리
            // 예를 들어, 빈 페이지(Page.empty())를 반환하거나 예외를 던지는 등의 방법을 선택할 수 있습니다.
            return Page.empty();
        }
    }

    @Transactional
    public Page<Message> findMessageSenderByMemberId(String memberID, Pageable pageable) {
        Optional<MemberEntity> memberEntityOptional = memberRepository.findByMemberId(memberID);

        if (memberEntityOptional.isPresent()) {
            return messageRepository.findBySenderAndDeletedBySenderFalse(memberEntityOptional.get(), pageable);
        } else {
            // 멤버가 존재하지 않을 때의 처리
            // 예를 들어, 빈 페이지(Page.empty())를 반환하거나 예외를 던지는 등의 방법을 선택할 수 있습니다.
            return Page.empty();
        }
    }


    // 받은 편지 삭제
    @Transactional
    public String deleteMessageByReceiver(List<Long> selectedMessages, MemberEntity memberEntity){
        if (selectedMessages != null) {
            for (Long messageId : selectedMessages) {
                // messageId를 사용한 처리
                // 예를 들어, messageId를 가지고 해당 메시지를 삭제하는 로직을 수행할 수 있습니다.
                Message message = messageRepository.findById(messageId).orElseThrow(()->{
                    return new IllegalArgumentException("데이터를 찾을 수 없습니다.");
                });

                if(memberEntity == message.getReceiver()){
                    message.deleteByReceiver(); // 보낸 사람에게 메시지 삭제
                    if (message.isDeleted()){
                        // 받은 사람과 보낸 사람 모두 삭제했으면 데이터베이스에서 삭제 요청
                        messageRepository.delete(message);
                        // 양쪽 모두 삭제
                    }
                    // if 문에 들어가지 않으면 한쪽만 삭제
                } else {
                    return "유저 정보가 일치하지 않습니다.";
                }
            }
        }
        else {
            return "Empty Message";
        }
        return "success";
    }

    // 보낸 편지 삭제
    @Transactional
    public String deleteMessageBySender(List<Long> selectedMessages, MemberEntity memberEntity){
        if (selectedMessages != null) {
            for (Long messageId : selectedMessages) {
                // messageId를 사용한 처리
                // 예를 들어, messageId를 가지고 해당 메시지를 삭제하는 로직을 수행할 수 있습니다.
                Message message = messageRepository.findById(messageId).orElseThrow(()->{
                    return new IllegalArgumentException("데이터를 찾을 수 없습니다.");
                });

                if(memberEntity == message.getSender()){
                    message.deleteBySender(); // 받은 사람에게 메시지 삭제
                    if (message.isDeleted()){
                        // 받은 사람과 보낸 사람 모두 삭제했으면 데이터베이스에서 삭제 요청
                        messageRepository.delete(message);
                        // 양쪽 모두 삭제
                    }
                    // if 문에 들어가지 않으면 한쪽만 삭제
                } else {
                    return "유저 정보가 일치하지 않습니다.";
                }
            }
        }
        else {
            return "Empty Message";
        }
        return "success";
    }
}