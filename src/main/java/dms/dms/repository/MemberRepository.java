package dms.dms.repository;

import dms.dms.domain.MemberEntity;
import dms.dms.domain.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // 첫번째 인자 : 어떤 Entity인지, 두번째 인자 : pk 어떤 타입인지
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    // 아이디로 회원 정보 조회 ( select * from member where member_id=?)
    Optional<MemberEntity> findByMemberId(String memberId);
    Optional<MemberEntity> findByMemberSequence(Long memberSequence);
    MemberEntity findByMemberName(String memberName);
    boolean existsByMemberId(String memberId);
    boolean existsByMemberEmail(String memberEmail);
}