package dms.dms.controller;

import dms.dms.domain.MemberEntity;
import dms.dms.domain.MemberRole;
import dms.dms.dto.MemberDTO;
import dms.dms.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor // MemberService에 대한 멤버를 사용 가능
public class MemberController {

    // 생성자 주입
    private final MemberService memberService;

    // 회원가입 페이지 출력 요청
    @GetMapping("/member/save")
    public String saveForm(Model model) {
        model.addAttribute("loginType", "dms");
        model.addAttribute("pageName", "세션 로그인");

        model.addAttribute("memberDTO", new MemberDTO());
        return "/member/save";
    }

    @PostMapping("/member/save")    // name값을 requestparam에 담아온다
    public String save(@Valid @ModelAttribute MemberDTO memberDTO, BindingResult bindingResult, Model model) {
        model.addAttribute("loginType", "dms");
        model.addAttribute("pageName", "세션 로그인");

        System.out.println("MemberController.save");
        System.out.println("memberDTO = " + memberDTO);

        // password와 passwordCheck가 같은지 체크
        if (!memberDTO.getMemberPassword().equals(memberDTO.getMemberPasswordCheck())){
            bindingResult.addError(new FieldError("memberDTO", "memberPasswordCheck", "비밀번호가 일치하지 않습니다."));
        }

        if (bindingResult.hasErrors()){
            return "/member/save";
        }

        memberService.save(memberDTO);

        return "redirect:/";
    }

    @GetMapping("/member/delete")
    public String userDeletePage(@SessionAttribute(name = "memberId", required = false) String memberId, Model model) {
        model.addAttribute("loginType", "dms");
        model.addAttribute("pageName", "세션 로그인");

        model.addAttribute("deleteError", "none");

        MemberEntity memberEntity = memberService.getMember(memberId);
        model.addAttribute("memberName", memberEntity.getMemberName());
        return "/member/delete";
    }

    @PostMapping("/member/delete")
    public String userDelete(@RequestParam("memberPassword") String memberPassword, @SessionAttribute(name = "memberId", required = false) String memberId, Model model) {
        model.addAttribute("loginType", "dms");
        model.addAttribute("pageName", "세션 로그인");
        MemberEntity memberEntity = memberService.getMember(memberId);
        model.addAttribute("memberName", memberEntity.getMemberName());

        Boolean deleteSuccess = memberService.delete(memberId, memberPassword);
        if (deleteSuccess) {
            System.out.println("MemberController.탈퇴 성공");
            model.addAttribute("deleteError", "false");
            model.addAttribute("nextUrl", "/member/logout");
            return "redirect:/member/logout";
        } else {
            System.out.println("MemberController.탈퇴 실패");
            System.out.println("deleteError : true");
            model.addAttribute("deleteError", "true");
            model.addAttribute("nextUrl", "/member/delete");
            return "/member/delete";
        }
    }

    @GetMapping("/member/login")
    public String loginPage(Model model){
        model.addAttribute("loginType", "dms");
        model.addAttribute("pageName", "세션 로그인");

        model.addAttribute("memberDTO", new MemberDTO());
        return "home";
    }

//    @GetMapping("/check-login")
//    @ResponseBody
//    public String checkForLogin(@ModelAttribute MemberDTO memberDTO) {
//        MemberEntity memberEntity = memberService.login(memberDTO);
//        if (memberEntity == null){
//            return "FALSE";
//        }else {
//            return "TRUE";
//        }
//    }

    @PostMapping("/member/login")
    public String login(@ModelAttribute MemberDTO memberDTO, BindingResult bindingResult, HttpServletRequest httpServletRequest, Model model){
        model.addAttribute("loginType", "dms");
        model.addAttribute("pageName", "세션 로그인");

        MemberEntity memberEntity = memberService.login(memberDTO);

        System.out.println("memberController.login");
        System.out.println("memberDTO = "+memberDTO);

        if (memberEntity == null){
            bindingResult.reject("loginFail", "로그인 아이디 또는 비밀번호가 틀렸습니다.");
            System.out.println("login_error : true");
            System.out.println("member_empty : false");
        }

        System.out.println(bindingResult.hasErrors());

        model.addAttribute("login_error", bindingResult.hasErrors());
        model.addAttribute("member_empty", false);

        if (bindingResult.hasErrors()){
            return "home";
        }

        System.out.println("login_error : false");
        System.out.println("member_empty : false");

        //로그인 성공 하면 세션을 생성
        //세션 생성 전 기존의 세션 파기
        httpServletRequest.getSession().invalidate();
        HttpSession session = httpServletRequest.getSession(true); // session이 없으면 생성

        // 세션에 userId를 넣어줌
        session.setAttribute("memberId", memberDTO.getMemberId());
        session.setMaxInactiveInterval(1800); // session이 30분동안 유지

//        MemberDTO loginResult = memberService.login(memberDTO);
//        if (loginResult != null) {
//            // login 성공
//            session.setAttribute("loginId", loginResult.getMemberId());
//            return "redirect:/main";
//        } else {
//            // login 실패
//            return "redirect:/";
//        }
        return "redirect:/";
    }


    @GetMapping("/check-emailDuplicate")
    @ResponseBody
    public String checkForEmailDuplicate(@RequestParam("memberEmail") String memberEmail) {
        boolean isDuplicate = memberService.checkMemberEmailDuplicate(memberEmail);

        if (memberEmail == ""){
            return "EMPTY";
        }
        else if (isDuplicate) {
            return "DUPLICATE";
        } else {
            return "OK";
        }
    }

    @GetMapping("/check-idDuplicate")
    @ResponseBody
    public String checkForDuplicate(@RequestParam("memberId") String memberId) {
        boolean isDuplicate = memberService.checkMemberIdDuplicate(memberId);

        if (memberId == ""){
            return "EMPTY";
        }
        else if (isDuplicate) {
            return "DUPLICATE";
        } else {
            return "OK";
        }
    }


    @GetMapping("/member/logout")
    public String logout(HttpServletRequest request, Model model){
        model.addAttribute("loginType", "dms");
        model.addAttribute("pageName", "세션 로그인");

        System.out.println("memberController.logout");

        HttpSession session = request.getSession(false); // session이 없으면 null return
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

    @GetMapping("member/info")
    public String userInfo(@SessionAttribute(name = "memberId", required = false) String memberId, Model model){
        model.addAttribute("loginType", "dms");
        model.addAttribute("pageName","세션 로그인");

        MemberEntity loginMember = memberService.getLoginUserByLoginId(memberId);

        if (loginMember == null){
            return "redirect:/login";
        }

        model.addAttribute("member", loginMember);
        return "/member/info";
    }

    @GetMapping("/admin")
    public  String adminPage(@SessionAttribute(name = "memberId", required = false) String memberId, Model model){
        model.addAttribute("loginType", "dms");
        model.addAttribute("pageName","세션 로그인");

        MemberEntity loginMember = memberService.getLoginUserByLoginId(memberId);

        if (loginMember == null){
            return "redirect:/";
        }

        if (!loginMember.getMemberRole().equals(MemberRole.ADMIN)){
            return "redirect:/";
        }

        return "/member/admin";
    }


//    @GetMapping("/main")
//    public String main(Model model, @SessionAttribute(name = "memberId", required = false) String memberId) {
//        model.addAttribute("loginType", "session-login");
//        model.addAttribute("pageName", "세션 로그인");
//
//        MemberEntity loginMember = memberService.getLoginUserByLoginId(memberId);
//
//        if (loginMember != null){
//            model.addAttribute("Name", loginMember.getMemberName());
//        }
//
////        return "main";
//        return "home";
//    }
}