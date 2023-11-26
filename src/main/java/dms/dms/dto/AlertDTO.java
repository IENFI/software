package dms.dms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Getter
@AllArgsConstructor
public class AlertDTO {
    private String alertMessage;
    private String redirectURL;
    private RequestMethod method;
    private Map<String , Object> data;
}
