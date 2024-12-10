module org.example.wordsearchgui {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.wordsearchgui to javafx.fxml;
    exports org.example.wordsearchgui;
}