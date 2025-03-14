module com.br {
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.br.controller to javafx.fxml;
    opens com.br.entity to javafx.base;
    opens com.br to javafx.fxml;
    exports com.br;
    exports com.br.connections;
    exports com.br.entity;
    opens com.br.connections to javafx.fxml;
}
