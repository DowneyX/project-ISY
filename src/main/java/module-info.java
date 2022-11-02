module isy.team4.projectisy {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens isy.team4.projectisy to javafx.fxml;

    exports isy.team4.projectisy;
}