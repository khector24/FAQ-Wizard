package com.rainbowacehardware.faqwizard.HelperMethods;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class UIControllerHelper {
    public static void closeWindow(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
