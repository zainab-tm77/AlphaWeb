module AlphaWeb_GUI {
    requires javafx.swt;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.swing;
    requires javafx.web;
    
    opens AlphaWeb.screens to javafx.graphics;
}
