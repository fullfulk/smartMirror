package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("viewMarkUp.fxml"));
            BorderPane root = (BorderPane) loader.load();
            root.setStyle("-fx-background-color: whitesmoke;");
            Scene scene = new Scene(root, 800, 600);
            primaryStage.setTitle("Face Detection and Tracking");
            primaryStage.setScene(scene);
            primaryStage.show();
            final MainController controller = loader.getController();
            controller.init();
            primaryStage.setOnCloseRequest((new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    controller.setClosed();
                }
            }));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
