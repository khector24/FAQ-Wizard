module com.rainbowacehardware.faqwizard {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;


    opens com.rainbowacehardware.faqwizard to javafx.fxml;
    exports com.rainbowacehardware.faqwizard;
}