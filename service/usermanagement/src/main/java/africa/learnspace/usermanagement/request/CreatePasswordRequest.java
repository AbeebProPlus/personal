package africa.learnspace.usermanagement.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreatePasswordRequest {
    @NotBlank(message = "Password can not be blank")
    private String password;
}
