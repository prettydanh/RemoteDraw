module remotedraw.main {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.swing;
    requires org.kordamp.iconli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;
    requires org.kordamp.bootstrapfx.core;
    requires com.jfoenix;
    requires java.desktop;

    opens com.starea to javafx.fxml;
    exports com.starea;
}