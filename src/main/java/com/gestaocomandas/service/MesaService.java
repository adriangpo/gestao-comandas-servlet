package com.gestaocomandas.service;

import com.gestaocomandas.dao.MesaDAO;
import com.gestaocomandas.vo.Mesa;

import java.sql.SQLException;
import java.util.List;

public class MesaService {

    private final MesaDAO dao;

    public MesaService() {
        this.dao = new MesaDAO();
    }

    public void save(Mesa mesa) throws SQLException {
        if (mesa.getId() > 0) {
            dao.update(mesa);
        } else {
            mesa.setOrdemExibicao(dao.nextOrdemExibicao());
            dao.insert(mesa);
        }
    }

    public void updateOrdemExibicao(int id, int ordemExibicao) throws SQLException {
        dao.updateOrdemExibicao(id, ordemExibicao);
    }

    public void softDelete(int id) throws SQLException {
        dao.softDelete(id);
    }

    public void reactivate(int id) throws SQLException {
        dao.reactivate(id);
    }

    public List<Mesa> findAll() throws SQLException {
        return dao.findAll();
    }

    public void updateQuantidadePessoas(int id, int quantidadePessoas) throws SQLException {
        dao.updateQuantidadePessoas(id, quantidadePessoas);
    }

    public Mesa findById(int id) throws SQLException {
        return dao.findById(id);
    }

    public List<Mesa> findAllOrdered() throws SQLException {
        return dao.findAllOrdered();
    }
}
