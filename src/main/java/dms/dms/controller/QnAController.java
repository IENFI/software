package dms.dms.controller;

import dms.dms.domain.MemberEntity;
import dms.dms.domain.MemberRole;
import dms.dms.dto.AlertDTO;
import dms.dms.dto.MessageDTO;
import dms.dms.dto.QnADTO;
import dms.dms.repository.MemberRepository;
import dms.dms.repository.QnARepository;
import dms.dms.service.MemberService;
import dms.dms.service.QnAService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;

@RequiredArgsConstructor
@Controller
public class QnAController {

    private final QnAService qnaService;
    private final QnARepository qnaRepository;
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    private String showMessageAndRedirect(final AlertDTO params, Model model) {
        model.addAttribute("params", params);
        return "alert/alertRedirect";
    }

    @GetMapping("/qna/writeQnA")
    public String writeQuestionMessage(@SessionAttribute(name = "memberId", required = false) String memberId){
        MemberEntity loginMember = memberService.getLoginUserByLoginId(memberId);

        if (loginMember == null){
            return "redirect:/";
        }


        if (loginMember.getMemberRole()== MemberRole.USER){
            return "/qna/writeQuestionMessage";
        }

        else {
            return "/qna/writeAnswerMessage";
        }
    }

    @PostMapping("/qna/writeQuestion")
    public String writeQuestion(@SessionAttribute(name = "memberId", required = false) String memberId,
                                @ModelAttribute QnADTO qnaDTO, Model model){
        System.out.println("MessageController.Post_write");

        MemberEntity loginMember = memberService.getLoginUserByLoginId(memberId);

        qnaDTO.setUserId(loginMember.getMemberId());

        Long time = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = dateFormat.format(time);
        qnaDTO.setDate(date);

        if(qnaService.write(qnaDTO)== null){
            AlertDTO message = new AlertDTO("문의를 보내지 못했습니다.", "/messages/write", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }

        AlertDTO message = new AlertDTO("문의를 보냈습니다.", "/messages/sent", RequestMethod.GET, null);
        return showMessageAndRedirect(message, model);
    }

    @GetMapping("/qna/question")
    public String questionMessage(@SessionAttribute(name = "memberId", required = false) String memberId, Model model,
                                  @PageableDefault(page = 0, size = 10, sort="date", direction = Sort.Direction.ASC)
                                  Pageable pageable){
        System.out.println("QnAController.questionMessage");
        MemberEntity loginMember = memberService.getLoginUserByLoginId(memberId);

        if (loginMember == null){
            return "redirect:/";
        }

        Page<QnADTO> list = qnaService.findQnAByUserId(memberId, pageable).map(qna -> {
            QnADTO qnaDTO = new QnADTO();
            qnaDTO.setQnaId(qna.getQnaId());
            qnaDTO.setQuestionContent(qna.getQuestionContent());
            qnaDTO.setTitle(qna.getTitle());
            qnaDTO.setUserId(qna.getUser().getMemberId());
            qnaDTO.setDate(qna.getDate());
            if(qna.getAdmin()==null){
                // 답변이 없을 경우 답변 란, 답변한 관리자 아이디 null로 반환
                qnaDTO.setAnswerContent(null);
                qnaDTO.setAdminId(null);
            }
            else {
                // 답변이 있을 경우 답변 란, 답변한 관리자 아이디 입력
                qnaDTO.setAnswerContent(qna.getAnswerContent());
                qnaDTO.setAdminId(qnaDTO.getAdminId());
            }
            return qnaDTO;
        });

        int nowPage = list.getPageable().getPageNumber()+1;
        int startPage = Math.max(nowPage-4,1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());

        model.addAttribute("qnas", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

//        return new Response("성공", "받은 쪽지를 불러왔습니다.", messageService.receivedMessage(memberEntity));
        return "/qna/questionMessage";
    }

    @GetMapping("/qna/answer")
    public String answerMessage(@SessionAttribute(name = "memberId", required = false) String memberId, Model model,
                                  @PageableDefault(page = 0, size = 10, sort="date", direction = Sort.Direction.ASC)
                                  Pageable pageable){
        MemberEntity loginMember = memberService.getLoginUserByLoginId(memberId);

        if (loginMember == null){
            return "redirect:/";
        }

        Page<QnADTO> list = qnaService.findAnsweredQnAByUserId(memberId, pageable).map(qna -> {
            QnADTO qnaDTO = new QnADTO();
            qnaDTO.setQnaId(qna.getQnaId());
            qnaDTO.setQuestionContent(qna.getQuestionContent());
            qnaDTO.setAnswerContent(qna.getAnswerContent());
            qnaDTO.setTitle(qna.getTitle());
            qnaDTO.setUserId(qna.getUser().getMemberId());
            qnaDTO.setDate(qna.getDate());
            qnaDTO.setAdminId(qna.getAdmin().getMemberId());
            return qnaDTO;
        });

        int nowPage = list.getPageable().getPageNumber()+1;
        int startPage = Math.max(nowPage-4,1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());

        model.addAttribute("qnas", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

//        return new Response("성공", "받은 쪽지를 불러왔습니다.", messageService.receivedMessage(memberEntity));
        return "/qna/answerMessage";
    }
}
