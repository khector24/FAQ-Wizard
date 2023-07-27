package com.rainbowacehardware.faqwizard.controllers.FAQScreenControllers;

import com.rainbowacehardware.faqwizard.DatabaseConnection;
import com.rainbowacehardware.faqwizard.objects.FAQ;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NewFAQController {
    @FXML
    public TextField questionTfld;
    @FXML
    public TextArea answerTextArea;
    @FXML
    public Button addBtn;
    @FXML
    public Button updateBtn;
    @FXML
    public Button deleteBtn;
    @FXML
    public Button clearBtn;
    @FXML
    public Button homeBtn;
    @FXML
    public Button FAQBtn;
    @FXML
    public Button closeBtn;

    public void addBtnOnAction(ActionEvent event) {
        String question = questionTfld.getText();
        String answer = answerTextArea.getText();

        if (question.isEmpty() || answer.isEmpty()) {
            showAlert("Error", "Please enter both question and answer.");
            return;
        }

        try {
            Connection connection = new DatabaseConnection().getConnection();
            String query = "INSERT INTO faq_table (question, answer) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, question);
            preparedStatement.setString(2, answer);
            preparedStatement.executeUpdate();
            showAlert("Success", "FAQ added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to add FAQ to the database.");
        }

        clear();
    }

    public void updateBtnOnAction (ActionEvent event) {
        openModalWindowHelperMethod("");
    }

    public void deleteBtnOnAction(ActionEvent event) {
        // Implement the delete logic here
        // You can use a similar approach to the "Add" method
    }

    public void clearBtnOnAction(ActionEvent event) {
        clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void closeBtnOnAction(ActionEvent event) {
        Stage stage = (Stage) closeBtn.getScene().getWindow();
        stage.close();
    }

    public void clear() {
        questionTfld.clear();
        answerTextArea.clear();
    }

    public void openModalWindowHelperMethod(String fxmlFileName) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            Parent root = loader.load();

            // Create a new stage for the modal window
            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setScene(new Scene(root));

            // Show the modal window and wait for user interaction
            modalStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

