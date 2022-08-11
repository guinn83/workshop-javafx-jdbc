package com.workshop.javafx.model.dao.impl;

import com.workshop.javafx.db.DB;
import com.workshop.javafx.db.DbException;
import com.workshop.javafx.db.DbIntegrityException;
import com.workshop.javafx.model.dao.SellerDao;
import com.workshop.javafx.model.entities.Department;
import com.workshop.javafx.model.entities.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {

    private final Connection con;

    public SellerDaoJDBC(Connection con) {
        this.con = con;
    }

    @Override
    public void insert(Seller obj) {
        PreparedStatement st = null;
        ResultSet rs;
        try {
            st = con.prepareStatement("" +
                    "SELECT Email " +
                    "FROM seller " +
                    "WHERE Email = ?");

            st.setString(1, obj.getEmail());
            rs = st.executeQuery();
            if (rs.next()) {
                throw new DbException("ERROR: Seller already exists!");
            }

            st = con.prepareStatement("" +
                            "INSERT INTO seller " +
                            "(Name, Email, BirthDate, BaseSalary, DepartmentId) " +
                            "VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            st.setString(1, obj.getName());
            st.setString(2, obj.getEmail());
            st.setDate(3, new Date(obj.getBirthDate().getTime()));
            st.setDouble(4, obj.getBaseSalary());
            st.setInt(5, obj.getDepartment().getId());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
                DB.closeResultSet(rs);
            } else {
                throw new DbException("No rows affected");
            }

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Seller obj) {
        PreparedStatement pst = null;
        try {
            pst = con.prepareStatement("" +
                    "UPDATE seller " +
                    "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? " +
                    "WHERE Id = ?");

            pst.setString(1, obj.getName());
            pst.setString(2, obj.getEmail());
            pst.setDate(3, new Date(obj.getBirthDate().getTime()));
            pst.setDouble(4, obj.getBaseSalary());
            pst.setInt(5, obj.getDepartment().getId());
            pst.setInt(6, obj.getId());

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
            pst = con.prepareStatement("DELETE FROM seller WHERE Id = ?");

            pst.setInt(1, id);

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected == 0) {
                throw new DbException("ERROR: There's no seller with this Id: " + id);
            }

        } catch (SQLException e) {
            throw new DbIntegrityException(e.getMessage());
        } finally {
            DB.closeStatement(pst);
        }
    }

    @Override
    public Seller findById(int id) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = con.prepareStatement("" +
                    "SELECT seller.*, department.Name as DepName " +
                    "FROM seller INNER JOIN department " +
                    "ON seller.DepartmentId = department.Id " +
                    "WHERE seller.Id = ?");

            pst.setInt(1, id);
            rs = pst.executeQuery();

            if (rs.next()) {
                Department dep = instantiateDepartment(rs);
                return instatiateSeller(rs, dep);
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
    public List<Seller> findByDepartment(int depId) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = con.prepareStatement("" +
                    "SELECT *, department.Name as DepName  " +
                    "FROM seller INNER JOIN department " +
                    "ON seller.DepartmentId = department.Id " +
                    "WHERE DepartmentId = ? " +
                    "ORDER BY seller.Name");


            pst.setInt(1, depId);
            rs = pst.executeQuery();

            List<Seller> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (rs.next()) {
                Department dep = map.get(depId);
                if (dep == null) {
                    dep = instantiateDepartment(rs);
                    map.put(depId, dep);
                }

                list.add(instatiateSeller(rs, dep));
            }

            return list;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(pst);
            DB.closeResultSet(rs);
        }
    }

    private Seller instatiateSeller(ResultSet rs, Department dep) throws SQLException {
        Seller seller = new Seller();
        seller.setId(rs.getInt(1));
        seller.setName(rs.getString(2));
        seller.setEmail(rs.getString(3));
        seller.setBirthDate(new java.util.Date(rs.getTimestamp(4).getTime()));
        seller.setBaseSalary(rs.getDouble(5));
        seller.setDepartment(dep);
        return seller;
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        return new Department(rs.getInt("DepartmentId"), rs.getString("DepName"));
    }

    @Override
    public List<Seller> findAll() {
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = con.prepareStatement("" +
                    "SELECT *, department.Name as DepName  " +
                    "FROM seller INNER JOIN department " +
                    "ON seller.DepartmentId = department.Id " +
                    "ORDER BY seller.Name");

            rs = pst.executeQuery();

            List<Seller> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (rs.next()) {
                int depId = rs.getInt("DepartmentId");
                Department dep = map.get(depId);
                if (dep == null) {
                    dep = instantiateDepartment(rs);
                    map.put(depId, dep);
                }

                list.add(instatiateSeller(rs, dep));
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
