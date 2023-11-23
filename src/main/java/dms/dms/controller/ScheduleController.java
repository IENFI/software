package dms.dms.controller;

import dms.dms.domain.Schedule;
import dms.dms.service.ScheduleService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Optional;

@Controller
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping(value = "/schedule/plan")
    public String scheduleListByMemberID(@RequestParam String memberID, Model model) {
        Optional<Schedule> schedules = scheduleService.findSchedulesByMemberID(memberID);

        model.addAttribute("schedules", schedules);
        model.addAttribute("memberID", memberID);

        return "/schedule/plan";
    }

    @GetMapping(value = "/schedule/scheduleCreate")
    public String scheduleInsert(@RequestParam String memberID, Model model) {
        model.addAttribute("memberID", memberID);
        return "/schedule/scheduleCreate";
    }

    @PostMapping(value = "/schedule/scheduleCreates")
    public String insert(@RequestParam String memberID, Schedule scheduleInfo) {

        Schedule schedule = new Schedule();
        schedule.setMemberID(memberID);
        schedule.setTitle(scheduleInfo.getTitle());
        schedule.setDate(scheduleInfo.getDate());

        scheduleService.saveSchedule(schedule);

        return "redirect:/schedule/plan?memberID="+memberID;

    }
}
