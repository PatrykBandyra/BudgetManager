package sample.controllers;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import sample.OraConnTest;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Controller {
    public Button queryB = null;
    public Label resultLabel = null;

    public void onMouseClicked(MouseEvent mouseEvent) {
        OraConnTest test = new OraConnTest();
        test.getConnection("jdbc:oracle:thin:@ora4.ii.pw.edu.pl:1521/pdb1.ii.pw.edu.pl", "pbandyra", "pbandyra");
        final String sql = "SELECT * FROM (SELECT * FROM HR.employees e WHERE e.salary = (SELECT MAX(salary) FROM HR.employees) ORDER BY employee_id ASC) WHERE ROWNUM = 1";
        try {
            final Statement statement = test.connection.createStatement();
            ResultSet results = statement.executeQuery(sql);
            while (results.next()){
                resultLabel.setText("ID: "+results.getString(1)+", "+"First Name: "+results.getString(2)+", "+"Second Name: "+results.getString(3)
                +", "+"Salary: "+results.getString(8));
            }
        } catch (Exception e) {
            resultLabel.setText("Connection error");
            e.printStackTrace();
        }
    }
}
