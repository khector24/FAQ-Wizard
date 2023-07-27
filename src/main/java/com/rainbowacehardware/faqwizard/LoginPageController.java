package com.rainbowacehardware.faqwizard;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;

public class LoginPageController {
    @FXML
    public TextField usernameTfld;
    @FXML
    public PasswordField passwordTfld;
    @FXML
    public Button loginBtn;
    @FXML
    public Button closeBtn;
    @FXML
    public Label loginMessageLbl;
    @FXML
    public Label copyrightLbl;

    @FXML
    public void initialize() {
        setCurrentYear();
    }

    public void loginBtnOnAction (ActionEvent event) {
        if (!usernameTfld.getText().isBlank() && !passwordTfld.getText().isBlank()) {
            loginMessageLbl.setText("Success!");
            validateLogin();
        } else {
            showAlert("Missing Info!", "Please fill out both the username and password!");
        }
    }

    public void closeBtnOnAction (ActionEvent event) {
        Stage stage = (Stage) closeBtn.getScene().getWindow();
        stage.close();
    }

    public void validateLogin() {

        String userName = usernameTfld.getText();
        String password = passwordTfld.getText();

        String verifyQuery = "SELECT count(1) FROM userAccounts WHERE username = ? AND password = ?";

        try {
            Connection connection = new DatabaseConnection().getConnection();
            PreparedStatement statement = connection.prepareStatement(verifyQuery);
            statement.setString(1, userName);
            statement.setString(2, password);

            ResultSet queryResult = statement.executeQuery();

            while (queryResult.next()) {
                if (queryResult.getInt(1) == 1) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("Home-Page.fxml"));
                        Parent root  = loader.load();

                        // Create a new stage for the home page
                        Stage homePageStage = new Stage();
                        homePageStage.setScene(new Scene(root));

                        // Close the current stage (associated with the login page view)
                        Stage currentStage = (Stage) loginBtn.getScene().getWindow();
                        currentStage.close();

                        // Show the home page stage
                        homePageStage.show();

                    } catch (IOException e){
                        e.printStackTrace();
                    }
                } else {
                    showAlert("Error", "Wrong username or password!");
                }
            }

            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setCurrentYear() {
        // Get the current year dynamically
        int year = Year.now().getValue();
        copyrightLbl.setText("Copyright © " + year + ". All rights reserved.");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
