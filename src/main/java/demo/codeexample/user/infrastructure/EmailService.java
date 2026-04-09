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

    /*Why SimpleMailMessage instead of MimeMessage?
    SimpleMailMessage is for plain text emails — simple and fast.
    MimeMessage supports HTML formatting, attachments, embedded images. For a welcome email, plain text is fine.
    You can upgrade to MimeMessage later if you want styled emails.*/

    public void sendWelcomeEmail(String toEmail, String firstName, String tempPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
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

        /*Why Java text blocks with """?
        Text blocks (triple quotes) allow multiline strings without \n everywhere. Much more readable.
        Available since Java 15.*/
    }
}
