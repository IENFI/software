package dms.repository;

import dms.domain.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyRepository extends JpaRepository<Study, Long> {

    Page<Study> findStudiesByMemberId(String memberID, Pageable pageable);
    List<Study> findStudiesByMemberId(String memberID);

    void deleteStudiesByMemberId(String memberID);

}
