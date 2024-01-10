package com.gustavo.model.dao;

import com.gustavo.infra.db.DB;
import com.gustavo.model.dao.impl.DepartmentDaoJDBC;
import com.gustavo.model.dao.impl.SellerDaoJDBC;

public class DaoFactory {

    public static SellerDao createSellerDao() {
        return new SellerDaoJDBC(DB.getConnection());
    }

    public static DepartmentDao createDepartmentDao() {
        return new DepartmentDaoJDBC(DB.getConnection());
    }
}
