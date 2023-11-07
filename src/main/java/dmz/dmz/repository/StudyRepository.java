package dmz.dmz.repository;

import dmz.dmz.domain.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface StudyRepository extends JpaRepository<Study, Long> {


}
