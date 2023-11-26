package dms.dms.service;

import dms.dms.domain.MemberEntity;
import dms.dms.dto.MemberDTO;
import dms.dms.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
//import java.lang.foreign.SymbolLookup;
import java.util.Optional;

@Service // 스프링이 관리해주는 객체 == 스프링 빈
@RequiredArgsConstructor // controller와 같이 final 멤버 변수 생성자 만드는 역할
public class MemberService {

    private final MemberRepository memberRepository; // 먼저 jpa, mysql dependency 추가

    public MemberEntity getMember(String memberId) {
        return memberRepository.findByMemberId(memberId).get();
    }

    public void save(MemberDTO memberDTO) {
        // repository의 save 메서드 호출
        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
        memberRepository.save(memberEntity);
        // Repository의 save 메서드 호출 (조건. entity 객체를 넘겨줘야 함)

    }

    // 탈퇴 기능
    @Transactional
    public Boolean delete(String memberId, String nowPassword) {
        MemberEntity memberEntity = memberRepository.findByMemberId(memberId).get();
        System.out.println("MemberService.delete");
        if (memberEntity.getMemberPassword().equals(nowPassword)) {

            // 게시글, 댓글, 일정, 포스트 등을 전부 삭제하는 코드 구현

//            List<Like> likes = likeRepository.findAllByUserLoginId(loginId);
//            for (Like like : likes) {
//                like.getBoard().likeChange( like.getBoard().getLikeCnt() - 1 );
//            }
//
//            List<Comment> comments = commentRepository.findAllByUserLoginId(loginId);
//            for (Comment comment : comments) {
//                comment.getBoard().commentChange( comment.getBoard().getCommentCnt() - 1 );
//            }

            memberRepository.delete(memberEntity);
            return true;
        } else {
            return false;
        }
    }

    public MemberEntity login(MemberDTO memberDTO) { // entity 객체는 service에서만
        System.out.println("로그인 시도 중");
        Optional<MemberEntity> byMemberId = memberRepository.findByMemberId(memberDTO.getMemberId());
        if (byMemberId.isPresent()) {
            // 조회 결과가 있다
            MemberEntity memberEntity = byMemberId.get(); // Optional에서 꺼냄
            if (memberEntity.getMemberPassword().equals(memberDTO.getMemberPassword())) {
                // 비밀번호 일치
                // entity -> dto 변환 후 리턴
                return memberEntity;
            } else {
                // 비밀번호 불일치
                return null;
            }

        } else {
            System.out.println("로그인 시도 실패");
            // 조회 결과가 없다
            return null;
        }
    }

    // ID 중복체크
    public boolean checkMemberIdDuplicate(String memberId) {
        return memberRepository.existsByMemberId(memberId);
    }

    public boolean checkMemberEmailDuplicate(String memberEmail) {
        return memberRepository.existsByMemberEmail(memberEmail);
    }

    // Sequence를 이용하여 Member를 return 해주는 기능
//    public MemberEntity getLoginUserById(Long memberSequence){
//        if (memberSequence == null) return null;
//
//        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberSequence(memberSequence);
//        if (optionalMemberEntity.isEmpty()) return null;
//
//        return optionalMemberEntity.get();
//    }

    // Id를 이용하여 Member를 return 해주는 기능
    public MemberEntity getLoginUserByLoginId(String memberId) {
        if (memberId == null) return null;

        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberId(memberId);
        if (optionalMemberEntity.isEmpty()) return null;

        return optionalMemberEntity.get();
    }

    @Transactional
    public String editMemberEmail(String memberId, String memberEmail) {
        if (memberId == null || memberEmail == null) return null;

        Optional<MemberEntity> byMemberId = memberRepository.findByMemberId(memberId);
        if (byMemberId.isPresent()) {
            // 조회 결과가 있다
            MemberEntity memberEntity = byMemberId.get(); // Optional에서 꺼냄
            if (memberEntity.getMemberEmail() == memberEmail)
                return "equal";
            memberEntity.setMemberEmail(memberEmail);
            return "success";

        } else {
            System.out.println("수정할 멤버 조회 불가");
            // 조회 결과가 없다
            return "fail";
        }
    }

    @Transactional
    public String editMemberDept(String memberId, String memberDept) {
        if (memberId == null || memberDept == null) return null;

        Optional<MemberEntity> byMemberId = memberRepository.findByMemberId(memberId);
        if (byMemberId.isPresent()) {
            // 조회 결과가 있다
            MemberEntity memberEntity = byMemberId.get(); // Optional에서 꺼냄
            if (memberEntity.getMemberDept() == memberDept)
                return "equal";
            memberEntity.setMemberDept(memberDept);
            return "success";

        } else {
            System.out.println("수정할 멤버 조회 불가");
            // 조회 결과가 없다
            return "fail";
        }
    }

    @Transactional
    public String editMemberInterest(String memberId, String memberInterest) {
        if (memberId == null || memberInterest == null) return null;

        Optional<MemberEntity> byMemberId = memberRepository.findByMemberId(memberId);
        if (byMemberId.isPresent()) {
            // 조회 결과가 있다
            MemberEntity memberEntity = byMemberId.get(); // Optional에서 꺼냄
            if (memberEntity.getMemberInterest() == memberInterest)
                return "equal";
            memberEntity.setMemberInterest(memberInterest);
            return "success";

        } else {
            System.out.println("수정할 멤버 조회 불가");
            // 조회 결과가 없다
            return "fail";
        }
    }



}