package dms.dms.service;

import dms.dms.domain.Schedule;
import dms.dms.repository.ScheduleRepository;
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

    public List<Schedule> findByUserSchedules(Schedule schedule) {
        return scheduleRepository.findAll();
    }

    public void saveSchedule(Schedule schedule) {
        scheduleRepository.save(schedule);
    }

    public Optional<Schedule> findOneSchedule(Long scheduleId) {
        return scheduleRepository.findById(scheduleId);
    }

    public void deleteSchedule(Long scheduleID) {
        scheduleRepository.deleteById(scheduleID);
    }

}
