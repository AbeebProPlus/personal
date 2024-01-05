package africa.learnspace.usermanagement.services;

import africa.learnspace.usermanagement.request.UserRegistrationRequest;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public interface KeyCloakUserService {

    void createUser(UserRegistrationRequest userRegistrationRequest);


    void createPassword(String email, String password);

    AccessTokenResponse login(String email, String password);

    void deleteUser(String email);

    UserRepresentation deactivateUser (String email);
    void createClient(String instituteName);
    List<UserRepresentation> getUserRepresentations(String email);

    void addRolesToRealm();

    List<ClientRepresentation> getClients(String clientName);
}