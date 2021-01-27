package sample;

import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sample.customExceptions.InvalidAmountException;
import sample.tasks.GetConnectionAndLoadLatestData;

public class App extends Application {

    public static boolean summaryChecked = false;

    public static Stage stage;
    public static Boolean isSplashLoaded = false;

    public static DatabaseManager databaseManager;

    public static ObservableList<DataRow> expenses = FXCollections.observableArrayList();
    public static ObservableList<DataRow> incomes = FXCollections.observableArrayList();

    public static SimpleDoubleProperty balance = new SimpleDoubleProperty();


    @Override
    public void init() throws Exception {
        unit_tests();
        // while loading splash screen - get connection
        databaseManager = new DatabaseManager();
        new Thread(new GetConnectionAndLoadLatestData(summaryChecked)).start();
    }

    @Override
    public void start(Stage stage) throws Exception {
        App.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("/sample/resources/main.fxml"));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/sample/resources/img/dollar_logo.png")));
        stage.setTitle("HomeBudgetApp");
        stage.setScene(new Scene(root));
        App.stage.setMinHeight(600);
        App.stage.setMinWidth(800);
        stage.show();
    }

    @Override
    public void stop() {
        // close connection on application close
        databaseManager.closeConnection();
    }

    public static void main(String[] args) {
        launch(args);
    }


    public static void unit_tests(){
        if(!App.expenses.isEmpty()){
            System.out.println("WARNING: tests ran on non-empty expenses list, contents got cleared\n");
            App.expenses.clear();
        }

        int test_nr=0;


        App.expenses.add(new DataRow(0,0,"",0,0,0,"",""));
        if(App.expenses.get(0).getId()!=0) throw new AssertionError("\n\t\tfailed to load unreasonably clear expenses row\n");
        System.out.println("test "+test_nr+" OK");test_nr++;
        if(App.expenses.get(0).getValue()!=0) throw new AssertionError("\n\t\tfailed to load unreasonably clear expenses row\n");
        System.out.println("test "+test_nr+" OK");test_nr++;
        if(App.expenses.get(0).getAmount()!=0) throw new AssertionError("\n\t\tfailed to load unreasonably clear expenses row\n");
        System.out.println("test "+test_nr+" OK");test_nr++;
        if(!App.expenses.get(0).getUnit().equals("")) throw new AssertionError("\n\t\tfailed to load unreasonably clear expenses row\n");
        System.out.println("test "+test_nr+" OK");test_nr++;
        if(App.expenses.get(0).getYear()!=0) throw new AssertionError("\n\t\tfailed to load unreasonably clear expenses row\n");
        System.out.println("test "+test_nr+" OK");test_nr++;
        if(App.expenses.get(0).getMonth()!=0) throw new AssertionError("\n\t\tfailed to load unreasonably clear expenses row\n");
        System.out.println("test "+test_nr+" OK");test_nr++;
        if(App.expenses.get(0).getDay()!=0) throw new AssertionError("\n\t\tfailed to load unreasonably clear expenses row\n");
        System.out.println("test "+test_nr+" OK");test_nr++;
        if(!App.expenses.get(0).getDate().equals("0/0/0")) throw new AssertionError("\n\t\tfailed to load unreasonably clear expenses row\n");
        System.out.println("test "+test_nr+" OK");test_nr++;
        if(!App.expenses.get(0).getName().equals("")) throw new AssertionError("\n\t\tfailed to load unreasonably clear expenses row\n");
        System.out.println("test "+test_nr+" OK");test_nr++;
        if(!App.expenses.get(0).getCategory().equals("")) throw new AssertionError("\n\t\tfailed to load unreasonably clear expenses row\n");
        System.out.println("test "+test_nr+" OK");test_nr++;
        if(!App.expenses.get(0).toString().equals("Id: 0, Value: 0.0, Amount: 0.0, Unit: , Category: , Name: , Date: 0/0/0")) throw new AssertionError("\n\t\tfailed to load unreasonably clear expenses row\n");
        App.expenses.add(new DataRow(1,1,"resda",1,1,1,"asds","dsad"));
        if(App.expenses.get(1).getId()!=0) throw new AssertionError("\n\t\tfailed to load second expenses row\n");
        System.out.println("test "+test_nr+" OK");test_nr++;
        if(App.expenses.get(1).getValue()!=1) throw new AssertionError("\n\t\tfailed to load second expenses row\n");
        System.out.println("test "+test_nr+" OK");test_nr++;
        if(App.expenses.get(1).getAmount()!=1) throw new AssertionError("\n\t\tfailed to load second expenses row\n");
        System.out.println("test "+test_nr+" OK");test_nr++;
        if(!App.expenses.get(1).getUnit().equals("resda")) throw new AssertionError("\n\t\tfailed to load second expenses row\n");
        System.out.println("test "+test_nr+" OK");test_nr++;
        if(App.expenses.get(1).getYear()!=1) throw new AssertionError("\n\t\tfailed to load second expenses row\n");
        System.out.println("test "+test_nr+" OK");test_nr++;
        if(App.expenses.get(1).getMonth()!=1) throw new AssertionError("\n\t\tfailed to load second expenses row\n");
        System.out.println("test "+test_nr+" OK");test_nr++;
        if(App.expenses.get(1).getDay()!=1) throw new AssertionError("\n\t\tfailed to load second expenses row\n");
        System.out.println("test "+test_nr+" OK");test_nr++;
        if(!App.expenses.get(1).getDate().equals("1/1/1")) throw new AssertionError("\n\t\tfailed to load second expenses row\n");
        System.out.println("test "+test_nr+" OK");test_nr++;
        if(!App.expenses.get(1).getName().equals("asds")) throw new AssertionError("\n\t\tfailed to load second expenses row\n");
        System.out.println("test "+test_nr+" OK");test_nr++;
        if(!App.expenses.get(1).getCategory().equals("dsad")) throw new AssertionError("\n\t\tfailed to load second expenses row\n");
        System.out.println("test "+test_nr+" OK");test_nr++;
        if(!App.expenses.get(1).toString().equals("Id: 0, Value: 1.0, Amount: 1.0, Unit: resda, Category: dsad, Name: asds, Date: 1/1/1")) throw new AssertionError("\n\t\tfailed to load unreasonably clear expenses row\n");
        if(!InputValidation.doName("sedwser").equals("\"sedwser\"")) throw new AssertionError("\n\t\tInputValidation.doName() failed\n");
        System.out.println("test "+test_nr+" OK");test_nr++;
        if(!InputValidation.doName("safer").equals("\"safer\"")) throw new AssertionError("\n\t\tInputValidation.doName() failed\n");
        System.out.println("test "+test_nr+" OK");test_nr++;
        if(!InputValidation.doName("cat").equals("\"cat\"")) throw new AssertionError("\n\t\tInputValidation.doName() failed\n");
        System.out.println("test "+test_nr+" OK");test_nr++;
        if(!InputValidation.doName("").equals("\"\"")) throw new AssertionError("\n\t\tInputValidation.doName() failed\n");
        System.out.println("test "+test_nr+" OK");test_nr++;
        if(!InputValidation.doCategory("ssag").equals("\"ssag\"")) throw new AssertionError("\n\t\tInputValidation.doCategory() failed\n");
        System.out.println("test "+test_nr+" OK");test_nr++;
        if(!InputValidation.doCategory("gyjmyg").equals("\"gyjmyg\"")) throw new AssertionError("\n\t\tInputValidation.doCategory() failed\n");
        System.out.println("test "+test_nr+" OK");test_nr++;
        if(!InputValidation.doCategory("tfgb").equals("\"tfgb\"")) throw new AssertionError("\n\t\tInputValidation.doCategory() failed\n");
        System.out.println("test "+test_nr+" OK");test_nr++;
        try{if(InputValidation.validateAmount("1")!=1.0)throw new AssertionError("\n\t\tInputValidation.validateAmount() failed\n");
        }catch(InvalidAmountException exception){throw new AssertionError("\n\t\tInputValidation.validateAmount() failed\n");}
        System.out.println("test "+test_nr+" OK");test_nr++;
        try{if(InputValidation.validateAmount("3443257")!=3443257) throw new AssertionError("\n\t\tInputValidation.validateAmount() failed\n");
        }catch(InvalidAmountException exception){throw new AssertionError("\n\t\tInputValidation.validateAmount() failed\n");}
        System.out.println("test "+test_nr+" OK");test_nr++;
        try{InputValidation.validateAmount("0"); throw new AssertionError("\n\t\tInputValidation.validateAmount() failed\n");
        }catch(InvalidAmountException ignored){}
        System.out.println("test "+test_nr+" OK");test_nr++;
        try{InputValidation.validateAmount("-1"); throw new AssertionError("\n\t\tInputValidation.validateAmount() failed\n");
        }catch(InvalidAmountException ignored){}
        System.out.println("test "+test_nr+" OK");test_nr=ReceiptLoader.unit_tests(test_nr);
    }
}
