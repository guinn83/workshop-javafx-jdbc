package com.workshop.model.dao.impl;

import com.workshop.db.DB;
import com.workshop.db.DbException;
import com.workshop.db.DbIntegrityException;
import com.workshop.model.dao.DepartmentDao;
import com.workshop.model.entities.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoJDBC implements DepartmentDao {

    private final Connection con;

    public DepartmentDaoJDBC(Connection con) {
        this.con = con;
    }

    @Override
    public void insert(Department obj) {
        PreparedStatement st = null;
        ResultSet rs;
        try{
            st = con.prepareStatement("" +
                    "SELECT Name " +
                    "FROM department " +
                    "WHERE Name = ?");

            st.setString(1, obj.getName());
            rs = st.executeQuery();
            if (rs.next()) {
                throw new DbException("ERROR: Department already exists!");
            }

            st = con.prepareStatement("" +
                    "INSERT INTO department " +
                    "(Name) " +
                    "VALUES (?)",
                    Statement.RETURN_GENERATED_KEYS);

            st.setString(1, obj.getName());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
                DB.closeResultSet(rs);
            } else {
                throw new DbException("ERROR: No rows affected");
            }

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Department obj) {
        PreparedStatement pst = null;
        try {
            pst = con.prepareStatement("" +
                    "UPDATE department " +
                    "SET Name = ? " +
                    "WHERE Id = ?");

            pst.setString(1, obj.getName());
            pst.setInt(2, obj.getId());

            int rowsAffected = pst.executeUpdate();

            System.out.println("Done! Updated row : " + rowsAffected);

        } catch (SQLException e) {
            throw new DbIntegrityException(e.getMessage());
        } finally {
            DB.closeStatement(pst);
        }
    }

    @Override
    public void deleteById(int id) {
        PreparedStatement pst = null;
        try {
            pst = con.prepareStatement("DELETE FROM department WHERE Id = ?");

            pst.setInt(1, id);

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected == 0) {
                throw new DbException("ERROR: There's no department with this Id: " + id);
            }

        } catch (SQLException e) {
            throw new DbIntegrityException(e.getMessage());
        } finally {
            DB.closeStatement(pst);
        }
    }

    @Override
    public void deleteByName(String name) {
        PreparedStatement pst = null;
        try {
            pst = con.prepareStatement("DELETE FROM department WHERE Name = ?");

            pst.setString(1, name);

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected == 0) {
                throw new DbException("ERROR: There's no department with this name: " + name);
            }

        } catch (SQLException e) {
            throw new DbIntegrityException(e.getMessage());
        } finally {
            DB.closeStatement(pst);
        }
    }

    @Override
    public Department findById(int id) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = con.prepareStatement("" +
                    "SELECT * " +
                    "FROM department " +
                    "WHERE Id = ?");

            pst.setInt(1, id);
            rs = pst.executeQuery();

            if (rs.next()) {
                return new Department(id, rs.getString("Name"));
            }

            return null;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(pst);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Department> findAll() {
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = con.prepareStatement("" +
                    "SELECT * " +
                    "FROM department " +
                    "ORDER BY Name");

            rs = pst.executeQuery();

            List<Department> list = new ArrayList<>();

            while (rs.next()) {
                Department dep = new Department(rs.getInt("Id"), rs.getString("Name"));
                list.add(dep);
            }

            return list;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(pst);
            DB.closeResultSet(rs);
        }
    }
}
