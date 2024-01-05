package africa.learnspace.usermanagement.services;

import africa.learnspace.usermanagement.exception.LearnSpaceUserException;
import africa.learnspace.usermanagement.request.UserRegistrationRequest;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.token.TokenManager;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeyCloakServiceImpl implements KeyCloakUserService {

    private final Keycloak keycloak;

    private final ModelMapper mapper;

    @Value("${keycloak.realm}")
    private String KEYCLOAK_REALM;

    @Value("${keycloak.client}")
    private String CLIENT;

    @Value("${keycloak.server}")
    private String SERVER_URL;

    @Override
    public void createUser(UserRegistrationRequest userRegistrationRequest) {
        UserRepresentation userRepresentation = mapper.map(userRegistrationRequest,
                UserRepresentation.class);
        userRepresentation.setUsername(userRegistrationRequest.getEmail());
        try (Response response = keycloak
                .realm(KEYCLOAK_REALM)
                .users()
                .create(userRepresentation)) {
            if (response.getStatusInfo().equals(Response.Status.CONFLICT)) {
                throw new LearnSpaceUserException("User already exists");
            }
            assignRole(userRegistrationRequest);
        } catch (NotFoundException | LearnSpaceUserException exception) {
            throw new LearnSpaceUserException(exception.getMessage());
        }
    }

    private void assignRole(UserRegistrationRequest userRegistrationRequest) {
        String email = userRegistrationRequest.getEmail();
        String role = userRegistrationRequest.getRole();
        try {
            List<UserRepresentation> users = getUserRepresentations(email);
            if (users.isEmpty()) throw new LearnSpaceUserException("User does not exist");
            UserRepresentation user = users.get(0);

            RoleRepresentation roleRepresentation = keycloak
                    .realm(KEYCLOAK_REALM)
                    .roles()
                    .get(role.toUpperCase().trim())
                    .toRepresentation();

            if (roleRepresentation == null) throw new LearnSpaceUserException("Role not found: " + role.toUpperCase());

            UserResource userResource = keycloak
                    .realm(KEYCLOAK_REALM)
                    .users()
                    .get(user.getId());

            userResource.roles()
                    .realmLevel()
                    .add(List.of(roleRepresentation));
        } catch (NotFoundException exception) {
            throw new LearnSpaceUserException("Resource not found: " + exception.getMessage());
        }
    }

    @Override
    public void createClient(String instituteName) {
        ClientRepresentation clientRepresentation = new ClientRepresentation();
        clientRepresentation.setClientId(instituteName);
        clientRepresentation.setDirectAccessGrantsEnabled(true);
        clientRepresentation.setPublicClient(true);

        try (Response response = keycloak.realm(KEYCLOAK_REALM).clients().create(clientRepresentation)) {
            if (response.getStatusInfo().equals(Response.Status.CONFLICT)) {
                throw new LearnSpaceUserException("Client already exists");
            }
        } catch (NotFoundException | LearnSpaceUserException exception) {
            throw new LearnSpaceUserException(exception.getMessage());
        }
    }

    @Override
    public void createPassword(String email, String password) {
        List<UserRepresentation> users = getUserRepresentations(email);
        if (users.isEmpty()) throw new LearnSpaceUserException("User does not exist");
        UserRepresentation user = users.get(0);
        UserResource userResource = keycloak.realm(KEYCLOAK_REALM).users().get(user.getId());
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);
        userResource.resetPassword(credential);
        user.setEmailVerified(true);
        user.setEnabled(true);
        userResource.update(user);
    }

    public List<UserRepresentation> getUserRepresentations(String email) {
        return keycloak
                .realm(KEYCLOAK_REALM)
                .users()
                .search(email);
    }

    @Override
    public AccessTokenResponse login(String email, String password) {
        try (Keycloak keycloak = KeycloakBuilder.builder().realm(KEYCLOAK_REALM)
                .serverUrl(SERVER_URL)
                .username(email)
                .password(password)
                .clientId(CLIENT).build()) {
            TokenManager tokenManager = keycloak.tokenManager();
            return tokenManager.getAccessToken();
        } catch (NotAuthorizedException exception) {
            log.info("Login failed: {}", exception.getMessage());
            throw new LearnSpaceUserException(exception.getMessage());
        }
    }

    @Override
    public void deleteUser(String email) {
        UserRepresentation user = deactivateUser(email);
        try (Response response = keycloak
                .realm(KEYCLOAK_REALM)
                .users()
                .delete(user.getId())) {
            if (!response.getStatusInfo().equals(Response.Status.NO_CONTENT)) {
                log.info("User deletion failed. HTTP Response Code: " + response.getStatus());
            }
        }
    }

    @Override
    public UserRepresentation deactivateUser(String email) {
        List<UserRepresentation> users = getUserRepresentations(email);
        if (users.isEmpty()) throw new LearnSpaceUserException("User does not exist");
        UserRepresentation user = users.get(0);
        user.setEnabled(false);
        return user;
    }

    @Override
    public void addRolesToRealm() {
        List<String> roles = List.of("INSTITUTE_ADMIN", "INSTITUTE_EMPLOYEE", "SUPER_ADMIN", "TRAINEE", "FINANCE_MANAGER");
        RealmResource realmResource = keycloak.realm(KEYCLOAK_REALM);
        RolesResource rolesResource = realmResource.roles();

        for (String roleName : roles) {
            if (!roleExists(rolesResource, roleName)) {
                RoleRepresentation roleRepresentation = new RoleRepresentation();
                roleRepresentation.setName(roleName);
                rolesResource.create(roleRepresentation);
            }
        }
    }

    @Override
    public List<ClientRepresentation> getClients(String clientName) {
        return keycloak.realm(KEYCLOAK_REALM).clients().findByClientId(clientName);
    }

    private boolean roleExists(RolesResource rolesResource, String roleName) {
        List<RoleRepresentation> existingRoles = rolesResource.list();
        return existingRoles.stream().anyMatch(role -> roleName.equals(role.getName()));
    }
}