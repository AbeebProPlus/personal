package africa.learnspace.usermanagement.services;

import africa.learnspace.usermanagement.request.UserRegistrationRequest;
import org.keycloak.representations.AccessTokenResponse;

public interface AuthService {
    void createUser(UserRegistrationRequest userRegistrationRequest);

    void createPassword(String token, String password);

    AccessTokenResponse login(String email, String password);

    void forgetPassword(String email);
}