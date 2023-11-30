package dms.dms.controller;

import dms.dms.domain.MemberEntity;
import dms.dms.domain.MemberRole;
import dms.dms.dto.AlertDTO;
import dms.dms.dto.MemberDTO;
import dms.dms.service.MemberService;
import dms.dms.service.ScheduleService;
import dms.dms.service.StudyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.HTML;
import java.lang.reflect.Member;

@Controller
@RequiredArgsConstructor // MemberService에 대한 멤버를 사용 가능
public class MemberController {

    // 생성자 주입
    private final MemberService memberService;
    private final StudyService studyService;
    private final ScheduleService scheduleService;

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
        if (memberId == null){
            return "redirect:/";
        }

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
//            model.addAttribute("deleteError", "false");
//            model.addAttribute("nextUrl", "/member/logout");

            // 해당 멤버의 공부기록, 일정 삭제
            studyService.deleteMemberStudy(memberId);
            scheduleService.deleteMemberSchedule(memberId);

            AlertDTO message = new AlertDTO("회원 탈퇴 성공했습니다.", "/member/logout", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);

//            return "redirect:/member/logout";
        } else {
            System.out.println("MemberController.탈퇴 실패");
//            System.out.println("deleteError : true");
//            model.addAttribute("deleteError", "true");
//            model.addAttribute("nextUrl", "/member/delete");

            AlertDTO message = new AlertDTO("비밀번호가 틀렸습니다.", "/member/delete", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);

//            return "/member/delete";
        }
    }

    @GetMapping("/member/login")
    public String loginPage(Model model){
        model.addAttribute("loginType", "dms");
        model.addAttribute("pageName", "세션 로그인");

        model.addAttribute("memberDTO", new MemberDTO());
        return "redirect:/";
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
            return "redirect:/";
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
    public String logout(@SessionAttribute(name = "memberId", required = false) String memberId, HttpServletRequest request, Model model){
        model.addAttribute("loginType", "dms");
        model.addAttribute("pageName", "세션 로그인");

        MemberEntity loginMember = memberService.getLoginUserByLoginId(memberId);

        if (loginMember == null){
            return "redirect:/";
        }

        System.out.println("memberController.logout");

        HttpSession session = request.getSession(false); // session이 없으면 null return
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

    @GetMapping("/member/info")
    public String memberInfo(@SessionAttribute(name = "memberId", required = false) String memberId,
                             Model model){
        model.addAttribute("loginType", "dms");
        model.addAttribute("pageName","세션 로그인");

        MemberEntity loginMember = memberService.getLoginUserByLoginId(memberId);

        if (loginMember == null){
            return "redirect:/";
        }

        model.addAttribute("member", loginMember);
        return "member/info";
    }

    // 사용자에게 메시지를 전달하고, 페이지를 리다이렉트 한다.
    private String showMessageAndRedirect(final AlertDTO params, Model model) {
        model.addAttribute("params", params);
        return "alert/alertRedirect";
    }
    // 출처: https://congsong.tistory.com/22 [Let's develop:티스토리]

    @PostMapping("/member/editEmail")
    public String editMemberEmail (@SessionAttribute(name = "memberId", required = false) String memberId,
                            @RequestParam("memberEmail") String memberEmail, Model model)
    {
        System.out.println("MemberController.editEmail");

        String editToken = memberService.editMemberEmail(memberId, memberEmail);
        if (editToken == "success"){
            AlertDTO message = new AlertDTO("이메일 수정이 완료되었습니다.", "/member/info", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
        else if (editToken == "equal"){
            AlertDTO message = new AlertDTO("새로고침 오류", "/member/info", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
        else {
            AlertDTO message = new AlertDTO("이메일 수정을 실패하였습니다.", "/member/info", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
    }

    @PostMapping("/member/editDept")
    public String editMemberInterest (@SessionAttribute(name = "memberId", required = false) String memberId,
                            @RequestParam("memberDept") String memberDept, Model model)
    {
        System.out.println("MemberController.editDept");

        String editToken = memberService.editMemberDept(memberId, memberDept);

        if (editToken == "success"){
            AlertDTO message = new AlertDTO("학적정보 수정이 완료되었습니다.", "/member/info", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
        else if (editToken == "equal"){
            AlertDTO message = new AlertDTO("새로고침 오류", "/member/info", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
        else {
            AlertDTO message = new AlertDTO("학적정보 수정을 실패하였습니다.", "/member/info", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
    }

    @PostMapping("/member/editInterest")
    public String editMemberDept (@SessionAttribute(name = "memberId", required = false) String memberId,
                                  @RequestParam("memberInterest") String memberInterest, Model model)
    {
        System.out.println("MemberController.editInterest");

        String editToken = memberService.editMemberInterest(memberId, memberInterest);

        if (editToken == "success"){
            AlertDTO message = new AlertDTO("관심분야 수정이 완료되었습니다.", "/member/info", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
        else if (editToken == "equal"){
            AlertDTO message = new AlertDTO("새로고침 오류", "/member/info", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
        else {
            AlertDTO message = new AlertDTO("관심분야 수정을 실패하였습니다.", "/member/info", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
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

    @GetMapping("/member/findIDPW")
    public String findIDPW() {
        return "/member/findIDPW";
    }

    @PostMapping("/member/findID")
    public String findID(MemberEntity memberInfo, Model model) {
        String memberCheck = memberService.findMemberID(memberInfo.getMemberName(), memberInfo.getMemberEmail());

        if(memberCheck == "incorrect") {
            AlertDTO message = new AlertDTO("해당 사용자를 찾을 수 없습니다. 아이디, 이메일을 다시 확인해주세요.", "/member/findIDPW", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
        else {
            AlertDTO message = new AlertDTO("아이디는 '"+memberCheck+"' 입니다.", "/", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }

    }

    @GetMapping("/member/findPW")
    @ResponseBody
    public String findPW(@RequestParam("memberID") String memberID,
                       @RequestParam("memberName") String memberName,
                       @RequestParam("memberEmail") String memberEmail,
                       @RequestParam("check") String check) {
        String memberID1 = memberService.findMemberID(memberName, memberEmail);
        if(memberID1 != null && memberID != null) {
            if(memberID1.equals(memberID) && check.equals("true")) {
                return "correct";
            }
            else {
                return "incorrect";
            }
        }
        else {
            return "null";
        }

    }

    @PostMapping(value = "/member/changePW")
    public String changePW(String memberID, String memberPW) {

        MemberEntity member = memberService.getMember(memberID);

        member.setMemberPassword(memberPW);
        System.out.println(memberPW);
        memberService.changePW(member);

        return "redirect:/";
    }


}