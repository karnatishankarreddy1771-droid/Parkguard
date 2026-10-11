package com.anurag.parking.config;

import com.anurag.parking.entity.User;
import com.anurag.parking.enums.Role;
import com.anurag.parking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {
    @Value("${parking.default-admin.email:}")
    private String email;

    @Value("${parking.default-admin.password:}")
    private String password;

    @Value("${parking.default-admin.reset-password-on-startup:false}")
    private boolean resetPasswordOnStartup;

    @Bean
    CommandLineRunner seedAdmin(UserRepository repo, PasswordEncoder encoder){
        return args -> {
            if (email.isBlank() || password.isBlank()) return;
            User existing=repo.findByEmail(email);
            if(existing==null){
                User admin=new User();
                admin.setName("System Administrator");
                admin.setEmail(email);
                admin.setPhone("");
                admin.setFlatNumber("");
                admin.setRole(Role.ADMIN);
                admin.setPassword(encoder.encode(password));
                repo.save(admin);
            } else if (resetPasswordOnStartup) {
                existing.setRole(Role.ADMIN);
                existing.setPassword(encoder.encode(password));
                repo.save(existing);
            }
        };
    }
}
