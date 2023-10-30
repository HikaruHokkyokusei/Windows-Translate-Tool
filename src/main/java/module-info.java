module org.hentai_productions {
    requires javafx.controls;
    requires FXTrayIcon;
    requires javafx.fxml;
    requires system.hook;
    requires java.google.speech.api;

    opens org.hentai_productions to javafx.fxml;
    exports org.hentai_productions;
}