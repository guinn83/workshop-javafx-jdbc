package com.workshop.model.dao;

import com.workshop.model.entities.Department;

import java.util.List;

public interface DepartmentDao {

    void insert(Department obj);

    void update(Department obj);

    void deleteById(int id);

    void deleteByName(String name);

    Department findById(int id);

    List<Department> findAll();

}
