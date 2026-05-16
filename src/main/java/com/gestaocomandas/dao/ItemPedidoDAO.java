package com.gestaocomandas.dao;

import com.gestaocomandas.connection.Connection;
import com.gestaocomandas.vo.ItemPedido;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemPedidoDAO {

    private final Connection connection;

    public ItemPedidoDAO() {
        this.connection = new Connection();
    }

    public void insert(ItemPedido item) throws SQLException {
        String sql = "INSERT INTO itens_pedido (pedido_id, produto_id, quantidade, preco_unitario) VALUES (?, ?, ?, ?)";
        java.sql.Connection sqlConnection = connection.connect();

        try {
            PreparedStatement statement = sqlConnection.prepareStatement(sql);
            statement.setInt(1, item.getPedidoId());
            statement.setInt(2, item.getProdutoId());
            statement.setInt(3, item.getQuantidade());
            statement.setDouble(4, item.getPrecoUnitario());
            statement.executeUpdate();
            statement.close();
        } finally {
            connection.disconnect();
        }
    }

    public void updateQuantity(int id, int quantidade) throws SQLException {
        String sql = "UPDATE itens_pedido SET quantidade = ? WHERE id = ?";
        java.sql.Connection sqlConnection = connection.connect();

        try {
            PreparedStatement statement = sqlConnection.prepareStatement(sql);
            statement.setInt(1, quantidade);
            statement.setInt(2, id);
            statement.executeUpdate();
            statement.close();
        } finally {
            connection.disconnect();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM itens_pedido WHERE id = ?";
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


    public ItemPedido findByOrderAndProduct(int pedidoId, int produtoId) throws SQLException {
        String sql = "SELECT ip.*, p.nome AS nome_produto FROM itens_pedido ip " +
                "JOIN produtos p ON ip.produto_id = p.id " +
                "WHERE ip.pedido_id = ? AND ip.produto_id = ?";
        java.sql.Connection sqlConnection = connection.connect();

        try {
            PreparedStatement statement = sqlConnection.prepareStatement(sql);
            statement.setInt(1, pedidoId);
            statement.setInt(2, produtoId);
            ResultSet resultSet = statement.executeQuery();

            ItemPedido item = null;
            if (resultSet.next()) {
                item = mapRow(resultSet);
            }

            resultSet.close();
            statement.close();
            return item;
        } finally {
            connection.disconnect();
        }
    }

    public List<ItemPedido> findByOrder(int pedidoId) throws SQLException {
        String sql = "SELECT ip.*, p.nome AS nome_produto FROM itens_pedido ip " +
                "JOIN produtos p ON ip.produto_id = p.id " +
                "WHERE ip.pedido_id = ? ORDER BY p.nome";
        java.sql.Connection sqlConnection = connection.connect();

        try {
            PreparedStatement statement = sqlConnection.prepareStatement(sql);
            statement.setInt(1, pedidoId);
            ResultSet resultSet = statement.executeQuery();

            List<ItemPedido> list = new ArrayList<>();
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

    private ItemPedido mapRow(ResultSet resultSet) throws SQLException {
        ItemPedido item = new ItemPedido();
        item.setId(resultSet.getInt("id"));
        item.setPedidoId(resultSet.getInt("pedido_id"));
        item.setProdutoId(resultSet.getInt("produto_id"));
        item.setQuantidade(resultSet.getInt("quantidade"));
        item.setPrecoUnitario(resultSet.getDouble("preco_unitario"));
        item.setNomeProduto(resultSet.getString("nome_produto"));
        return item;
    }
}
