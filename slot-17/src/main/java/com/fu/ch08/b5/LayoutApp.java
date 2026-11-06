package com.fu.ch08.b5;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class LayoutApp extends Application {
    @Override public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("main-layout.fxml"))));
        stage.setTitle("B5 - Layout Gallery");
        stage.setMinWidth(600); stage.setMinHeight(400); stage.show();
    }
    public static void main(String[] args) { launch(args); }
}
