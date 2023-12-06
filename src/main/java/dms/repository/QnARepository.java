package dms.repository;

import dms.domain.MemberEntity;
import dms.domain.QnA;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnARepository extends JpaRepository<QnA, Long> {
    Page<QnA> findQnASByUser(MemberEntity memberEntity, Pageable pageable);
    Page<QnA> findQnAByAdmin(MemberEntity memberEntity, Pageable pageable);
    Page<QnA> findQnASByUserAndAdminIsNotNull(MemberEntity memberEntity, Pageable pageable);
    Page<QnA> findQnASByAdminIsNull(Pageable pageable);
    QnA findQnAByQnaId(Long qnaId);
}
