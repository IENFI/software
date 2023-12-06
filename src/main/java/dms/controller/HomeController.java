package dms.controller;


import dms.domain.MemberEntity;
import dms.domain.MemberRole;
import dms.domain.Schedule;
import dms.dto.MemberDTO;
import dms.service.MemberService;
import dms.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Controller
@RequiredArgsConstructor // MemberService에 대한 멤버를 사용 가능
public class HomeController {
    private final MemberService memberService;
    private final ScheduleService scheduleService;

    @RequestMapping
    public String home(Model model, @SessionAttribute(name = "memberId", required = false) String memberId){
        model.addAttribute("memberDTO", new MemberDTO());
        model.addAttribute("user", MemberRole.USER);

        MemberEntity loginMember = memberService.getLoginUserByLoginId(memberId);
        model.addAttribute("login_error", false);

        if(loginMember != null) {
            model.addAttribute("home_button", true);
            model.addAttribute("role",loginMember.getMemberRole());
            System.out.println("home_button : true");
            System.out.println("loginMember : "+ loginMember);
        }
        else {
            model.addAttribute("home_button", false);
            System.out.println("home_button : false");
        }

        System.out.println("login_error : false");

        // 일정 관련
        List<Schedule> schedules = scheduleService.findSchedulesByMemberID(memberId);
        model.addAttribute("scheduleList", schedules);

        return "home";
    }
}
