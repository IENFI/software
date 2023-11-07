package dmz.dmz.service;

import dmz.dmz.domain.Study;
import dmz.dmz.repository.StudyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public class StudyService {

    private final StudyRepository studyRepository;

    public StudyService(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }

    public void saveStudy(Study study) {
        studyRepository.save(study);
    }

    public Page<Study> findByUserIdStudies(Pageable pageable) {
        return studyRepository.findAll(pageable);
    }
    // user 식별해서 해당 user의 게시글만 보여주기 => findByUserIdStudies
    public Optional<Study> findOneStudy(Long studyId) {
        return studyRepository.findById(studyId);
    }

    public void deleteStudy(Long studyId) {
        studyRepository.deleteById(studyId);
    }



}
