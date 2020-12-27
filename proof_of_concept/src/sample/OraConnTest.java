package sample;

import java.sql.*;


public class OraConnTest {
    public Connection connection = null;

    public OraConnTest() {
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

    public static void main(String[] args) {
        DatabaseManager test = new DatabaseManager();
        test.getConnection("jdbc:oracle:thin:@ora4.ii.pw.edu.pl:1521/pdb1.ii.pw.edu.pl", "pbandyra", "pbandyra");
        final String sql = "SELECT * FROM employees e WHERE e.salary = (SELECT MAX(salary) FROM employees) ORDER BY employee_id ASC";
        try {
            final Statement statement = test.connection.createStatement();
            ResultSet results = statement.executeQuery(sql);
            while (results.next()){
                System.out.println(results.getString(1) + "\t" + results.getString(2) + "\t" + results.getString(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}