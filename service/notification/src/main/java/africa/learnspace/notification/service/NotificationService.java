package africa.learnspace.notification.service;

import africa.learnspace.notification.data.request.EmailRequest;
import africa.learnspace.notification.data.request.SendEmailRequest;
import org.thymeleaf.context.Context;

public interface NotificationService {
    void sendEmail(SendEmailRequest request);
    Context getNameAndLinkContext(String link, String firstName);
    SendEmailRequest getSendEmailRequest(EmailRequest request);
}
