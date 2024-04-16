package ru.ase.mars.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ase.mars.enums.Roles;

@Data
@Entity
@NoArgsConstructor
@Table(name = "employee")
public class EmployeeEntity {

    @Id
    private int id;
    private String name;
    private String password;
    private Roles role;
}
