module com.fu.ch08 {
    requires javafx.controls;
    requires javafx.fxml;
    exports com.fu.ch08.b1;
    exports com.fu.ch08.b2;
    exports com.fu.ch08.b3;
    exports com.fu.ch08.b4;
    opens com.fu.ch08.b4 to javafx.fxml;
}

