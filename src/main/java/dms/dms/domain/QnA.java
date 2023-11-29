package dms.dms.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class QnA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qnaId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String questionContent;

    @Column
    private String answerContent;

    @Column(nullable = false)
    private String date;

    //MemberEntity가 맞을까??
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private MemberEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private MemberEntity admin;
}
