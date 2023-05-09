package com.test.task.botscrewtask.service;

import com.test.task.botscrewtask.model.Department;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MenuService implements CommandLineRunner {
    private final Scanner scanner = new Scanner(System.in);
    private final DepartmentService departmentService;

    public MenuService(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    private static final List<Pattern> patterns = new ArrayList<>();

    static {
        patterns.add(Pattern.compile("(Who is head of department )(.*)"));
        patterns.add(Pattern.compile("(Show )(.*)( statistics)"));
        patterns.add(Pattern.compile("(Show the average salary for the department )(.*)"));
        patterns.add(Pattern.compile("(Show count of employee for )(.*)"));
        patterns.add(Pattern.compile("(Global search by )(.*)"));
        patterns.add(Pattern.compile("(Exit)(.*)"));
    }


    private Matcher getScenario(String command) {
        return patterns.stream()
                .map(i -> i.matcher(command))
                .filter(Matcher::find)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Wrong command, try again"));
    }

    private void getMenu() {
        System.out.println("Please enter your command:");
        System.out.println("Who is head of department {department_name}");
        System.out.println("Show {department_name} statistics");
        System.out.println("Show the average salary for the department {department_name}");
        System.out.println("Show count of employee for {department_name}");
        System.out.println("Global search by {template}");
        System.out.println("Exit");

    }

    @Override
    public void run(String... args) {
        System.out.println("Welcome to University Console!");
        while (true) {
            getMenu();
            String command = scanner.nextLine();
            try {
                runMenu(command);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("-------------------------------------");
            }
        }
    }

    private void runMenu(String command) {
        Matcher matcher = getScenario(command);
        String inputParameter = matcher.group(2);

        switch (matcher.group(1)) {
            case "Who is head of department " -> {
                Department department = departmentService.getDepartment(inputParameter);
                if (department != null && department.getHead() != null) {
                    System.out.println("Head of " + inputParameter + " department is " + department.getHead().getName());
                } else {
                    System.out.println("Department not found or no head assigned");
                }
            }
            case "Show " -> {
                String departmentStatistics = departmentService.getDepartmentStatistics(inputParameter);
                System.out.println(departmentStatistics);
            }
            case "Show the average salary for the department " -> {
                double averageSalary = departmentService.getDepartmentAverageSalary(inputParameter);
                System.out.println("The average salary of " + inputParameter + " is " + averageSalary);
            }
            case "Show count of employee for " -> {
                int employeeCount = departmentService.getDepartmentEmployeeCount(inputParameter);
                System.out.println(employeeCount);
            }
            case "Global search by " -> {
                List<String> list = departmentService.globalSearch(inputParameter);
                if (list != null) {
                    list.forEach(System.out::println);
                }
                System.out.println("No results found!");
            }
            case "Exit" -> {
                scanner.close();
                System.exit(0);
            }
        }
    }
}

