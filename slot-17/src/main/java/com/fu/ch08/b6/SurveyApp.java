package com.fu.ch08.b6;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class SurveyApp extends Application {
    @Override public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("survey-view.fxml")), 640, 720));
        stage.setTitle("B6 - Khảo sát sinh viên"); stage.setMinWidth(480); stage.setMinHeight(400); stage.show();
    }
    public static void main(String[] args) { launch(args); }
}
