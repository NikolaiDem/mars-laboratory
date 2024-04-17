package ru.ase.mars.config;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.ase.mars.entity.EmployeeEntity;
import ru.ase.mars.enums.Roles;
import ru.ase.mars.repository.EmployeeRepository;

@Component
@AllArgsConstructor
public class UserInit {

    private EmployeeRepository employeeRepository;
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void create() {
        long count = employeeRepository.count();
        if (count == 0) {
            createUsers();
        }
    }

    private void createUsers() {
        EmployeeEntity sci1 = new EmployeeEntity();
        sci1.setName("sci1");
        sci1.setPassword(passwordEncoder.encode("123"));
        sci1.setRole(Roles.SCIENTIST);
        employeeRepository.save(sci1);

        EmployeeEntity sci2 = new EmployeeEntity();
        sci2.setName("sci2");
        sci2.setPassword(passwordEncoder.encode("456"));
        sci2.setRole(Roles.SCIENTIST);
        employeeRepository.save(sci2);

        EmployeeEntity ins = new EmployeeEntity();
        ins.setName("ins1");
        ins.setPassword(passwordEncoder.encode("789"));
        ins.setRole(Roles.INSPECTOR);
        employeeRepository.save(ins);
    }
}