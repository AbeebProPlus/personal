package africa.learnspace.notification.data.request;

import lombok.*;
import org.thymeleaf.context.Context;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailRequest {

    private String email;

    private String subject;

    private Context context;

    private String firstName;

    private String template;
}