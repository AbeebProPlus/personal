package africa.learnspace.web.service;

import africa.learnspace.user.manager.UserManager;
import africa.learnspace.user.model.User;
import africa.learnspace.usermanagement.request.UserRegistrationRequest;
import africa.learnspace.usermanagement.services.KeyCloakUserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AdminInitializer {

    private final KeyCloakUserService keyCloakUserService;

    private final UserManager userManager;

    private static UserRegistrationRequest getUserRegistrationRequest() {
        return UserRegistrationRequest.builder()
                .email("learnspace@learnspace.africa")
                .firstName("Learnspace")
                .lastName("Africa")
                .role("SUPER_ADMIN")
                .build();
    }

    @PostConstruct
    public void init() {
        UserRegistrationRequest request = getUserRegistrationRequest();
        saveUserToDatabase(request);
        saveUserToKeyCloak(request);
    }

    private void saveUserToKeyCloak(UserRegistrationRequest request) {
        List<UserRepresentation> users = keyCloakUserService.getUserRepresentations(request.getEmail());
        List<ClientRepresentation> clientRepresentations = keyCloakUserService.getClients("learnspace");

        if (clientRepresentations.isEmpty()) {
            keyCloakUserService.createClient("learnspace");
            keyCloakUserService.addRolesToRealm();

            if (users.isEmpty()) {
                keyCloakUserService.createUser(request);
                keyCloakUserService.createPassword("learnspace@learnspace.africa", "Learnspace@2021");
            }
        }
    }

    private void saveUserToDatabase(UserRegistrationRequest request) {
        if (userManager.findAll().isEmpty()) {
            User user = new User();
            user.setEmail(request.getEmail());
            user.setVerified(true);
            user.setDisabled(false);
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            userManager.saveUser(user);
        }
    }
}
