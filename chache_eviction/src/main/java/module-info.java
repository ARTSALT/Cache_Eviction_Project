module com.br {
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;

    opens com.br.controller to javafx.fxml;
    opens com.br.entity to javafx.base;
    opens com.br to javafx.fxml;
    exports com.br;
}
