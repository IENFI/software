package dms.dms.controller;

import dms.dms.dto.AlertDTO;
import dms.dms.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @GetMapping("/emailSend")
    @ResponseBody
    public int mailSend(@RequestParam("memberEmail") String memberEmail, Model model) throws Exception {
        int number = mailService.sendMail(memberEmail);
        System.out.println("mail 인증 코드 : "+number);
        System.out.println("멤버 메일 : "+memberEmail);

        return number;
    }

    private String showMessageAndRedirect(final AlertDTO params, Model model) {
        model.addAttribute("params", params);
        return "alert/alertRedirect";
    }

    @GetMapping("/emailSendComplete")
    public String find() {
        return "/member/findIDPW";
    }

}
