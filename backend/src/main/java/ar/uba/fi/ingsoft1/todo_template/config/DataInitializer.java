package ar.uba.fi.ingsoft1.todo_template.config;

import ar.uba.fi.ingsoft1.todo_template.user.User;
import ar.uba.fi.ingsoft1.todo_template.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder){
        return args -> {
            if(userRepository.findByUsername("admin").isEmpty()){
                User admin = new User("admin", passwordEncoder.encode("admin123"),"ADMIN","admin@dummy.com","Jose","Admin","https://en.wikipedia.org/wiki/File:First_Web_Server.jpg",LocalDate.now());
                admin.promover();
                userRepository.save(admin);
            }
        };
    }
}
