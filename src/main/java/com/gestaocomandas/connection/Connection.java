package com.gestaocomandas.connection;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Connection {

    private final String database;
    private final String user;
    private final String password;
    private java.sql.Connection connection;

    public Connection() {
        this.database = "gestao_comandas";
        this.user = "root";
        this.password = "";
        this.connection = null;
    }

    public java.sql.Connection connect() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost/" + database, user, password);
            return connection;
        } catch (ClassNotFoundException exception) {
            System.out.println("Erro na conexão: " + exception);
            return null;
        }
    }

    public void disconnect() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException exception) {
            System.out.println("Erro ao encerrar a conexão: " + exception);
        }
    }
}
