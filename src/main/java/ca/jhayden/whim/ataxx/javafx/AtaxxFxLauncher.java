package ca.jhayden.whim.ataxx.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AtaxxFxLauncher extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/jhayden/whim/ataxx/javafx/main.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 500, 600);
        
        primaryStage.setTitle("ATAXX - JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}