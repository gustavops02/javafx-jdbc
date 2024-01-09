package com.gustavo.model.services;

import com.gustavo.model.entities.Department;

import java.util.ArrayList;
import java.util.List;

public class DepartmentService {

    public List<Department> findAll() {
        List<Department> departments = new ArrayList<>();
        departments.add(new Department(1, "Livros"));
        departments.add(new Department(2, "Computadores"));
        departments.add(new Department(3, "Vestuário"));
        departments.add(new Department(4, "Higiene"));
        return departments;
    }

}
