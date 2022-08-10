package com.workshop.javafx.model.services;

import com.workshop.javafx.model.dao.DaoFactory;
import com.workshop.javafx.model.dao.SellerDao;
import com.workshop.javafx.model.entities.Seller;

import java.util.List;

public class SellerService {
    private final SellerDao dao = DaoFactory.createSellerDao();

    public List<Seller> findAll() {
        return dao.findAll();
    }

    public void saveOrUpdate(Seller obj) {
        if (obj.getId() == null) {
            dao.insert(obj);
        } else {
            dao.update(obj);
        }
    }

    public void remove(Seller obj) {
        dao.deleteById(obj.getId());
    }

}
