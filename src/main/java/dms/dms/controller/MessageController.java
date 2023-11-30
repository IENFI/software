package dms.dms.controller;

import dms.dms.domain.MemberEntity;
import dms.dms.dto.AlertDTO;
import dms.dms.dto.MessageDTO;
import dms.dms.repository.MemberRepository;
import dms.dms.service.MemberService;
import dms.dms.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class MessageController {

    private final MessageService messageService;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    // 알림창
    private String showMessageAndRedirect(final AlertDTO params, Model model) {
        model.addAttribute("params", params);
        return "alert/alertRedirect";
    }

    // 쪽지 쓰기 창
    @GetMapping("/messages/write")
    public String writeMessage(@SessionAttribute(name = "memberId", required = false) String memberId){

        MemberEntity loginMember = memberService.getLoginUserByLoginId(memberId);

        if (loginMember == null){
            return "redirect:/";
        }

        return "/messages/writeMessage";
    }

// @ApiOperation(value = "쪽지 보내기", notes = "쪽지 보내기") <-- swagger v2
//    @Operation(summary = "쪽지 보내기", description = "쪽지 보내기")
//    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/messages/write")
    public String sendMessage(@SessionAttribute(name = "memberId", required = false) String memberId,
                              @RequestParam("receiverId") String receiverId,
                              @ModelAttribute MessageDTO messageDTO, Model model) {
        // 임의로 유저 정보를 넣었지만, JWT 도입하고 현재 로그인 된 유저의 정보를 넘겨줘야함
        System.out.println("MessageController.Post_write");

        MemberEntity loginMember = memberService.getLoginUserByLoginId(memberId);
        System.out.println(receiverId);
        MemberEntity receiverMember = memberService.getLoginUserByLoginId(receiverId);

        if(receiverMember== null){
            AlertDTO message = new AlertDTO("사용자가 존재하지 않습니다.", "/messages/write", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }

        messageDTO.setSenderId(loginMember.getMemberId());
        messageDTO.setReceiverId(receiverMember.getMemberId());

        Long time = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = dateFormat.format(time);
        messageDTO.setDate(date);

        if(messageService.write(messageDTO)== null){
            AlertDTO message = new AlertDTO("본인에게 보낼 수 없습니다.", "/messages/write", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }

        AlertDTO message = new AlertDTO("쪽지를 보냈습니다.", "/messages/sent", RequestMethod.GET, null);
        return showMessageAndRedirect(message, model);
    }


    @Operation(summary = "받은 편지함 읽기", description = "받은 편지함 확인")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/messages/received")
    public String getReceivedMessage(@SessionAttribute(name = "memberId", required = false) String memberId, Model model,
                                     @PageableDefault(page = 0, size = 10, sort="date", direction = Sort.Direction.ASC)
                                         Pageable pageable)
    {
        // 임의로 유저 정보를 넣었지만, JWT 도입하고 현재 로그인 된 유저의 정보를 넘겨줘야함
//        MemberEntity memberEntity = memberRepository.findByMemberId(memberId).orElseThrow(() -> {
//            return new IllegalArgumentException("유저를 찾을 수 없습니다.");
//        });

        MemberEntity loginMember = memberService.getLoginUserByLoginId(memberId);

        if (loginMember == null){
            return "redirect:/";
        }

        Page<MessageDTO> list = messageService.findMessageReceiverByMemberId(memberId, pageable).map(message -> {
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setMessageId(message.getMessageId());
            messageDTO.setContent(message.getContent());
            messageDTO.setReceiverId(message.getReceiver().getMemberId());
            messageDTO.setTitle(message.getTitle());
            messageDTO.setSenderId(message.getSender().getMemberId());
            messageDTO.setDate(message.getDate());
            return messageDTO;
        });

        int nowPage = list.getPageable().getPageNumber()+1;
        int startPage = Math.max(nowPage-4,1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());

        model.addAttribute("messages", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

//        return new Response("성공", "받은 쪽지를 불러왔습니다.", messageService.receivedMessage(memberEntity));
        return "/messages/receivedMessage";
    }


    @Operation(summary = "받은 쪽지 삭제하기", description = "받은 쪽지를 삭제합니다.")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/messages/deleteReceivedMessage")
    public String deleteReceivedMessage(@SessionAttribute(name = "memberId", required = false) String memberId,
                                    @RequestParam(value = "selectedMessages", required = false) List<Long> selectedMessages,
                                    Model model) {
        // 임의로 유저 정보를 넣었지만, JWT 도입하고 현재 로그인 된 유저의 정보를 넘겨줘야함
//        MemberEntity memberEntity = memberRepository.findByMemberSequence(Long.valueOf(1)).orElseThrow(() -> {
//            return new IllegalArgumentException("유저를 찾을 수 없습니다.");
//        });
        MemberEntity loginMember = memberService.getLoginUserByLoginId(memberId);

        if (loginMember == null){
            return "redirect:/";
        }

        if (messageService.deleteMessageByReceiver(selectedMessages, loginMember).equals("success")){
            AlertDTO message = new AlertDTO("쪽지를 삭제했습니다.", "/messages/received", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
        else if (messageService.deleteMessageByReceiver(selectedMessages, loginMember).equals("Empty Message")){
            AlertDTO message = new AlertDTO("삭제할 쪽지가 없습니다.", "/messages/received", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
        else {
            AlertDTO message = new AlertDTO("유저 정보 오류.", "/messages/received", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }

//        return new Response<>("삭제 성공", "보낸 쪽지인, " + id + "번 쪽지를 삭제했습니다.", messageService.deleteMessageBySender(id, memberEntity));
    }





    @Operation(summary = "보낸 편지함 읽기", description = "보낸 편지함 확인")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/messages/sent")
    public String getSentMessage(@SessionAttribute(name = "memberId", required = false) String memberId, Model model,
                                     @PageableDefault(page = 0, size = 10, sort="date", direction = Sort.Direction.ASC)
                                     Pageable pageable)
    {
        // 임의로 유저 정보를 넣었지만, JWT 도입하고 현재 로그인 된 유저의 정보를 넘겨줘야함
//        MemberEntity memberEntity = memberRepository.findByMemberSequence(Long.valueOf(1)).orElseThrow(() -> {
//            return new IllegalArgumentException("유저를 찾을 수 없습니다.");
//        });
        MemberEntity loginMember = memberService.getLoginUserByLoginId(memberId);

        if (loginMember == null){
            return "redirect:/";
        }

        Page<MessageDTO> list = messageService.findMessageSenderByMemberId(memberId, pageable).map(message -> {
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setMessageId(message.getMessageId());
            messageDTO.setContent(message.getContent());
            messageDTO.setReceiverId(message.getReceiver().getMemberId());
            messageDTO.setTitle(message.getTitle());
            messageDTO.setSenderId(message.getSender().getMemberId());
            messageDTO.setDate(message.getDate());
            return messageDTO;
        });

        int nowPage = list.getPageable().getPageNumber()+1;
        int startPage = Math.max(nowPage-4,1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());

        model.addAttribute("messages", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "/messages/sentMessage";
//        return new Response("성공", "보낸 쪽지를 불러왔습니다.", messageService.sentMessage(memberEntity));
    }



    @Operation(summary = "보낸 쪽지 삭제하기", description = "보낸 쪽지를 삭제합니다.")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/messages/deleteSentMessage")
    public String deleteSentMessage(@SessionAttribute(name = "memberId", required = false) String memberId,
                                         @RequestParam(value = "selectedMessages", required = false) List<Long> selectedMessages,
                                    Model model) {
        // 임의로 유저 정보를 넣었지만, JWT 도입하고 현재 로그인 된 유저의 정보를 넘겨줘야함
//        MemberEntity memberEntity = memberRepository.findByMemberSequence(Long.valueOf(1)).orElseThrow(() -> {
//            return new IllegalArgumentException("유저를 찾을 수 없습니다.");
//        });
        MemberEntity loginMember = memberService.getLoginUserByLoginId(memberId);

        if (loginMember == null){
            return "redirect:/";
        }

        if (messageService.deleteMessageBySender(selectedMessages, loginMember).equals("success")){
            AlertDTO message = new AlertDTO("쪽지를 삭제했습니다.", "/messages/sent", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
        else if (messageService.deleteMessageBySender(selectedMessages, loginMember).equals("Empty Message")){
            AlertDTO message = new AlertDTO("삭제할 쪽지가 없습니다.", "/messages/sent", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
        else {
            AlertDTO message = new AlertDTO("유저 정보 오류.", "/messages/sent", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }

//        return new Response<>("삭제 성공", "보낸 쪽지인, " + id + "번 쪽지를 삭제했습니다.", messageService.deleteMessageBySender(id, memberEntity));
    }

}
