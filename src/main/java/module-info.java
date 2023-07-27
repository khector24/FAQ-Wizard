module com.rainbowacehardware.faqwizard {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.sql;
    requires org.xerial.sqlitejdbc;


    opens com.rainbowacehardware.faqwizard to javafx.fxml;
    exports com.rainbowacehardware.faqwizard;
    exports com.rainbowacehardware.faqwizard.controllers;
    exports com.rainbowacehardware.faqwizard.controllers.FAQScreenControllers;
    exports com.rainbowacehardware.faqwizard.objects;
}