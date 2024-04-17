package ru.ase.mars;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@SpringBootApplication
public class ReportServiceApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ReportServiceApplication.class, args);
        /*PasswordEncoder bean = context.getBean(PasswordEncoder.class);

        String str = "INSERT INTO employee(id,name,password,role) values (%s,%s,%s,%s);";
        Random random = new Random();
        for(int i=0;i<20;i++) {
            String pass = "password" + i;
            String role = random.nextInt() % 2 == 0? Roles.INSPECTOR.name() : Roles.SCIENTIST.name();
            String encode = "'" + bean.encode(pass) + "'";
            String user = "'" + "user" + i + "'";
            String format = String.format(str, i, user, encode, "'" + role + "'");
            System.out.println(format);
        }*/
    }

}
