package com.rainbowacehardware.faqwizard.Controllers.FAQScreenControllers;

import com.rainbowacehardware.faqwizard.DatabaseConnection;
import com.rainbowacehardware.faqwizard.HelperMethods.UIControllerHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    public Button clearBtn;
    @FXML
    public Button closeBtn;
    @FXML
    public Label idLabel;

    private int faqIdToEdit;

    // Set the ID of the FAQ to edit
    public void setFaqIdToEdit(int faqId) {
        this.faqIdToEdit = faqId;
        idLabel.setText("ID: " + faqIdToEdit); // Update the idLabel text with the selected faqId
        loadFAQData(); // Load the data for the selected FAQ (if it exists)
    }

    // Load the data for the selected FAQ and populate the questionTfld and answerTextArea
    private void loadFAQData() {
        try (Connection connection = new DatabaseConnection().getConnection();) {
            String query = "SELECT * FROM faq_table WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, faqIdToEdit);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String question = resultSet.getString("question");
                String answer = resultSet.getString("answer");
                questionTfld.setText(question);
                answerTextArea.setText(answer);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load FAQ data from the database.");
        }
    }

    // Handle the add button action to insert a new FAQ into the database
    public void addBtnOnAction(ActionEvent event) {
        // Get the question and answer from the input fields
        String question = questionTfld.getText();
        String answer = answerTextArea.getText();

        // Check if the input fields are empty
        if (question.isEmpty() || answer.isEmpty()) {
            showAlert("Error", "Please enter both question and answer.");
            return;
        }

        try (Connection connection = new DatabaseConnection().getConnection();) {
            // Prepare the insert query
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

        clear(); // Clear the input fields after adding the FAQ
    }

    // Handle the update button action to update an existing FAQ in the database
    public void updateBtnOnAction(ActionEvent event) {
        // Set the idLabel text to the updated faqIdToEdit
        idLabel.setText("ID: " + faqIdToEdit);
        int id = faqIdToEdit;

        // Get the question and answer from the input fields
        String question = questionTfld.getText();
        String answer = answerTextArea.getText();

        // Check if the input fields are empty
        if (question.isEmpty() || answer.isEmpty()) {
            showAlert("Error", "Missing Selected item.");
            return;
        }

        try (Connection connection = new DatabaseConnection().getConnection();) {
            // Prepare the update query
            String query = "UPDATE faq_table SET question = ?, answer = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, question);
            preparedStatement.setString(2, answer);
            preparedStatement.setInt(3, id);
            preparedStatement.executeUpdate();
            showAlert("Success", "FAQ updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to update FAQ in the database.");
        }

        clear(); // Clear the input fields after updating the FAQ
    }

    // Handle the clear button action to clear the input fields
    @FXML
    public void clearBtnOnAction(ActionEvent event) {
        clear();
    }

    // Display an alert dialog with the given title and message
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Handle the close button action to close the window
    @FXML
    public void closeBtnOnAction(ActionEvent event) {
        UIControllerHelper.closeWindow(event);
    }

    // Clear the input fields
    public void clear() {
        questionTfld.clear();
        answerTextArea.clear();
    }

    // Open a modal window with the given FXML file name
    public void openModalWindowHelperMethod(String fxmlFileName) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            Parent root = loader.load();

            // Create a new stage for the modal window
            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setScene(new Scene(root));
            modalStage.initStyle(StageStyle.UNDECORATED);

            // Show the modal window and wait for user interaction
            modalStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


