package com.example.ui;
import com.example.StudentManagementApplication;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
public class JavaFxApplication extends Application {
 private ConfigurableApplicationContext context;
 @Override public void init(){context=new SpringApplicationBuilder(StudentManagementApplication.class).web(WebApplicationType.NONE).run(getParameters().getRaw().toArray(String[]::new));}
 @Override public void start(Stage stage) throws Exception { FXMLLoader loader=new FXMLLoader(getClass().getResource("/student-view.fxml")); loader.setControllerFactory(context::getBean); Scene scene=new Scene(loader.load(),1050,600); scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm()); stage.setTitle("Student Management System"); stage.setScene(scene); stage.show(); }
 @Override public void stop(){if(context!=null)context.close(); Platform.exit();}
}
