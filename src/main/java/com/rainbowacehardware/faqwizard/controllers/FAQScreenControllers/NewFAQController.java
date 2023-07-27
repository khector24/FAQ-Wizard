package com.rainbowacehardware.faqwizard.controllers.FAQScreenControllers;

import com.rainbowacehardware.faqwizard.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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

    public void Add(ActionEvent event) {
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
    }

    public void Update(ActionEvent event) {
        // Implement the update logic here
        // You can use a similar approach to the "Add" method
    }

    public void Delete(ActionEvent event) {
        // Implement the delete logic here
        // You can use a similar approach to the "Add" method
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

