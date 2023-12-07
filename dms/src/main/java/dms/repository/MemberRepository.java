package dms.repository;

import dms.domain.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    // 아이디로 회원 정보 조회 ( select * from member where member_id=?)
    Optional<MemberEntity> findByMemberId(String memberId);
    Optional<MemberEntity> findByMemberSequence(Long memberSequence);
    MemberEntity findByMemberName(String memberName);
    MemberEntity findByMemberEmail(String memberEmail);
    boolean existsByMemberId(String memberId);
    boolean existsByMemberEmail(String memberEmail);
}
