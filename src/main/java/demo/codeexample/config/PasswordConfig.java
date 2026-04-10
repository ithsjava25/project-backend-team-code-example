package demo.codeexample.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
        // Default cost factor is 10 — means 2^10 = 1024 hashing rounds
        // Higher = slower to crack, but also slower to process
        // 10 is the industry standard sweet spot
    }
}
