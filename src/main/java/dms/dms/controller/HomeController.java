package dms.dms.controller;


import dms.dms.domain.MemberEntity;
import dms.dms.domain.Schedule;
import dms.dms.dto.MemberDTO;
import dms.dms.service.MemberService;
import dms.dms.service.ScheduleService;
import dms.dms.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Controller
@RequiredArgsConstructor // MemberService에 대한 멤버를 사용 가능
public class HomeController {
    private final MemberService memberService;
    private final ScheduleService scheduleService;

    @GetMapping(value = "/home")
    public String home() {
        return "home";
    }

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

        // 일정 관련
        List<Schedule> schedules = scheduleService.findSchedulesByMemberID(memberId);
        model.addAttribute("scheduleList", schedules);

        return "home";
    }
}
