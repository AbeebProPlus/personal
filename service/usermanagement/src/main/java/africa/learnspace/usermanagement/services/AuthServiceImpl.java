package africa.learnspace.usermanagement.services;

import africa.learnspace.notification.controller.NotificationController;
import africa.learnspace.notification.data.request.SendEmailRequest;
import africa.learnspace.user.manager.UserManager;
import africa.learnspace.user.model.Role;
import africa.learnspace.user.model.User;
import africa.learnspace.usermanagement.exception.LearnSpaceUserException;
import africa.learnspace.usermanagement.iam.TokenGenerator;
import africa.learnspace.usermanagement.request.UserRegistrationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final UserManager userManager;
    private final NotificationController notificationController;
    private final KeyCloakUserService keyCloakUserService;
    private final TokenGenerator tokenGenerator;

    @Value("${baseUrl:http://localhost:3000}")
    private String baseUrl;
    @Override
    @Transactional
    public void createUser(UserRegistrationRequest userRegistrationRequest) {
        if (userRegistrationRequest.getEmail() == null
                || userRegistrationRequest.getFirstName() == null
                || userRegistrationRequest.getRole() == null) {
            throw new LearnSpaceUserException("Invalid user registration request");
        }
        if(userManager.findByEmail(userRegistrationRequest.getEmail()) != null){
            throw new LearnSpaceUserException("User already exists");
        }
        Set<Role> roles = new HashSet<>();
        roles.add(Role.valueOf(userRegistrationRequest.getRole().toUpperCase().trim()));
        User user = new User();
        user.setEmail(userRegistrationRequest.getEmail());
        user.setFirstName(userRegistrationRequest.getFirstName());
        user.setLastName(userRegistrationRequest.getLastName());
        user.setRoles(roles);
        userManager.saveUser(user);
        keyCloakUserService.createUser(userRegistrationRequest);
    }


    @Override
    public void createPassword(String token, String password) {
        String email = tokenGenerator.decodeJWT(token);
        User foundUser = userManager.findByEmail(email);
        foundUser.setDisabled(false);
        foundUser.setVerified(true);
        userManager.saveUser(foundUser);
        keyCloakUserService.createPassword(email, password);
    }

    @Override
    public AccessTokenResponse login(String email, String password) {
        return keyCloakUserService.login(email, password);
    }

    @Override
    public void forgetPassword(String email) {
        User foundUser = userManager.findByEmail(email);
        if(foundUser == null) return;

        String token = tokenGenerator.generateToken(email);
        String link = baseUrl + "/create-password?token=" + token;

        Context context = new Context();
        context.setVariable("token", link);
        context.setVariable("firstName", foundUser.getFirstName());
        context.setVariable("currentYear", LocalDate.now().getYear());

        SendEmailRequest emailRequest = new SendEmailRequest();
        emailRequest.setSubject("Password Reset Instructions");
        emailRequest.setTo(email);
        emailRequest.setHtmlContent("password_reset");
        emailRequest.setContext(context);
        notificationController.sendEmail(emailRequest);
    }
}
