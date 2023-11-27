package dms.dms.repository;

import dms.dms.domain.MemberEntity;
import dms.dms.domain.Message;
import dms.dms.domain.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByReceiver(MemberEntity memberEntity);
    List<Message> findAllBySender(MemberEntity memberEntity);

    Page<Message> findMessagesByReceiver(MemberEntity memberEntity, Pageable pageable);
}
