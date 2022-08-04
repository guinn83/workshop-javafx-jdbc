package com.workshop.model.dao;

import com.workshop.db.DB;
import com.workshop.model.dao.impl.SellerDaoJDBC;
import com.workshop.model.dao.impl.DepartmentDaoJDBC;


public class DaoFactory {

    public static SellerDao createSellerDao() {
        return new SellerDaoJDBC(DB.getConnection());
    }

    public static DepartmentDao createDepartmentDao() {
        return new DepartmentDaoJDBC(DB.getConnection());
    }

}
