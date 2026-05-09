package com.gestaocomandas.dao;

import com.gestaocomandas.connection.Connection;
import com.gestaocomandas.vo.Pedido;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    private final Connection connection;

    public PedidoDAO() {
        this.connection = new Connection();
    }

    public int insert(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO pedidos (mesa_id) VALUES (?)";
        java.sql.Connection sqlConnection = connection.connect();

        try {
            PreparedStatement statement = sqlConnection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, pedido.getMesaId());
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            int generatedId = 0;
            if (generatedKeys.next()) {
                generatedId = generatedKeys.getInt(1);
            }

            generatedKeys.close();
            statement.close();
            return generatedId;
        } finally {
            connection.disconnect();
        }
    }

    public void updateStatus(int id, String status) throws SQLException {
        String sql = "UPDATE pedidos SET status = ? WHERE id = ?";
        java.sql.Connection sqlConnection = connection.connect();

        try {
            PreparedStatement statement = sqlConnection.prepareStatement(sql);
            statement.setString(1, status);
            statement.setInt(2, id);
            statement.executeUpdate();
            statement.close();
        } finally {
            connection.disconnect();
        }
    }

    public Pedido findById(int id) throws SQLException {
        String sql = "SELECT * FROM pedidos WHERE id = ?";
        java.sql.Connection sqlConnection = connection.connect();

        try {
            PreparedStatement statement = sqlConnection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            Pedido pedido = null;
            if (resultSet.next()) {
                pedido = mapRow(resultSet);
            }

            resultSet.close();
            statement.close();
            return pedido;
        } finally {
            connection.disconnect();
        }
    }

    public List<Pedido> findPendingByMesa(int mesaId) throws SQLException {
        String sql = "SELECT * FROM pedidos WHERE mesa_id = ? AND status = 'PENDENTE' ORDER BY criado_em";
        java.sql.Connection sqlConnection = connection.connect();

        try {
            PreparedStatement statement = sqlConnection.prepareStatement(sql);
            statement.setInt(1, mesaId);
            ResultSet resultSet = statement.executeQuery();

            List<Pedido> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(mapRow(resultSet));
            }

            resultSet.close();
            statement.close();
            return list;
        } finally {
            connection.disconnect();
        }
    }

    public boolean hasPendingOrders(int mesaId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM pedidos WHERE mesa_id = ? AND status = 'PENDENTE'";
        java.sql.Connection sqlConnection = connection.connect();

        try {
            PreparedStatement statement = sqlConnection.prepareStatement(sql);
            statement.setInt(1, mesaId);
            ResultSet resultSet = statement.executeQuery();

            boolean hasPending = false;
            if (resultSet.next()) {
                hasPending = resultSet.getInt(1) > 0;
            }

            resultSet.close();
            statement.close();
            return hasPending;
        } finally {
            connection.disconnect();
        }
    }

    private Pedido mapRow(ResultSet resultSet) throws SQLException {
        Pedido pedido = new Pedido();
        pedido.setId(resultSet.getInt("id"));
        pedido.setMesaId(resultSet.getInt("mesa_id"));
        pedido.setStatus(resultSet.getString("status"));

        Timestamp criadoEm = resultSet.getTimestamp("criado_em");
        if (criadoEm != null) {
            pedido.setCriadoEm(criadoEm.toLocalDateTime());
        }

        Timestamp atualizadoEm = resultSet.getTimestamp("atualizado_em");
        if (atualizadoEm != null) {
            pedido.setAtualizadoEm(atualizadoEm.toLocalDateTime());
        }

        return pedido;
    }
}
