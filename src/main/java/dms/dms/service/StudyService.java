package dms.dms.service;

import dms.dms.domain.Schedule;
import dms.dms.domain.Study;
import dms.dms.repository.StudyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    public String updateStudy(Study study) {
        studyRepository.save(study);

        Optional<Study> updateStudy = studyRepository.findById(study.getId());
        if(updateStudy.isPresent()) {
            return "success";
        }
        else {
            return "fail";
        }
    }

    public Page<Study> findStudiesByMemberID(String memberID, Pageable pageable) {
        return studyRepository.findStudiesByMemberId(memberID, pageable);
    }

    public List<Study> findStudiesByMemberID(String memberID) {
        return studyRepository.findStudiesByMemberId(memberID);
    }

    // user 식별해서 해당 user의 게시글만 보여주기 => findByUserIdStudies
    public Optional<Study> findOneStudy(Long studyId) {
        return studyRepository.findById(studyId);
    }

    public String deleteStudy(Long studyId) {
        studyRepository.deleteById(studyId);

        Optional<Study> study = studyRepository.findById(studyId);
        if(study.isPresent()) {
            return "fail";
        }
        else {
            return "success";
        }
    }

    public String deleteMemberStudy(String memberID) {
        studyRepository.deleteStudiesByMemberId(memberID);
        List<Study> study = studyRepository.findStudiesByMemberId(memberID);
        if(study != null) {
            return "fail";
        }
        else {
            return "success";
        }
    }
}
