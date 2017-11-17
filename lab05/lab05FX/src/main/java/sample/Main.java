package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        final Controller controller = new Controller();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/sample.fxml"));
        fxmlLoader.setController(controller);
        Parent root = fxmlLoader.load();
//        Parent root = FXMLLoader.load(getClass().getResource("/fxml/sample.fxml"));
//
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.setOnCloseRequest(event -> {
            controller.exitApplication();
        });
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
