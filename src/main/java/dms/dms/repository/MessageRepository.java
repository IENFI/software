package dms.dms.repository;

import dms.dms.domain.MemberEntity;
import dms.dms.domain.Message;
import dms.dms.domain.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    Page<Message> findMessagesByReceiverAndDeletedByReceiverFalse(MemberEntity memberEntity, Pageable pageable);
    Page<Message> findBySenderAndDeletedBySenderFalse(MemberEntity memberEntity, Pageable pageable);
    Message findMessageByMessageId(Long messageId);
}
