package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import sample.database.Database;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    Database db;

    public void initialize(URL location, ResourceBundle resources) {
        db = new Database("nowa-baza");
    }

    @FXML
    void createTables() {
        System.out.println("sad");
        db.createTables();
    }

    public void exitApplication(){
        db.closeConnection();
        System.exit(0);
    }
}
