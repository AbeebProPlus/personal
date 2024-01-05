package africa.learnspace.notification.service;

import africa.learnspace.notification.data.request.EmailRequest;
import africa.learnspace.notification.data.request.SendEmailRequest;
import africa.learnspace.notification.exception.NotificationException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final SpringTemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;

    @Override
    public void sendEmail(SendEmailRequest request) {
        CompletableFuture.supplyAsync(() -> {
            try {
                String emailContent = templateEngine.process(request.getHtmlContent(), request.getContext());
                MimeMessage mailMessage = getMimeMessage(request, emailContent);
                javaMailSender.send(mailMessage);
                return String.format("Email sent successfully to " + request.getTo());
            } catch (MessagingException | MailException | UnsupportedEncodingException exception) {
                throw new NotificationException(exception.getMessage());
            }
        });
    }

    private MimeMessage getMimeMessage(SendEmailRequest request, String emailContent) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mailMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mailMessage, "utf-8");
        mimeMessageHelper.setSubject(request.getSubject());
        mimeMessageHelper.setTo(request.getTo());
        mimeMessageHelper.setFrom(new InternetAddress("learnspace@learnspace.africa", "learnspace"));
        mimeMessageHelper.setText(emailContent, true);
        return mailMessage;
    }
    @Override
    public Context getNameAndLinkContext(String link, String firstName) {
        Context context = new Context();
        context.setVariable("token", link);
        context.setVariable("firstName", firstName);
        context.setVariable("currentYear", LocalDate.now().getYear());
        return context;
    }

    @Override
    public SendEmailRequest getSendEmailRequest(EmailRequest request) {
        SendEmailRequest emailRequest = new SendEmailRequest();
        emailRequest.setSubject(request.getSubject());
        emailRequest.setTo(request.getEmail());
        emailRequest.setHtmlContent(request.getTemplate());
        emailRequest.setContext(request.getContext());
        return emailRequest;
    }
}