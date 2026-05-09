package com.gestaocomandas.dao;

import com.gestaocomandas.connection.Connection;
import com.gestaocomandas.vo.Produto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    private final Connection connection;

    public ProdutoDAO() {
        this.connection = new Connection();
    }

    public void insert(Produto produto) throws SQLException {
        String sql = "INSERT INTO produtos (nome, preco, descricao) VALUES (?, ?, ?)";
        java.sql.Connection sqlConnection = connection.connect();

        try {
            PreparedStatement statement = sqlConnection.prepareStatement(sql);
            statement.setString(1, produto.getNome());
            statement.setDouble(2, produto.getPreco());
            statement.setString(3, produto.getDescricao());
            statement.executeUpdate();
            statement.close();
        } finally {
            connection.disconnect();
        }
    }

    public void update(Produto produto) throws SQLException {
        String sql = "UPDATE produtos SET nome = ?, preco = ?, descricao = ? WHERE id = ?";
        java.sql.Connection sqlConnection = connection.connect();

        try {
            PreparedStatement statement = sqlConnection.prepareStatement(sql);
            statement.setString(1, produto.getNome());
            statement.setDouble(2, produto.getPreco());
            statement.setString(3, produto.getDescricao());
            statement.setInt(4, produto.getId());
            statement.executeUpdate();
            statement.close();
        } finally {
            connection.disconnect();
        }
    }

    public void softDelete(int id) throws SQLException {
        String sql = "UPDATE produtos SET ativo = FALSE WHERE id = ?";
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

    public void reactivate(int id) throws SQLException {
        String sql = "UPDATE produtos SET ativo = TRUE WHERE id = ?";
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

    public Produto findById(int id) throws SQLException {
        String sql = "SELECT * FROM produtos WHERE id = ?";
        java.sql.Connection sqlConnection = connection.connect();

        try {
            PreparedStatement statement = sqlConnection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            Produto produto = null;
            if (resultSet.next()) {
                produto = mapRow(resultSet);
            }

            resultSet.close();
            statement.close();
            return produto;
        } finally {
            connection.disconnect();
        }
    }

    public List<Produto> findAll() throws SQLException {
        String sql = "SELECT * FROM produtos ORDER BY nome";
        java.sql.Connection sqlConnection = connection.connect();

        try {
            PreparedStatement statement = sqlConnection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            List<Produto> list = new ArrayList<>();
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

    public List<Produto> findActive() throws SQLException {
        String sql = "SELECT * FROM produtos WHERE ativo = TRUE ORDER BY nome";
        java.sql.Connection sqlConnection = connection.connect();

        try {
            PreparedStatement statement = sqlConnection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            List<Produto> list = new ArrayList<>();
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

    private Produto mapRow(ResultSet resultSet) throws SQLException {
        Produto produto = new Produto();
        produto.setId(resultSet.getInt("id"));
        produto.setNome(resultSet.getString("nome"));
        produto.setPreco(resultSet.getDouble("preco"));
        produto.setDescricao(resultSet.getString("descricao"));
        produto.setAtivo(resultSet.getBoolean("ativo"));
        return produto;
    }
}
