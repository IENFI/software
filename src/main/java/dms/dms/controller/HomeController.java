package dms.dms.controller;


import dms.dms.domain.MemberEntity;
import dms.dms.dto.MemberDTO;
import dms.dms.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequiredArgsConstructor // MemberService에 대한 멤버를 사용 가능
public class HomeController {
    private final MemberService memberService;

    @RequestMapping
    public String dms(Model model, @SessionAttribute(name = "memberId", required = false) String memberId) {
        model.addAttribute("loginType", "dms");
        model.addAttribute("pageName", "세션 로그인");
        model.addAttribute("memberDTO", new MemberDTO());

        MemberEntity loginMember = memberService.getLoginUserByLoginId(memberId);
        model.addAttribute("login_error", false);

        if(loginMember != null) {
            model.addAttribute("home_button", true);
            System.out.println("home_button : true");
            System.out.println("loginMember : "+ loginMember);
        }
        else{
            model.addAttribute("home_button", false);
            System.out.println("home_button : false");
        }

        System.out.println("login_error : false");

        return "/login";
    }
}
