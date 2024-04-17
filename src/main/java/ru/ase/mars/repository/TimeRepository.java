package ru.ase.mars.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.ase.mars.entity.Period;

public interface TimeRepository extends JpaRepository<Period, Integer> {

    Optional<Period> findFirstByExecutedIsNullOrderByFromDate();

    List<Period> findByExecutedIsNullOrderByFromDate();
}
