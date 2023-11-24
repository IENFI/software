package dms.dms.service;

import dms.dms.domain.Study;
import dms.dms.repository.StudyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
public class StudyService {

    private final StudyRepository studyRepository;

    public StudyService(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }

    public void saveStudy(Study study) {
        studyRepository.save(study);
    }

    public Page<Study> findStudiesByMemberID(String memberID, Pageable pageable) {
        return studyRepository.findStudiesByMemberId(memberID, pageable);
    }

    // user 식별해서 해당 user의 게시글만 보여주기 => findByUserIdStudies
    public Optional<Study> findOneStudy(Long studyId) {
        return studyRepository.findById(studyId);
    }

    public void deleteStudy(Long studyId) {
        studyRepository.deleteById(studyId);
    }


}
