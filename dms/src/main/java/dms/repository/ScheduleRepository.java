package dms.repository;

import dms.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findSchedulesByMemberId(String memberID);

    void deleteByMemberId(String memberID);
}
