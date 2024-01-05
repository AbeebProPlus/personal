package africa.learnspace.usermanagement.services;

import africa.learnspace.user.manager.UserManager;
import africa.learnspace.user.model.User;
import africa.learnspace.usermanagement.exception.LearnSpaceUserException;
import africa.learnspace.usermanagement.request.UserRegistrationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.representations.AccessTokenResponse;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authService;
    private UserRegistrationRequest userRegistrationRequest;
    @Mock
    private UserManager userManager;
    @Mock
    private KeyCloakServiceImpl keyCloakService;

    private final String email = "learnspace@learnspace.africa";
    private final String password = "password123";
    @BeforeEach
    void setUp() {
        userRegistrationRequest = new UserRegistrationRequest();
        userRegistrationRequest.setEmail(email);
        userRegistrationRequest.setFirstName("Learnspace");
        userRegistrationRequest.setLastName("Africa");
        userRegistrationRequest.setRole("TEST");
    }

    @Test
    public void testCreateUserWithValidRequest() {
        authService.createUser(userRegistrationRequest);
        verify(userManager, times(1)).saveUser(any(User.class));
        verify(keyCloakService, times(1)).createUser(userRegistrationRequest);
    }

    @Test
    public void testCreateUserWithSaveUserException() {
        assertDoesNotThrow(() -> authService.createUser(userRegistrationRequest));
        verify(userManager, times(1)).saveUser(any(User.class));
        verify(keyCloakService, times(1)).createUser(userRegistrationRequest);
    }

    @Test
    public void testCreateUser_NullInput() {
        assertThrows(LearnSpaceUserException.class,
                () -> authService.createUser(new UserRegistrationRequest()));
    }


    @Test
    public void testLogin_Success() {
        AccessTokenResponse expectedResponse = new AccessTokenResponse();
        expectedResponse.setToken("token");
        when(keyCloakService.login(email, password)).thenReturn(expectedResponse);
        AccessTokenResponse actualResponse = authService.login(email, password);
        verify(keyCloakService, times(1)).login(email, password);
       assertEquals(expectedResponse.getToken(), actualResponse.getToken());
    }


    @Test
    public void testLogin_Failure() {
        when(keyCloakService.login(email, password)).thenReturn(null);
        AccessTokenResponse actualResponse = authService.login(email, password);
        verify(keyCloakService, times(1)).login(email, password);
        assertNull(actualResponse);
    }
}