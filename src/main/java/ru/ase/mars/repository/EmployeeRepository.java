package ru.ase.mars.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ase.mars.entity.EmployeeEntity;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer> {

    EmployeeEntity getByName(String name);
}
