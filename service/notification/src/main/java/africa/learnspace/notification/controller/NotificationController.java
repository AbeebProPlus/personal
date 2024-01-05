package africa.learnspace.notification.controller;

import africa.learnspace.notification.data.request.EmailRequest;
import africa.learnspace.notification.data.request.SendEmailRequest;
import africa.learnspace.notification.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

@Component
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void sendEmail(@Valid SendEmailRequest sendEmailRequest) {
        notificationService.sendEmail(sendEmailRequest);
    }

    public Context getNameAndLinkContext(String link, String firstName) {
        return notificationService.getNameAndLinkContext(link, firstName);
    }

    public SendEmailRequest getSendEmailRequest(EmailRequest emailRequest) {
        return notificationService.getSendEmailRequest(emailRequest);
    }
}
