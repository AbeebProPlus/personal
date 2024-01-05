package africa.learnspace.usermanagement.services;

import africa.learnspace.usermanagement.request.PasswordUpdateRequest;
import africa.learnspace.usermanagement.request.UserRegistrationRequest;

public interface UserService {

    String inviteColleague(UserRegistrationRequest request);

    String changePassword(PasswordUpdateRequest request, String email);
}
