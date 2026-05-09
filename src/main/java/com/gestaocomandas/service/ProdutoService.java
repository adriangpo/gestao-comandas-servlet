package com.gestaocomandas.service;

import com.gestaocomandas.dao.ProdutoDAO;
import com.gestaocomandas.vo.Produto;

import java.sql.SQLException;
import java.util.List;

public class ProdutoService {

    private final ProdutoDAO dao;

    public ProdutoService() {
        this.dao = new ProdutoDAO();
    }

    public void save(Produto produto) throws SQLException {
        if (produto.getId() > 0) {
            dao.update(produto);
        } else {
            dao.insert(produto);
        }
    }

    public void softDelete(int id) throws SQLException {
        dao.softDelete(id);
    }

    public void reactivate(int id) throws SQLException {
        dao.reactivate(id);
    }

    public Produto findById(int id) throws SQLException {
        return dao.findById(id);
    }

    public List<Produto> findAll() throws SQLException {
        return dao.findAll();
    }

    public List<Produto> findActive() throws SQLException {
        return dao.findActive();
    }
}
