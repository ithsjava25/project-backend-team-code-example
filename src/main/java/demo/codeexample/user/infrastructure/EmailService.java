package demo.codeexample.user.infrastructure;

import demo.codeexample.company.TenantContext;
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

// Get tenant from context — which company is this user for?
        String tenant  = TenantContext.getTenant();
        String loginUrl = buildLoginUrl(tenant);

        SimpleMailMessage message = new SimpleMailMessage();

//        message.setFrom("noreply@filmstudio.com");
        message.setTo(toEmail);
        message.setSubject("Welcome to FilmStudio - Your Account Details");
        message.setText("""
                Hi %s,
                
                Your account has been created at Film Studio.
                
                Login details:
                Email:    %s
                Password: %s
                
                Login here: %s
                
                Please change your password immediately after logging in.
                
                Best regards,
                Film Studio Admin Team
                """.formatted(firstName, toEmail, tempPassword, loginUrl));

        mailSender.send(message);
    }

    private String buildLoginUrl(String tenant) {
        String baseUrl = "http://localhost:8080";  // from properties ideally!
        if (tenant == null || tenant.isBlank()) {
            return baseUrl + "/login";
        }
        return baseUrl + "/" + tenant.toLowerCase() + "/login";
    }

    public void sendPasswordResetEmail(String toEmail,
                                       String firstName,
                                       String tempPassword) {
        String tenant   = TenantContext.getTenant();
        String loginUrl = buildLoginUrl(tenant);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Film Studio — Password Reset");
        message.setText("""
            Hi %s,

            Your password has been reset by an administrator.

            New temporary login details:
            Email:    %s
            Password: %s

            Login here: %s

            Please change your password immediately.

            Best regards,
            Film Studio Admin Team
            """.formatted(firstName, toEmail, tempPassword, loginUrl));

        mailSender.send(message);
    }
}
