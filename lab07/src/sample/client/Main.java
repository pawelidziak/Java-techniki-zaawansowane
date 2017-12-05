package sample.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    static Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
//        primaryStage.setTitle("Hello World");
//        primaryStage.setScene(new Scene(root));
//        primaryStage.show();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sample.fxml"));
        controller = new Controller();
        fxmlLoader.setController(controller);
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("JMX - lab07. Paweł Idziak");
        primaryStage.setScene(new Scene(root));
        primaryStage.setOnCloseRequest(event -> {
//            controller.exitApplication();
        });
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
