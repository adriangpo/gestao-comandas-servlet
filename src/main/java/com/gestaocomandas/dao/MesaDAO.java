package com.gestaocomandas.dao;

import com.gestaocomandas.connection.Connection;
import com.gestaocomandas.vo.Mesa;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MesaDAO {

    private final Connection connection;

    public MesaDAO() {
        this.connection = new Connection();
    }

    public void insert(Mesa mesa) throws SQLException {
        String sql = "INSERT INTO mesas (identificador, capacidade, ordem_exibicao) VALUES (?, ?, ?)";
        java.sql.Connection sqlConnection = connection.connect();

        try {
            PreparedStatement statement = sqlConnection.prepareStatement(sql);
            statement.setString(1, mesa.getIdentificador());
            statement.setInt(2, mesa.getCapacidade());
            statement.setInt(3, mesa.getOrdemExibicao());
            statement.executeUpdate();
            statement.close();
        } finally {
            connection.disconnect();
        }
    }

    public void update(Mesa mesa) throws SQLException {
        String sql = "UPDATE mesas SET identificador = ?, capacidade = ? WHERE id = ?";
        java.sql.Connection sqlConnection = connection.connect();

        try {
            PreparedStatement statement = sqlConnection.prepareStatement(sql);
            statement.setString(1, mesa.getIdentificador());
            statement.setInt(2, mesa.getCapacidade());
            statement.setInt(3, mesa.getId());
            statement.executeUpdate();
            statement.close();
        } finally {
            connection.disconnect();
        }
    }

    public void updateOrdemExibicao(int id, int ordemExibicao) throws SQLException {
        String sql = "UPDATE mesas SET ordem_exibicao = ? WHERE id = ?";
        java.sql.Connection sqlConnection = connection.connect();

        try {
            PreparedStatement statement = sqlConnection.prepareStatement(sql);
            statement.setInt(1, ordemExibicao);
            statement.setInt(2, id);
            statement.executeUpdate();
            statement.close();
        } finally {
            connection.disconnect();
        }
    }

    public int nextOrdemExibicao() throws SQLException {
        String sql = "SELECT COALESCE(MAX(ordem_exibicao), 0) + 1 FROM mesas";
        java.sql.Connection sqlConnection = connection.connect();

        try {
            PreparedStatement statement = sqlConnection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int next = resultSet.getInt(1);
            resultSet.close();
            statement.close();
            return next;
        } finally {
            connection.disconnect();
        }
    }

    public void updateQuantidadePessoas(int id, int quantidadePessoas) throws SQLException {
        String sql = "UPDATE mesas SET quantidade_pessoas = ? WHERE id = ?";
        java.sql.Connection sqlConnection = connection.connect();

        try {
            PreparedStatement statement = sqlConnection.prepareStatement(sql);
            statement.setInt(1, quantidadePessoas);
            statement.setInt(2, id);
            statement.executeUpdate();
            statement.close();
        } finally {
            connection.disconnect();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM mesas WHERE id = ?";
        java.sql.Connection sqlConnection = connection.connect();

        try {
            PreparedStatement statement = sqlConnection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
            statement.close();
        } finally {
            connection.disconnect();
        }
    }

    public Mesa findById(int id) throws SQLException {
        String sql = "SELECT * FROM mesas WHERE id = ?";
        java.sql.Connection sqlConnection = connection.connect();

        try {
            PreparedStatement statement = sqlConnection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            Mesa mesa = null;
            if (resultSet.next()) {
                mesa = mapRow(resultSet);
            }

            resultSet.close();
            statement.close();
            return mesa;
        } finally {
            connection.disconnect();
        }
    }

    public List<Mesa> findAllOrdered() throws SQLException {
        String sql = "SELECT m.*, " +
                "(SELECT COUNT(*) FROM pedidos p WHERE p.mesa_id = m.id AND p.status = 'PENDENTE') AS pending_count " +
                "FROM mesas m ORDER BY m.ordem_exibicao";
        java.sql.Connection sqlConnection = connection.connect();

        try {
            PreparedStatement statement = sqlConnection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            List<Mesa> list = new ArrayList<>();
            while (resultSet.next()) {
                Mesa mesa = mapRow(resultSet);
                mesa.setOcupada(resultSet.getInt("pending_count") > 0);
                list.add(mesa);
            }

            resultSet.close();
            statement.close();
            return list;
        } finally {
            connection.disconnect();
        }
    }

    private Mesa mapRow(ResultSet resultSet) throws SQLException {
        Mesa mesa = new Mesa();
        mesa.setId(resultSet.getInt("id"));
        mesa.setIdentificador(resultSet.getString("identificador"));
        mesa.setCapacidade(resultSet.getInt("capacidade"));
        mesa.setQuantidadePessoas(resultSet.getInt("quantidade_pessoas"));
        mesa.setOrdemExibicao(resultSet.getInt("ordem_exibicao"));
        return mesa;
    }
}
