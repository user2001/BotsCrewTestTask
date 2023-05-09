package com.test.task.botscrewtask.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "lectors")
public class Lector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Degree degree;

    private Double salary;

    @ManyToMany(mappedBy = "lectors", fetch = FetchType.EAGER)
    private Set<Department> departments;

    public Lector(String name, Degree degree, Double salary) {
        this.name = name;
        this.degree = degree;
        this.salary = salary;
    }
}
