package dms.dms;

import dms.dms.repository.StudyRepository;
import dms.dms.service.StudyService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    private final StudyRepository studyRepository;

    public SpringConfig(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }

    @Bean
    public StudyService studyService() {
        return new StudyService(studyRepository);
    }

}
