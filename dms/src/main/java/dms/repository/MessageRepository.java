package dms.repository;

import dms.domain.MemberEntity;
import dms.domain.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {

    Page<Message> findMessagesByReceiverAndDeletedByReceiverFalse(MemberEntity memberEntity, Pageable pageable);
    Page<Message> findBySenderAndDeletedBySenderFalse(MemberEntity memberEntity, Pageable pageable);
    Message findMessageByMessageId(Long messageId);
}
