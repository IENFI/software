package dms.dms.repository;

import dms.dms.domain.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface StudyRepository extends JpaRepository<Study, Long> {

    Page<Study> findStudiesByMemberId(String memberID, Pageable pageable);
    List<Study> findStudiesByMemberId(String memberID);

    void deleteStudiesByMemberId(String memberID);

}
