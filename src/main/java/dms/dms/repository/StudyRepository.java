package dms.dms.repository;

import dms.dms.domain.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface StudyRepository extends JpaRepository<Study, Long> {

    Page<Study> findStudiesByMemberID(String memberID, Pageable pageable);

}
