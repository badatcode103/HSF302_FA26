package com.example.ui.controller;
import com.example.data.pojo.Student;
import com.example.data.exception.ValidationException;
import com.example.data.exception.StudentNotFoundException;
import com.example.data.service.StudentService;
import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import org.springframework.stereotype.Component;
@Component public class StudentController {
 @FXML private TableView<Student> tableView;
 @FXML private TextField searchField,idField,nameField,emailField,ageField;
 @FXML private Button btnUpdate,btnDelete;
 @FXML private Label messageLabel,statusLabel;
 private final StudentService studentService;
 public StudentController(StudentService studentService){this.studentService=studentService;}
 private final ObservableList<Student> data=FXCollections.observableArrayList();
 @FXML public void initialize(){tableView.setItems(data); tableView.getSelectionModel().selectedItemProperty().addListener((obs,oldValue,student)->{if(student!=null)fillForm(student);}); btnUpdate.disableProperty().bind(tableView.getSelectionModel().selectedItemProperty().isNull()); btnDelete.disableProperty().bind(tableView.getSelectionModel().selectedItemProperty().isNull()); loadData();}
 private void loadData(){try {data.setAll(studentService.findAll()); statusLabel.setText("Loaded " + data.size() + " students");} catch(Exception e){showError("Cannot load data: " + e.getMessage()); statusLabel.setText("Database error");}}
 @FXML private void handleSearch(){try {data.setAll(studentService.search(searchField.getText())); statusLabel.setText("Found " + data.size() + " students");} catch(Exception e){showError("Cannot load data: " + e.getMessage()); statusLabel.setText("Database error");}}
 @FXML private void handleRefresh(){searchField.clear(); loadData();}
 private void fillForm(Student s){idField.setText(String.valueOf(s.getId())); nameField.setText(s.getName()); emailField.setText(s.getEmail()); ageField.setText(String.valueOf(s.getAge()));}
 private void showInfo(String message){messageLabel.setStyle("-fx-text-fill: green;"); messageLabel.setText(message);}
 private void showError(String message){messageLabel.setStyle("-fx-text-fill: red;"); messageLabel.setText(message);}
 private Student readForm(){String age=ageField.getText().trim(); if(age.isEmpty())throw new ValidationException("Age is required"); try{return new Student(nameField.getText(),emailField.getText(),Integer.valueOf(age));}catch(NumberFormatException e){throw new ValidationException("Age must be a number");}}
 @FXML private void handleAdd(){try {Student s=studentService.create(readForm()); handleClear(); loadData(); showInfo("Added: " + s.getName());}catch(ValidationException | StudentNotFoundException e){showError(e.getMessage());}catch(Exception e){showError("System error: " + e.getMessage());}}
 @FXML private void handleUpdate(){try {Student selected=tableView.getSelectionModel().getSelectedItem(); if(selected==null)return; Student edit=readForm(); edit.setId(selected.getId()); Student s=studentService.update(edit); handleClear(); loadData(); showInfo("Updated: " + s.getName());}catch(ValidationException | StudentNotFoundException e){showError(e.getMessage());}catch(Exception e){showError("System error: " + e.getMessage());}}
 @FXML private void handleDelete(){try {Student selected=tableView.getSelectionModel().getSelectedItem(); if(selected==null)return; Alert confirm=new Alert(Alert.AlertType.CONFIRMATION,"Delete student \"" + selected.getName() + "\"?",ButtonType.OK,ButtonType.CANCEL); if(confirm.showAndWait().orElse(ButtonType.CANCEL)!=ButtonType.OK)return; studentService.delete(selected.getId()); String name=selected.getName(); handleClear(); loadData(); showInfo("Deleted: " + name);}catch(ValidationException | StudentNotFoundException e){showError(e.getMessage());}catch(Exception e){showError("System error: " + e.getMessage());}}
 @FXML private void handleClear(){tableView.getSelectionModel().clearSelection(); idField.clear(); nameField.clear(); emailField.clear(); ageField.clear(); nameField.requestFocus();}
}
