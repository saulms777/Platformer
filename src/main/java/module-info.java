module com.example.platformer {
    requires javafx.controls;
    requires javafx.fxml;
            
        requires org.controlsfx.controls;
                    requires org.kordamp.ikonli.javafx;
            requires org.kordamp.bootstrapfx.core;
    
    opens com.platformer to javafx.fxml;
    exports com.platformer;
}