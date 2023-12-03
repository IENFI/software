package dms.dms.dto;

import dms.dms.domain.MemberEntity;
import dms.dms.domain.MemberEntity;
import dms.dms.domain.MemberRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MemberDTO { // 회원 정보를 필드로 정의
    private Long memberSequence;
    private String memberId;
    private String memberEmail;
    private String memberPassword;
    private String memberPasswordCheck; // 중복 체크 시 사용.. 필요없어짐!
    private String memberName;
    private String memberPhoneNumber;
    private String memberDept;
    private String memberInterest;
    private MemberRole memberRole;

    //lombok 어노테이션으로 getter,setter,생성자,toString 메서드 생략 가능

    public static MemberDTO toMemberDTO(MemberEntity memberEntity){
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMemberSequence(memberEntity.getMemberSequence());
        memberDTO.setMemberId(memberEntity.getMemberId());
        memberDTO.setMemberEmail(memberEntity.getMemberEmail());
        memberDTO.setMemberName(memberEntity.getMemberName());
        memberDTO.setMemberPassword(memberEntity.getMemberPassword());
        memberDTO.setMemberPhoneNumber(memberEntity.getMemberPhoneNumber());
        memberDTO.setMemberDept(memberEntity.getMemberDept());
        memberDTO.setMemberInterest(memberEntity.getMemberInterest());
        memberDTO.setMemberRole(memberEntity.getMemberRole());
        return memberDTO;
    }
}