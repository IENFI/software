package dms.dms.repository;

import dms.dms.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    Optional<Schedule> findSchedulesByMemberID(String memberID);

}
