package africa.learnspace.usermanagement.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserRegistrationRequest {
    @NotBlank(message = "Email address can not be blank")
    private String email;

    @NotBlank(message = "FirstName can not be blank")
    private String firstName;

    @NotBlank(message = "LastName can not be blank")
    private String lastName;

    private String role;

    private String instituteName;
}