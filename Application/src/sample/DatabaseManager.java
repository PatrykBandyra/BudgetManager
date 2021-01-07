package sample;

import java.sql.*;


public class DatabaseManager {
    public Connection connection = null;

    public DatabaseManager() {
//         registerDriver();
    }

    /**
     * Function to test if JDBC driver is present on our machine
     */
    private void registerDriver() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            System.out.println("Oracle JDBC Controller registered.");
        } catch (ClassNotFoundException ex) {
            System.out.println("No JDBC Controller.");
        }
    }

    public void getConnection(String host, String user, String password) throws SQLException {
        connection = DriverManager.getConnection(host, user, password);
    }

    public void closeConnection() {
        try {
            if (connection != null && connection.isValid(1))
                connection.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
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