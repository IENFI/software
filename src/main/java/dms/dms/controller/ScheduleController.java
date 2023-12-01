package dms.dms.controller;

import dms.dms.domain.DateData;
import dms.dms.domain.Schedule;
import dms.dms.domain.ScheduleInfo;
import dms.dms.domain.Study;
import dms.dms.dto.AlertDTO;
import dms.dms.service.ScheduleService;
import dms.dms.service.StudyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final StudyService studyService;

    public ScheduleController(ScheduleService scheduleService, StudyService studyService) {
        this.scheduleService = scheduleService;
        this.studyService = studyService;
    }

    @GetMapping(value = "/schedule/plan")
    public String scheduleHome(@SessionAttribute(name = "memberId", required = false) String memberId, Model model) {

        if(memberId==null) {
            return "redirect:/";
        }

        // 일정 불러오기
        List<Schedule> schedules = scheduleService.findSchedulesByMemberID(memberId);
        List<Study> studyList = studyService.findStudiesByMemberID(memberId);
        model.addAttribute("scheduleList", schedules);
        model.addAttribute("studyList", studyList);
        model.addAttribute("memberId", memberId);

        return "/schedule/plan";
    }

    @PostMapping(value = "/schedule/scheduleCreates")
    public String insert(@SessionAttribute(name = "memberId", required = false) String memberId, ScheduleInfo scheduleInfo) {

        if(memberId==null) {
            return "redirect:/";
        }

        Schedule schedule = new Schedule();
        schedule.setMemberId(scheduleInfo.getMemberId());
        schedule.setTitle(scheduleInfo.getTitle());

        String date = scheduleInfo.getDate();
        System.out.println(date);

        schedule.setYear(scheduleInfo.getDate().substring(0,4));
        schedule.setMonth(scheduleInfo.getDate().substring(5,7));
        schedule.setDay(scheduleInfo.getDate().substring(8));

        scheduleService.saveSchedule(schedule);

        return "redirect:/schedule/plan?memberID="+schedule.getMemberId();

    }

    @PostMapping(value = "/scheduleDelete")
    public String scheduleDelete(@SessionAttribute(name = "memberId", required = false) String memberId, ScheduleInfo scheduleInfo, Model model) {

        if(memberId==null) {
            return "redirect:/";
        }

//        Schedule schedule = new Schedule();
//        schedule.setMemberId(scheduleInfo.getMemberId());
//        schedule.setId(scheduleInfo.getId());
//        schedule.setTitle(scheduleInfo.getTitle());

//        String date = scheduleInfo.getDate();
//
//        schedule.setYear(scheduleInfo.getDate().substring(0,4));
//        schedule.setMonth(scheduleInfo.getDate().substring(5,7));
//        schedule.setDay(scheduleInfo.getDate().substring(8));

        String deleteCheck = scheduleService.deleteSchedule(scheduleInfo.getId());
        if(deleteCheck == "success") {
            AlertDTO message = new AlertDTO("일정 삭제가 완료되었습니다.", "/schedule/plan", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
        else {
            AlertDTO message = new AlertDTO("일정 삭제에 실패했습니다.", "/schedule/plan", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
    }

    @PostMapping(value="/scheduleUpdate")
    public String update(@SessionAttribute(name = "memberId", required = false) String memberId, ScheduleInfo scheduleInfo, Model model) {

        if(memberId==null) {
            return "redirect:/";
        }

        Schedule schedule = new Schedule();
        schedule.setMemberId(scheduleInfo.getMemberId());
        schedule.setId(scheduleInfo.getId());
        schedule.setTitle(scheduleInfo.getTitle());

        schedule.setYear(scheduleInfo.getDate().substring(0, 4));
        schedule.setMonth(scheduleInfo.getDate().substring(5, 7));
        schedule.setDay(scheduleInfo.getDate().substring(8));

        String updateCheck = scheduleService.updateSchedule(schedule);
        if (updateCheck == "success") {
            AlertDTO message = new AlertDTO("일정 수정이 완료되었습니다.", "/schedule/plan", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        } else {
            AlertDTO message = new AlertDTO("일정 수정에 실패했습니다.", "/schedule/plan", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
    }

    private String showMessageAndRedirect(final AlertDTO params, Model model) {
        model.addAttribute("params", params);
        return "alert/alertRedirect";
    }

}
