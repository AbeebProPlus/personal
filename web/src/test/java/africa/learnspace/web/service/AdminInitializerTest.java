package africa.learnspace.web.service;

import africa.learnspace.user.manager.UserManager;
import africa.learnspace.user.model.User;
import africa.learnspace.usermanagement.request.UserRegistrationRequest;
import africa.learnspace.usermanagement.services.KeyCloakUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AdminInitializerTest {

    @InjectMocks
    private AdminInitializer adminInitializer;
    @Mock
    private KeyCloakUserService keyCloakUserService;
    @Mock
    private UserManager userManager;


    @Test
    public void testInit_WhenUserDoesNotExist() {
        when(userManager.findAll()).thenReturn(Collections.emptyList());
        when(keyCloakUserService.getUserRepresentations(anyString())).thenReturn(Collections.emptyList());
        adminInitializer.init();
        verify(userManager, times(1)).saveUser(any(User.class));
        verify(keyCloakUserService, times(1)).createUser(any(UserRegistrationRequest.class));
        verify(keyCloakUserService, times(1)).createPassword("learnspace@learnspace.africa", "Learnspace@2021");
    }

    @Test
    public void testInit_WhenUserExists() {
        User user = new User();
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername("learnspace");
        when(userManager.findAll()).thenReturn(Collections.singletonList(user));
        when(keyCloakUserService.getUserRepresentations(anyString())).thenReturn(Collections.singletonList(userRepresentation));
        adminInitializer.init();
        verify(userManager, never()).saveUser(any(User.class));
        verify(keyCloakUserService, never()).createUser(any(UserRegistrationRequest.class));
        verify(keyCloakUserService, never()).createPassword(anyString(), anyString());
    }
}