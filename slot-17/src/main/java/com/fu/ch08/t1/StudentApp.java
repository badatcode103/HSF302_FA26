package com.fu.ch08.t1;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class StudentApp extends Application {
    @Override public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("student-view.fxml"))));
        stage.setTitle("T1 - Quản lý sinh viên"); stage.setMinWidth(700); stage.setMinHeight(420); stage.show();
    }
    public static void main(String[] args) { launch(args); }
}
