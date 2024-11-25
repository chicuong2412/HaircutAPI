package com.haircutAPI.HaircutAPI.configuration;


import java.util.HashSet;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.haircutAPI.HaircutAPI.ENUM.RoleEmployee;
import com.haircutAPI.HaircutAPI.ENUM.UserType;
import com.haircutAPI.HaircutAPI.enity.User;
import com.haircutAPI.HaircutAPI.enity.Worker;
import com.haircutAPI.HaircutAPI.repositories.UserRepository;
import com.haircutAPI.HaircutAPI.repositories.WorkerRepository;

@Configuration
public class ApplicationIntConfiguration {
    
    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository, WorkerRepository workerRepository) {
        return args -> {
            if (!userRepository.existsByUsername("admin")) {
                HashSet<String> roles = new HashSet<>();
                roles.add(UserType.WORKER.name());
                PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
                User user = new User();
                user.setUsername("admin");
                user.setPassword(passwordEncoder.encode("admin"));
                user.setRoles(roles);
                
                userRepository.save(user);

                Worker worker = new Worker();
                worker.setId(user.getId());
                worker.setUsername(user.getUsername());
                worker.setPassword(user.getPassword());
                worker.setIdRole(RoleEmployee.ADMIN);
                workerRepository.save(worker);
            }
        };
    }
}
