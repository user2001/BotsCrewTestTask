package com.test.task.botscrewtask.service;

import com.test.task.botscrewtask.exception.NotFoundException;
import com.test.task.botscrewtask.model.Degree;
import com.test.task.botscrewtask.model.Department;
import com.test.task.botscrewtask.model.Lector;
import com.test.task.botscrewtask.repository.DepartmentRepository;
import com.test.task.botscrewtask.repository.LectorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final LectorRepository lectorRepository;

    public Department getDepartment(String departmentName) {
        return departmentRepository.findByName(departmentName).orElseThrow(() ->
                new NotFoundException(String.format("Department %s not found", departmentName)));
    }

    public String getDepartmentStatistics(String name) {
        Department department = getDepartment(name);
        long assistantsCount = lectorRepository.findByDepartmentsContainingAndDegree(department, Degree.ASSISTANT).size();
        long associateProfessorsCount = lectorRepository.findByDepartmentsContainingAndDegree(department, Degree.ASSOCIATE_PROFESSOR).size();
        long professorsCount = lectorRepository.findByDepartmentsContainingAndDegree(department, Degree.PROFESSOR).size();
        return "assistants - " + assistantsCount + ". associate professors - " + associateProfessorsCount +
                ". professors - " + professorsCount;
    }

    public Double getDepartmentAverageSalary(String name) {
        Department department = getDepartment(name);
        List<Lector> lectorsInDepartment = lectorRepository.findByDepartmentsContaining(department);
        return lectorsInDepartment.stream().mapToDouble(Lector::getSalary).average().orElse(0.0);
    }

    public int getDepartmentEmployeeCount(String departmentName) {
        Department department = getDepartment(departmentName);
        return department.getLectors().size();
    }

    public List<String> globalSearch(String template) {
        return new ArrayList<>(lectorRepository.findByNameContainingIgnoreCase(template).stream()
                .map(Lector::getName).toList());
    }
}
