package dms.dms.repository;

import dms.dms.domain.MemberEntity;
import dms.dms.domain.Message;
import dms.dms.domain.QnA;
import dms.dms.domain.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QnARepository extends JpaRepository<QnA, Long> {
    Page<QnA> findQnASByUser(MemberEntity memberEntity, Pageable pageable);
    Page<QnA> findQnASByUserAndAdminIsNotNull(MemberEntity memberEntity, Pageable pageable);
}
