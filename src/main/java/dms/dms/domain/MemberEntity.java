package dms.dms.domain;

import dms.dms.dto.MemberDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "member") // database에 해당 이름의 테이블 생성
public class MemberEntity { // table 역할
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long memberSequence;

    @Column(unique = true)
    private String memberId;

    @Column(unique = true)
    private String memberEmail;

    @Column
    private String memberPassword;

    private String memberPasswordCheck;

    @Column
    private String memberName;

    @Column
    private String memberPhoneNumber;

    @Column
    private String memberDept;

    @Column
    private String memberInterest;

    @Column
    private MemberRole memberRole;

    // join할 때 쓰는 함수; 필요하면 Role 역할 바꾸고 새함수 만들기
    public static MemberEntity toMemberEntity(MemberDTO memberDTO){
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMemberSequence(memberDTO.getMemberSequence());
        memberEntity.setMemberId(memberDTO.getMemberId());
        memberEntity.setMemberEmail(memberDTO.getMemberEmail());
        memberEntity.setMemberName(memberDTO.getMemberName());
        memberEntity.setMemberPassword(memberDTO.getMemberPassword());
        memberEntity.setMemberPhoneNumber(memberDTO.getMemberPhoneNumber());
        memberEntity.setMemberDept(memberDTO.getMemberDept());
        memberEntity.setMemberInterest(memberDTO.getMemberInterest());
        memberEntity.setMemberRole(MemberRole.USER);
        return memberEntity;
    }
}