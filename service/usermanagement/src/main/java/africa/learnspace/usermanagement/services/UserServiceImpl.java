package africa.learnspace.usermanagement.services;

import africa.learnspace.notification.controller.NotificationController;
import africa.learnspace.notification.data.request.EmailRequest;
import africa.learnspace.notification.data.request.SendEmailRequest;
import africa.learnspace.usermanagement.exception.LearnSpaceUserException;
import africa.learnspace.usermanagement.iam.TokenGenerator;
import africa.learnspace.usermanagement.request.PasswordUpdateRequest;
import africa.learnspace.usermanagement.request.UserRegistrationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final AuthService authService;
    private final NotificationController notificationController;
    private final TokenGenerator tokenGenerator;
    private final KeyCloakUserService keyCloakUserService;
    @Value("${baseUrl:http://localhost:3000}")
    private String baseUrl;

    @Override
    public String inviteColleague(UserRegistrationRequest request) {
        String token = tokenGenerator.generateToken(request.getEmail());
        log.info("token->{}", token);
        String link = baseUrl + "/create-password?token=" + token;
        request.setRole("FINANCE_MANAGER");
        authService.createUser(request);
        SendEmailRequest emailRequest = getSendEmailRequest(request, link);
        notificationController.sendEmail(emailRequest);
        return "User created successfully";
    }

    private SendEmailRequest getSendEmailRequest(UserRegistrationRequest request, String link) {
        Context context = notificationController.getNameAndLinkContext(link, request.getFirstName());
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setContext(context);
        emailRequest.setSubject("Invitation to LearnSpace");
        emailRequest.setEmail(request.getEmail());
        emailRequest.setTemplate("colleague-invitation");
        emailRequest.setFirstName(request.getFirstName());
        return notificationController.getSendEmailRequest(emailRequest);
    }


    @Override
    public String changePassword(PasswordUpdateRequest request, String email) {
        AccessTokenResponse tokenResponse = authService.login(email, request.getCurrentPassword());
        if (tokenResponse == null) throw new LearnSpaceUserException("Invalid Password");
        keyCloakUserService.createPassword(email, request.getNewPassword());
        return "Password Updated Successful";
    }
}