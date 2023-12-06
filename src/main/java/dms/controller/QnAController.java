package dms.controller;

import dms.domain.MemberEntity;
import dms.domain.MemberRole;
import dms.domain.QnA;
import dms.dto.AlertDTO;
import dms.dto.QnADTO;
import dms.repository.MemberRepository;
import dms.repository.QnARepository;
import dms.service.MemberService;
import dms.service.QnAService;
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
        return "/qna/writeQuestionMessage";
    }

    @PostMapping("/qna/writeAnswer/{qnaId}")
    public String writeAnswer(@SessionAttribute(name = "memberId", required = false) String memberId,
                              @ModelAttribute QnADTO qnaDTO, Model model,
                              @PathVariable("qnaId") Long qnaId){
        // html에서 넘어오는 건 관리자의 memberId, qnaId와 answerContent밖에 없음
        System.out.println("QnAController.writeAnswer");

        MemberEntity loginMember = memberService.getLoginUserByLoginId(memberId);
        qnaDTO.setAdminId(loginMember.getMemberId());

        // 답변한 시간 저장하기
//        Long time = System.currentTimeMillis();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String date = dateFormat.format(time);
//        qnaDTO.setDate(date);

        if(qnaService.answer(qnaDTO, qnaId)){
            AlertDTO message = new AlertDTO("문의에 답변했습니다.", "/qna/answeredQuestion", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
        String URL = "/qna/writeAnswer/"+qnaId;
        AlertDTO message = new AlertDTO("답변을 실패했습니다.", URL, RequestMethod.GET, null);
        return showMessageAndRedirect(message, model);
    }

    @GetMapping("/qna/writeAnswer/{qnaId}")
    public String writeAnswerMessage(@SessionAttribute(name = "memberId", required = false) String memberId,
                                     Model model, @PathVariable("qnaId") Long qnaId){
        System.out.println("QnAController.writeAnswerMessage");

        MemberEntity loginMember = memberService.getLoginUserByLoginId(memberId);
        QnA qna = qnaService.findOneQnA(qnaId);

        if (loginMember == null){
            return "redirect:/";
        }

        model.addAttribute("qna",qna);

        return "/qna/writeAnswerMessage";
    }


    @GetMapping("/qna/qnaContent")
    public String qnaContent(@SessionAttribute(name = "memberId", required = false) String memberId, @RequestParam("qnaId") Long qnaId, Model model) { // 공부 기록 상세 보기
        // qna가 조회되지 않을 때의 알림창 구분하기
        MemberEntity loginMember = memberService.getLoginUserByLoginId(memberId);
        if (loginMember == null){
            return "redirect:/";
        }

        QnA qna = qnaService.findOneQnA(qnaId);

        if (qna==null){
            AlertDTO message = new AlertDTO("QnA가 조회되지 않습니다.", "/qna/question", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
        model.addAttribute("qna", qna);
        model.addAttribute("role",loginMember.getMemberRole().equals(MemberRole.ADMIN));

        return "/qna/qnaContent";
    }

    @GetMapping("/qna/ansContent")
    public String ansContent(@SessionAttribute(name = "memberId", required = false) String memberId, @RequestParam("qnaId") Long qnaId, Model model) { // 공부 기록 상세 보기
        // qna가 조회되지 않을 때의 알림창 구분하기
        MemberEntity loginMember = memberService.getLoginUserByLoginId(memberId);
        if (loginMember == null){
            return "redirect:/";
        }

        QnA qna = qnaService.findOneQnA(qnaId);

        if (qna==null){
            AlertDTO message;
            if (loginMember.getMemberRole()==MemberRole.USER){
                message = new AlertDTO("QnA가 조회되지 않습니다.", "/qna/answer", RequestMethod.GET, null);
            }
            else {
                message = new AlertDTO("QnA가 조회되지 않습니다.", "/qna/answeredQuestion", RequestMethod.GET, null);
            }
            return showMessageAndRedirect(message, model);
        }
        model.addAttribute("qna", qna);

        return "/qna/ansContent";
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
                                  @PageableDefault(page = 0, size = 10, sort="date", direction = Sort.Direction.DESC)
                                  Pageable pageable){
        System.out.println("QnAController.questionMessage");
        MemberEntity loginMember = memberService.getLoginUserByLoginId(memberId);

        if (loginMember == null){
            return "redirect:/";
        }

        if (loginMember.getMemberRole() == MemberRole.USER){
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
                    qnaDTO.setAdminId(qna.getAdmin().getMemberId());
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

        else {
            Page<QnADTO> list = qnaService.findAllQnA(pageable).map(qna -> {
                QnADTO qnaDTO = new QnADTO();
                qnaDTO.setQnaId(qna.getQnaId());
                qnaDTO.setQuestionContent(qna.getQuestionContent());
                qnaDTO.setTitle(qna.getTitle());
                qnaDTO.setUserId(qna.getUser().getMemberId());
                qnaDTO.setDate(qna.getDate());
                qnaDTO.setAnswerContent(null);
                qnaDTO.setAdminId(null);
                return qnaDTO;
            });

            int nowPage = list.getPageable().getPageNumber()+1;
            int startPage = Math.max(nowPage-4,1);
            int endPage = Math.min(nowPage + 5, list.getTotalPages());

            model.addAttribute("qnas", list);
            model.addAttribute("nowPage", nowPage);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);

            return "/qna/adminQuestionMessage";
        }
    }

    @GetMapping("/qna/answeredQuestion")
    public String answeredQuestionMessage(@SessionAttribute(name = "memberId", required = false) String memberId, Model model,
                                          @PageableDefault(page = 0, size = 10, sort="date", direction = Sort.Direction.DESC)
                                          Pageable pageable){
        System.out.println("QnAController.answeredQuestionMessage");
        MemberEntity loginMember = memberService.getLoginUserByLoginId(memberId);

        if (loginMember == null){
            return "redirect:/";
        }

        if (loginMember.getMemberRole() == MemberRole.USER){
            return "redirect:/qna/question";
        }

        else {
            Page<QnADTO> list = qnaService.findQnAByAdminId(memberId, pageable).map(qna -> {
                QnADTO qnaDTO = new QnADTO();
                qnaDTO.setQnaId(qna.getQnaId());
                qnaDTO.setQuestionContent(qna.getQuestionContent());
                qnaDTO.setTitle(qna.getTitle());
                qnaDTO.setUserId(qna.getUser().getMemberId());
                qnaDTO.setDate(qna.getDate());
                qnaDTO.setAnswerContent(null);
                qnaDTO.setAdminId(null);
                return qnaDTO;
            });

            int nowPage = list.getPageable().getPageNumber()+1;
            int startPage = Math.max(nowPage-4,1);
            int endPage = Math.min(nowPage + 5, list.getTotalPages());

            model.addAttribute("qnas", list);
            model.addAttribute("nowPage", nowPage);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);

            return "/qna/adminAnsweredMessage";
        }
    }

    @GetMapping("/qna/answer")
    public String answerMessage(@SessionAttribute(name = "memberId", required = false) String memberId, Model model,
                                @PageableDefault(page = 0, size = 10, sort="date", direction = Sort.Direction.DESC)
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
