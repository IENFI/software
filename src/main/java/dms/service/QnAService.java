package dms.service;

import dms.domain.MemberEntity;
import dms.domain.QnA;
import dms.dto.QnADTO;
import dms.repository.MemberRepository;
import dms.repository.QnARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class QnAService {

    private final MemberRepository memberRepository;
    private final QnARepository qnaRepository;

    @Transactional
    public QnADTO write(QnADTO qnaDTO){
        MemberEntity user = memberRepository.findByMemberId(qnaDTO.getUserId()).get();

        QnA qna = new QnA();
        qna.setUser(user);

        qna.setTitle(qnaDTO.getTitle());
        qna.setQuestionContent(qnaDTO.getQuestionContent());
        qna.setDate(qnaDTO.getDate());
        qnaRepository.save(qna);

        return new QnADTO(
                qna.getQnaId(),
                qna.getTitle(),
                qna.getQuestionContent(),
                null,
                qna.getUser().getMemberId(),
                null,
                qna.getDate()
        );
    }

    @Transactional
    public Boolean answer(QnADTO qnaDTO, Long qnaId){
        MemberEntity admin = memberRepository.findByMemberId(qnaDTO.getAdminId()).get();
        if (admin==null){
            return false;
        }
        QnA qna = qnaRepository.findQnAByQnaId(qnaId);
        qna.setAdmin(admin);
        qna.setAnswerContent(qnaDTO.getAnswerContent());

        qnaRepository.save(qna);

        return true;
    }

    @Transactional
    public QnA findOneQnA(Long qnaId){
        return qnaRepository.findQnAByQnaId(qnaId);
    }

    // admin 쪽 함수
    @Transactional
    public Page<QnA> findAllQnA(Pageable pageable){
        // 답변이 되지 않은 QNA 전부 반환하는 함수
        return qnaRepository.findQnASByAdminIsNull(pageable);
    }

    public Page<QnA> findQnAByAdminId(String memberId, Pageable pageable){
        Optional<MemberEntity> memberEntityOptional = memberRepository.findByMemberId(memberId);

        if (memberEntityOptional.isPresent()){
            return qnaRepository.findQnAByAdmin(memberEntityOptional.get(), pageable);
        }
        else {
            return Page.empty();
        }
    }


    // user 쪽 함수
    @Transactional
    public Page<QnA> findQnAByUserId(String memberId, Pageable pageable){
        Optional<MemberEntity> memberEntityOptional = memberRepository.findByMemberId(memberId);

        if (memberEntityOptional.isPresent()) {
            return qnaRepository.findQnASByUser(memberEntityOptional.get(), pageable);
        } else {
            // 멤버가 존재하지 않을 때의 처리
            // 예를 들어, 빈 페이지(Page.empty())를 반환하거나 예외를 던지는 등의 방법을 선택할 수 있습니다.
            return Page.empty();
        }
    }

    @Transactional
    public Page<QnA> findAnsweredQnAByUserId(String memberId, Pageable pageable){
        // 답변된 QnA만 찾는 함수
        Optional<MemberEntity> memberEntityOptional = memberRepository.findByMemberId(memberId);

        if (memberEntityOptional.isPresent()) {
            return qnaRepository.findQnASByUserAndAdminIsNotNull(memberEntityOptional.get(), pageable);
        } else {
            // 멤버가 존재하지 않을 때의 처리
            // 예를 들어, 빈 페이지(Page.empty())를 반환하거나 예외를 던지는 등의 방법을 선택할 수 있습니다.
            return Page.empty();
        }
    }
}
