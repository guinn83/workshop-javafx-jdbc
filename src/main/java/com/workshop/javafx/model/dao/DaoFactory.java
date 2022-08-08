package com.workshop.javafx.model.dao;

import com.workshop.javafx.db.DB;
import com.workshop.javafx.model.dao.impl.DepartmentDaoJDBC;
import com.workshop.javafx.model.dao.impl.SellerDaoJDBC;


public class DaoFactory {

    public static SellerDao createSellerDao() {
        return new SellerDaoJDBC(DB.getConnection());
    }

    public static DepartmentDao createDepartmentDao() {
        return new DepartmentDaoJDBC(DB.getConnection());
    }

}
