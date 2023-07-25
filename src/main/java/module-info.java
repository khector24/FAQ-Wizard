module com.rainbowacehardware.faqwizard {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.rainbowacehardware.faqwizard to javafx.fxml;
    exports com.rainbowacehardware.faqwizard;
}