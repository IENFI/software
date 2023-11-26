package dms.dms.controller;

import dms.dms.domain.DateData;
import dms.dms.domain.Schedule;
import dms.dms.domain.ScheduleInfo;
import dms.dms.domain.Study;
import dms.dms.service.ScheduleService;
import dms.dms.service.StudyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

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
        // 일정 불러오기
        System.out.println("memberId : "+memberId);

        List<Schedule> schedules = scheduleService.findSchedulesByMemberID(memberId);
        List<Study> studyList = studyService.findStudiesByMemberID(memberId);
        model.addAttribute("scheduleList", schedules);
        model.addAttribute("studyList", studyList);
        model.addAttribute("memberId", memberId);

        return "/schedule/plan";
    }
//
//    @GetMapping(value = "/schedule/scheduleCreate")
//    public String scheduleInsert(@RequestParam String memberID, Model model) {
//        model.addAttribute("memberID", memberID);
//        return "/schedule/scheduleCreate";
//    }

//    @GetMapping(value="/schedule/ww")
//    public String schedule() {
//        return "/schedule/ww";
//    }

    @PostMapping(value = "/schedule/scheduleCreates")
    public String insert(ScheduleInfo scheduleInfo) {

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

//    @GetMapping(value = "/schedule/scheduleHome")
//    public String calendar(@RequestParam String memberID, Model model, HttpServletRequest request, DateData dateData) {
//        // 일정 불러오기
//        List<Schedule> schedules = scheduleService.findSchedulesByMemberID(memberID);
//        model.addAttribute("scheduleList", schedules);
//
//        // 달력 구현
//        Calendar calendar = Calendar.getInstance();
//        DateData calendarData;
//        System.out.println(calendar.get(Calendar.DATE));
//        dateData = new DateData(String.valueOf(calendar.get(Calendar.YEAR)),
//                    String.valueOf(calendar.get(Calendar.MONTH)),
//                    String.valueOf(calendar.get(Calendar.DATE)), null);
//
//        Map<String, Integer> todayInfo = todayInfo(dateData);
//        List<DateData> dateList = new ArrayList<DateData>();
//
//        for(int i=1; i<todayInfo.get("start"); i++) {
//            calendarData = new DateData(null, null, null, null);
//            dateList.add(calendarData);
//        }
//
//        for(int i=todayInfo.get("startDay"); i<=todayInfo.get("endDay"); i++) {
//            if(i==todayInfo.get("today")) {
//                calendarData = new DateData(String.valueOf(dateData.getYear()),
//                        String.valueOf(dateData.getMonth()),
//                        String.valueOf(i), "today");
//            }
//            else {
//                calendarData = new DateData(String.valueOf(dateData.getYear()),
//                        String.valueOf(dateData.getMonth()),
//                        String.valueOf(i), "normal_date");
//            }
//            dateList.add(calendarData);
//        }
//
//        int index = 7-dateList.size()%7;
//
//        if(dateList.size()%7 != 0) {
//            for(int i=0; i<index; i++) {
//                calendarData = new DateData(null, null, null, null);
//                dateList.add(calendarData);
//            }
//        }
//
//        System.out.println("dayList : " + dateList);
//
//        model.addAttribute("dateList", dateList);
//        model.addAttribute("today", calendar.get(Calendar.DATE));
//        model.addAttribute("todayInfo", todayInfo);
//
//        return "/schedule/scheduleHome";
//
//    }

//    public Map<String, Integer> todayInfo(DateData dateData) { // 캘린터 함수
//        Map<String, Integer> todayData = new HashMap<String, Integer>();
//
//        Calendar cal = Calendar.getInstance();
//        cal.set(Integer.parseInt(dateData.getYear()), Integer.parseInt(dateData.getMonth()), 1);
//
//        int startDay = cal.getMinimum(Calendar.DATE);
//        int endDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
//        int start = cal.get(Calendar.DAY_OF_WEEK);
//
//        Calendar todayCal = Calendar.getInstance();
//        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
//        SimpleDateFormat monthFormat = new SimpleDateFormat("M");
//
//        int todayYear = Integer.parseInt(yearFormat.format(todayCal.getTime()));
//        int todayMonth = Integer.parseInt(monthFormat.format(todayCal.getTime()));
//
//        int searchYear = Integer.parseInt(dateData.getYear());
//        int searchMonth = Integer.parseInt(dateData.getMonth()) + 1;
//
//        int today = -1;
//        if (todayYear == searchYear && todayMonth == searchMonth) {
//            SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
//            today = Integer.parseInt(dateFormat.format(todayCal.getTime()));
//        }
//
//        searchMonth = searchMonth - 1;
//
//        Map<String, Integer> beforeAfterCalendar = beforeAfterCalendar(searchYear, searchMonth);
//
//        System.out.println("search_Month : " + searchMonth);
//
//        todayData.put("start", start);
//        todayData.put("startDay", startDay);
//        todayData.put("endDay", endDay);
//        todayData.put("today", today);
//        todayData.put("search_year", searchYear);
//        todayData.put("search_month", searchMonth);
//        todayData.put("before_year", beforeAfterCalendar.get("before_year"));
//        todayData.put("before_month", beforeAfterCalendar.get("before_month"));
//        todayData.put("after_year", beforeAfterCalendar.get("after_year"));
//        todayData.put("after_month", beforeAfterCalendar.get("after_month"));
//
//        return todayData;
//    }
//
//    private Map<String, Integer> beforeAfterCalendar(int searchYear, int searchMonth) {
//
//        Map<String, Integer> beforeAfterData = new HashMap<String, Integer>();
//
//        int before_year = searchYear;
//        int before_month = searchMonth-1;
//        int after_year = searchYear;
//        int after_month = searchMonth+1;
//
//        if(before_month<0) {
//            before_month = 11;
//            before_year = searchYear-1;
//        }
//
//        if(after_month>11) {
//            after_month = 0;
//            after_year = searchYear+1;
//        }
//
//        beforeAfterData.put("before_year", before_year);
//        beforeAfterData.put("before_month", before_month);
//        beforeAfterData.put("after_year", after_year);
//        beforeAfterData.put("after_month", after_month);
//
//        return beforeAfterData;
//
//    }

    @GetMapping(value = "/schedule/scheduleContent")
    public String scheduleContent(@RequestParam("id") Long id, Model model) {
        Schedule schedule = scheduleService.findOneSchedule(id)
                .orElseThrow(NullPointerException::new);
        model.addAttribute("scheduleID", schedule.getId());
        model.addAttribute("scheduleTitle", schedule.getTitle());
        model.addAttribute("scheduleMemberID", schedule.getMemberId());

        String year = schedule.getYear();
        String month = schedule.getMonth();
        String day = schedule.getDay();

        String date = year+"-"+month+"-"+day;
        model.addAttribute("scheduleDate", date);

        return "/schedule/scheduleContent";
    }

    @PostMapping(value = "/scheduleDelete")
    public String scheduleDelete(ScheduleInfo scheduleInfo) {
        Schedule schedule = new Schedule();
        schedule.setMemberId(scheduleInfo.getMemberId());
        schedule.setId(scheduleInfo.getId());
        schedule.setTitle(scheduleInfo.getTitle());

        String date = scheduleInfo.getDate();

        schedule.setYear(scheduleInfo.getDate().substring(0,4));
        schedule.setMonth(scheduleInfo.getDate().substring(5,7));
        schedule.setDay(scheduleInfo.getDate().substring(8));

        scheduleService.deleteSchedule(schedule.getId());

        return "redirect:/schedule/plan";
    }

//    @GetMapping(value = "/schedule/scheduleUpdate")
//    public String scheduleUpdate(@RequestParam("id") Long id, Model model) {
//        Schedule schedule = scheduleService.findOneSchedule(id)
//                .orElseThrow(NullPointerException::new);
//        model.addAttribute("scheduleMemberID", schedule.getMemberId());
//        model.addAttribute("scheduleId", schedule.getId());
//        model.addAttribute("scheduleTitle", schedule.getTitle());
//        model.addAttribute("scheduleYear", schedule.getYear());
//        model.addAttribute("scheduleMonth", schedule.getMonth());
//        model.addAttribute("scheduleDay", schedule.getDay());
//        return "/schedule/scheduleUpdate";
//    }

    @PostMapping(value="/scheduleUpdate")
    public String update(ScheduleInfo scheduleInfo) {
        Schedule schedule = new Schedule();
        schedule.setMemberId(scheduleInfo.getMemberId());
        schedule.setId(scheduleInfo.getId());
        schedule.setTitle(scheduleInfo.getTitle());

        String date = scheduleInfo.getDate();

        schedule.setYear(scheduleInfo.getDate().substring(0,4));
        schedule.setMonth(scheduleInfo.getDate().substring(5,7));
        schedule.setDay(scheduleInfo.getDate().substring(8));

        scheduleService.saveSchedule(schedule);

        return "redirect:/schedule/plan";
    }



}
