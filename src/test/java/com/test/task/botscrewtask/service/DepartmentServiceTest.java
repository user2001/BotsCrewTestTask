package com.test.task.botscrewtask.service;

import com.test.task.botscrewtask.exception.NotFoundException;
import com.test.task.botscrewtask.model.Degree;
import com.test.task.botscrewtask.model.Department;
import com.test.task.botscrewtask.model.Lector;
import com.test.task.botscrewtask.repository.DepartmentRepository;
import com.test.task.botscrewtask.repository.LectorRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private LectorRepository lectorRepository;

    @InjectMocks
    private DepartmentService departmentService;

    @Test
    public void testGetDepartmentValidName() {
        Department department = new Department();
        department.setName("Computer Science");
        Mockito.when(departmentRepository.
                        findByName(Mockito.anyString())).
                thenReturn(Optional.of(department));

        Department result = departmentService.getDepartment("Computer Science");
        Assert.assertEquals("Computer Science", result.getName());
    }

    @Test
    public void testGetDepartmentInvalidName() {
        Mockito.when(departmentRepository.findByName(Mockito.anyString())).thenReturn(Optional.empty());
        try {
            departmentService.getDepartment("Invalid Department Name");
            Assertions.fail("Expected NotFoundException was not thrown");
        } catch (NotFoundException ignored) {
        }
    }

    @Test
    public void testGetDepartmentStatistics() {

        Department department = new Department();
        department.setName("Test Department");

        Lector assistant1 = new Lector();
        assistant1.setName("Assistant 1");
        assistant1.setDegree(Degree.ASSISTANT);
        assistant1.setDepartments(Collections.singleton(department));

        Lector assistant2 = new Lector();
        assistant2.setName("Assistant 2");
        assistant2.setDegree(Degree.ASSISTANT);
        assistant2.setDepartments(Collections.singleton(department));

        Lector associateProfessor1 = new Lector();
        associateProfessor1.setName("Associate Professor 1");
        associateProfessor1.setDegree(Degree.ASSOCIATE_PROFESSOR);
        associateProfessor1.setDepartments(Collections.singleton(department));

        Lector professor1 = new Lector();
        professor1.setName("Professor 1");
        professor1.setDegree(Degree.PROFESSOR);
        professor1.setDepartments(Collections.singleton(department));

        Mockito.when(departmentRepository.findByName(Mockito.anyString())).thenReturn(Optional.of(department));
        Mockito.when(lectorRepository.findByDepartmentsContainingAndDegree(department, Degree.ASSISTANT))
                .thenReturn(Arrays.asList(assistant1, assistant2));
        Mockito.when(lectorRepository.findByDepartmentsContainingAndDegree(department, Degree.ASSOCIATE_PROFESSOR))
                .thenReturn(Collections.singletonList(associateProfessor1));
        Mockito.when(lectorRepository.findByDepartmentsContainingAndDegree(department, Degree.PROFESSOR))
                .thenReturn(Collections.singletonList(professor1));

        String result = departmentService.getDepartmentStatistics("Test Department");

        String expected = "assistants - 2. associate professors - 1. professors - 1";
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testGetDepartmentAverageSalary() {
        Department department = new Department();
        department.setName("Computer Science");
        Mockito.when(departmentRepository.findByName(Mockito.anyString())).thenReturn(Optional.of(department));
        List<Lector> lectors = List.of(new Lector("John Doe", Degree.ASSISTANT, 5000.0),
                new Lector("Jane Doe", Degree.ASSOCIATE_PROFESSOR, 6000.0));
        Mockito.when(lectorRepository.findByDepartmentsContaining(Mockito.any(Department.class)))
                .thenReturn(lectors);

        Double result = departmentService.getDepartmentAverageSalary("Computer Science");
        Assertions.assertEquals(5500.0, result, 0.1);
    }

    @Test
    public void testGlobalSearch() {
        Lector lector1 = new Lector("John Doe", Degree.ASSOCIATE_PROFESSOR, 2000.00);
        Lector lector2 = new Lector("Jane Doe", Degree.ASSISTANT, 1000.00);
        Mockito.when(lectorRepository.findByNameContainingIgnoreCase(Mockito.anyString()))
                .thenReturn(List.of(lector1, lector2));

        List<String> result = departmentService.globalSearch("Doe");
        Assertions.assertEquals(List.of("John Doe", "Jane Doe"), result);
    }
}