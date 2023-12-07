package dms.service;

import dms.domain.Schedule;
import dms.repository.ScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public List<Schedule> findSchedulesByMemberID(String memberID) {
        return scheduleRepository.findSchedulesByMemberId(memberID);
    }

    public void saveSchedule(Schedule schedule) {
        scheduleRepository.save(schedule);
    }

    public String updateSchedule(Schedule schedule) {
        scheduleRepository.save(schedule);

        Optional<Schedule> updateSchedule = scheduleRepository.findById(schedule.getId());
        if(updateSchedule.isPresent()) {
            return "success";
        }
        else {
            return "fail";
        }
    }

    public Optional<Schedule> findOneSchedule(Long scheduleId) {
        return scheduleRepository.findById(scheduleId);
    }

    public String deleteSchedule(Long scheduleID) {
        scheduleRepository.deleteById(scheduleID);

        Optional<Schedule> schedule = scheduleRepository.findById(scheduleID);
        if(schedule.isPresent()) {
            return "fail";
        }
        else {
            return "success";
        }
    }

    public String deleteMemberSchedule(String memberID) {
        scheduleRepository.deleteByMemberId(memberID);
        List<Schedule> schedules = scheduleRepository.findSchedulesByMemberId(memberID);
        if(schedules != null) {
            return "fail";
        }
        else {
            return "success";
        }
    }

}
