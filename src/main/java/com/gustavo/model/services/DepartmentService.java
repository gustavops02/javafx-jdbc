package com.gustavo.model.services;

import com.gustavo.model.dao.DaoFactory;
import com.gustavo.model.dao.DepartmentDao;
import com.gustavo.model.entities.Department;

import java.util.List;

public class DepartmentService {

    private DepartmentDao dao = DaoFactory.createDepartmentDao();

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

}
