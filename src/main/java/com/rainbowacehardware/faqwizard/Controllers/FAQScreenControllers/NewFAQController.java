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

    public void setFaqIdToEdit(int faqId) {
        this.faqIdToEdit = faqId;
        idLabel.setText("ID: " + faqIdToEdit); // Update the idLabel text with the selected faqId
        loadFAQData(); // Load the data for the selected FAQ (if it exists)
    }

    private void loadFAQData() {
        // Load the data for the selected FAQ and populate the questionTfld and answerTextArea
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


    public void addBtnOnAction(ActionEvent event) {
        String question = questionTfld.getText();
        String answer = answerTextArea.getText();

        if (question.isEmpty() || answer.isEmpty()) {
            showAlert("Error", "Please enter both question and answer.");
            return;
        }

        try(Connection connection = new DatabaseConnection().getConnection();) {
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
        // Set the idLabel text to the updated faqIdToEdit
        idLabel.setText("ID: " + faqIdToEdit);
        int id = faqIdToEdit;

        String question = questionTfld.getText();
        String answer = answerTextArea.getText();

        if (question.isEmpty() || answer.isEmpty()) {
            showAlert("Error", "Missing Selected item.");
            return;
        }

        try(Connection connection = new DatabaseConnection().getConnection();) {
            String query = "UPDATE faq_table SET question = ?, answer = ?, toBeEdited = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, question);
            preparedStatement.setString(2, answer);
            preparedStatement.setBoolean(3, false);
            preparedStatement.setInt(4, id);
            preparedStatement.executeUpdate();
            showAlert("Success", "FAQ updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to update FAQ in the database.");
        }

        clear();
    }

    @FXML
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
        UIControllerHelper.closeWindow(event);
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
            modalStage.initStyle(StageStyle.UNDECORATED);

            // Show the modal window and wait for user interaction
            modalStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

