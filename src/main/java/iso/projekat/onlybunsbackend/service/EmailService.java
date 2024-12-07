package iso.projekat.onlybunsbackend.service;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("onlybunz05@gmal.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        System.out.println(to);
        mailSender.send(message);
    }
}