module com.fu.ch08 {
    requires javafx.controls;
    requires javafx.fxml;
    exports com.fu.ch08.b1;
    exports com.fu.ch08.b2;
    exports com.fu.ch08.b3;
    exports com.fu.ch08.b4;
    opens com.fu.ch08.b4 to javafx.fxml;
    opens com.fu.ch08.b5 to javafx.fxml;
    exports com.fu.ch08.b5;
    opens com.fu.ch08.b6 to javafx.fxml;
    exports com.fu.ch08.b6;
    opens com.fu.ch08.t1 to javafx.fxml, javafx.base;
    exports com.fu.ch08.t1;
    exports com.fu.ch08.t2;
    exports com.fu.ch08.t3;
    exports com.fu.ch08.t4;
    requires javafx.web;
    requires javafx.media;
    exports com.fu.ch08.t5;
}

