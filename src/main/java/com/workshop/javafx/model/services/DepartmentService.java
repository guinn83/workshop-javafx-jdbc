package com.workshop.javafx.model.services;

import com.workshop.javafx.model.dao.DaoFactory;
import com.workshop.javafx.model.dao.DepartmentDao;
import com.workshop.javafx.model.entities.Department;

import java.util.List;

public class DepartmentService {
    private final DepartmentDao dao = DaoFactory.createDepartmentDao();

    public List<Department> findAll() {
        return dao.findAll();
    }

    public void saveOrUpdate(Department obj) {
        if (obj.getId() == null) {
            dao.insert(obj);
        } else {
            dao.update(obj);
        }
    }

    public void remove(Department obj) {
        dao.deleteById(obj.getId());
    }

}
