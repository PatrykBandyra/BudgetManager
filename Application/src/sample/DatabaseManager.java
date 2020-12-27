package sample;

import java.sql.*;


public class DatabaseManager {
    public Connection connection = null;

    public DatabaseManager() {
//         registerDriver();
    }

    private void registerDriver() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            System.out.println("Oracle JDBC Controller registered.");
        } catch (ClassNotFoundException ex) {
            System.out.println("No JDBC Controller.");
        }
    }

    public Connection getConnection(String host, String user, String password) {
        try {
            connection = DriverManager.getConnection(host, user, password);
        } catch (SQLException ex) {
            System.out.println("Connection error. Check hostname, username and password.");
        }
        return connection;
    }

    public void testConnection() {
        try {
            if (connection != null) {
                System.out.println("Connected.");
                connection.close();
                System.out.println("Disconnected.");
            } else {
                System.out.println("No connection with database!");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}