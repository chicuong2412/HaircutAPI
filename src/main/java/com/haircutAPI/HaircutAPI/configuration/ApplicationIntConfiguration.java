package com.haircutAPI.HaircutAPI.configuration;

import java.util.HashSet;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.haircutAPI.HaircutAPI.ENUM.UserType;
import com.haircutAPI.HaircutAPI.enity.User;
import com.haircutAPI.HaircutAPI.repositories.UserRepository;

@Configuration
public class ApplicationIntConfiguration {
    
    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (!userRepository.existsByUsername("admin")) {
                HashSet<String> roles = new HashSet<>();
                roles.add(UserType.ADMIN.name());
                PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
                User user = new User();
                user.setUsername("admin");
                user.setPassword(passwordEncoder.encode("admin"));
                user.setRoles(roles);
                
                userRepository.save(user);
            }
        };
    }
}
