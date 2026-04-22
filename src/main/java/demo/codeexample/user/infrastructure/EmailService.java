package demo.codeexample.user.infrastructure;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender=mailSender;
    }

    public void sendWelcomeEmail(String toEmail, String firstName, String tempPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@filmstudio.com");
        message.setTo(toEmail);
        message.setSubject("Welcome to FilmStudio - Your Account Details");
        message.setText("""
                Hi %s,
                
                Your account has been created at Film Studio.
                
                Login details:
                Email:    %s
                Password: %s
                
                Please log in and change your password immediately.
                
                Best regards,
                Film Studio Admin Team
                """.formatted(firstName, toEmail, tempPassword));

        mailSender.send(message);
    }
    public void sendPasswordResetEmail(String toEmail,
                                       String firstName,
                                       String tempPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Film Studio — Password Reset");
        message.setText("""
            Hi %s,

            Your password has been reset by an administrator.

            New temporary login details:
            Email:    %s
            Password: %s

            Please log in and change your password immediately.

            Best regards,
            Film Studio Admin Team
            """.formatted(firstName, toEmail, tempPassword));

        mailSender.send(message);
    }


}
