package com.test.task.botscrewtask.repository;

import com.test.task.botscrewtask.model.Degree;
import com.test.task.botscrewtask.model.Department;
import com.test.task.botscrewtask.model.Lector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectorRepository extends JpaRepository<Lector, Long> {
    List<Lector> findByDepartmentsContaining(Department name);

    List<Lector> findByNameContainingIgnoreCase(String template);

    List<Lector> findByDepartmentsContainingAndDegree(Department department, Degree degree);
}
