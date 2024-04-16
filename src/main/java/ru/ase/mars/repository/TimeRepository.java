package ru.ase.mars.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ase.mars.entity.TimeEntity;

public interface TimeRepository extends JpaRepository<TimeEntity, Integer> {
}
