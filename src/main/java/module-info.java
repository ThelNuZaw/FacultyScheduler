module s25.cs151.application {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.desktop;

    opens s25.cs151.application to javafx.base;
    //exports s25.cs151.application;
    exports s25.cs151.application to javafx.graphics;
}