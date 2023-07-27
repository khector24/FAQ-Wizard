package com.rainbowacehardware.faqwizard.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class HomePageController {
    @FXML
    public Button viewFAQBtn;
    @FXML
    public Button otherBtn;

    @FXML
    public void viewFAQBtnOnAction(ActionEvent event) {
        openPageHelperMethod("/com/rainbowacehardware/faqwizard/FAQ-Screens/View-FAQs.fxml");
    }

    public void openPageHelperMethod(String fxmlFileName){
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            Parent root = loader.load();

            // Create a new stage for the loaded page
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));

            // Close the current stage (associated with the login page view)
            Stage currentStage = (Stage) viewFAQBtn.getScene().getWindow();
            currentStage.close();

            // Show the new stage
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
