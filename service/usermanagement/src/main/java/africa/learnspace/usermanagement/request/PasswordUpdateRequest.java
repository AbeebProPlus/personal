package africa.learnspace.usermanagement.request;

import lombok.Data;

@Data
public class PasswordUpdateRequest {

    private String currentPassword;

    private String newPassword;
}
