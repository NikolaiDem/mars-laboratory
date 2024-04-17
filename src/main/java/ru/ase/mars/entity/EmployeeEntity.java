package ru.ase.mars.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ase.mars.enums.Roles;

@Data
@Entity
@NoArgsConstructor
@Table(name = "employee")
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private int id;
    private String name;
    private String password;
    @Enumerated(EnumType.STRING)
    private Roles role;
}
