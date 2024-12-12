module org.example.weathr {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires org.json;


    opens org.example.weathr to javafx.fxml;
    exports org.example.weathr;
    exports org.example.weathr.View;
    opens org.example.weathr.View to javafx.fxml;
}