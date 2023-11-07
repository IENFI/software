package dmz.dmz;

import dmz.dmz.repository.StudyRepository;
import dmz.dmz.service.StudyService;
import jakarta.persistence.EntityManager;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

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
