package ru.ase.mars.security;

import jakarta.servlet.DispatcherType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.ase.mars.entity.EmployeeEntity;
import ru.ase.mars.enums.Roles;
import ru.ase.mars.repository.EmployeeRepository;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private EmployeeRepository employeeRepository;

    @Autowired
    public SecurityConfig(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    private void createUsers(PasswordEncoder encoder) {
        EmployeeEntity sci1 = new EmployeeEntity();
        sci1.setId(0);
        sci1.setName("sci1");
        sci1.setPassword(encoder.encode("123"));
        sci1.setRole(Roles.SCIENTIST);
        employeeRepository.save(sci1);

        EmployeeEntity sci2 = new EmployeeEntity();
        sci2.setId(1);
        sci2.setName("sci2");
        sci2.setPassword(encoder.encode("456"));
        sci2.setRole(Roles.SCIENTIST);
        employeeRepository.save(sci2);

        EmployeeEntity ins = new EmployeeEntity();
        ins.setId(2);
        ins.setName("ins1");
        ins.setPassword(encoder.encode("789"));
        ins.setRole(Roles.INSPECTOR);
        employeeRepository.save(ins);
    }

    @Bean
    public UserDetailsService user(PasswordEncoder encoder) {
        //createUsers(encoder);
        return username -> {
            EmployeeEntity employee = employeeRepository.getByName(username);
            if (employee == null) {
                throw new UsernameNotFoundException("");
            }
            return new User(employee.getName(), employee.getPassword(), Collections.emptyList());
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(
                (authorize) -> authorize
                        .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

}