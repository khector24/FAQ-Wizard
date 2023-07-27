package com.rainbowacehardware.faqwizard.Controllers;

import com.rainbowacehardware.faqwizard.HelperMethods.UIControllerHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class HomePageController {
    @FXML
    public Button viewFAQBtn;
    @FXML
    public Button otherBtn;
    public Button logoutBtn;
    public Button closeBtn;

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

            //Set the new stage style
            newStage.initStyle(StageStyle.UNDECORATED);

            // Show the new stage
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void logoutBtnOnAction(ActionEvent event) {
        try {
            // Load the login page FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/rainbowacehardware/faqwizard/Login-Page.fxml"));
            Parent root = loader.load();

            // Create a new stage for the login page
            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(root));
            loginStage.initStyle(StageStyle.UNDECORATED);

            // Close the current stage (associated with the home page view)
            Stage currentStage = (Stage) logoutBtn.getScene().getWindow();
            currentStage.close();

            // Show the login page stage
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void closeBtnOnAction(ActionEvent event) {
        UIControllerHelper.closeWindow(event);
    }
}
