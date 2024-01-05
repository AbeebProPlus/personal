package africa.learnspace.notification.data.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.thymeleaf.context.Context;

@Setter
@Getter
public class SendEmailRequest {
    @NotBlank(message = "Sender email address can not be blank")
    @Email(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
    private String from;

    @NotBlank(message = "Email content can not be blank")
    private String htmlContent;

    @NotBlank(message = "Email subject can not be blank")
    private String subject;

    @NotBlank(message = "Receiver Email address can not be blank")
    @Email(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
    private String to;

    private Context context;
}