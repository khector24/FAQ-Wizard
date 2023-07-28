package com.rainbowacehardware.faqwizard.Controllers.FAQScreenControllers;

import com.rainbowacehardware.faqwizard.DatabaseConnection;
import com.rainbowacehardware.faqwizard.HelperMethods.UIControllerHelper;
import com.rainbowacehardware.faqwizard.Objects.FAQ;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class ViewFAQsController implements Initializable {
    @FXML
    public Button addBtn;
    @FXML
    public Button editBtn;
    @FXML
    public Button deleteBtn;
    @FXML
    public Button homeBtn;
    @FXML
    public TableView<FAQ> faqTable;
    @FXML
    public TableColumn<FAQ, Integer> idCol;
    @FXML
    public TableColumn<FAQ, String> questionCol;
    @FXML
    public TableColumn<FAQ, String> answerCol;
    @FXML
    public Button refreshBtn;
    @FXML
    public Label notificationLbl;
    @FXML
    public Button closeBtn;
    @FXML
    public Button pushToWebsiteBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpTableColumns();
        loadDataIntoFAQTable();
    }

    public void setUpTableColumns() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        questionCol.setCellValueFactory(new PropertyValueFactory<>("question"));
        questionCol.setCellFactory(tc -> {
            TableCell<FAQ, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(questionCol.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });
        answerCol.setCellValueFactory(new PropertyValueFactory<>("answer"));
        answerCol.setCellFactory(tc -> {
            TableCell<FAQ, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(answerCol.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });
    }

    public void loadDataIntoFAQTable() {
        ObservableList<FAQ> faqs = FXCollections.observableArrayList();
        Connection connection = new DatabaseConnection().getConnection();

        String loadQuery = "SELECT * FROM faq_table";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(loadQuery)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String question = resultSet.getString("question");
                String answer = resultSet.getString("answer");

                FAQ faq = new FAQ(id, question, answer);
                faqs.add(faq);
            }

            faqTable.setItems(faqs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void addBtnOnAction(ActionEvent event) {
        notificationLbl.setText("");
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/rainbowacehardware/faqwizard/FAQ-Screens/New-FAQ.fxml"));
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

    public void editBtnOnAction(ActionEvent event) {
        notificationLbl.setText("");
        FAQ selectedFAQ = faqTable.getSelectionModel().getSelectedItem();

        if (selectedFAQ != null) {
            if (showConfirmationAlert("Inquiry", "Would you like to edit the question with ID: " + selectedFAQ.getId())) {
                setToBeEditedTrue(selectedFAQ.getId());
                openModalWindowHelperMethod("/com/rainbowacehardware/faqwizard/FAQ-Screens/New-FAQ.fxml", selectedFAQ.getId());
            }
        } else {
            showAlert("Error", "Please select a row to be edited.");
        }
    }

    @FXML
    public void deleteBtnOnAction(ActionEvent event) {
        FAQ selectedFAQ = faqTable.getSelectionModel().getSelectedItem();

        if (selectedFAQ != null) {
            Connection connection = new DatabaseConnection().getConnection();

            String deleteQuery = "DELETE FROM faq_table WHERE id=?";

            try (PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
                statement.setInt(1, selectedFAQ.getId());
                statement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }

            loadDataIntoFAQTable();
            notificationLbl.setText("Item deleted successfully!");
            notificationLbl.setStyle("-fx-text-fill: green;");
        }
    }

    @FXML
    public void homeBtnOnAction (ActionEvent event) {
        openPageHelperMethod("/com/rainbowacehardware/faqwizard/Home-Page.fxml");
    }

    public void openPageHelperMethod(String fxmlFileName){
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            Parent root = loader.load();

            // Create a new stage for the loaded page
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));

            //Set the new stage style
            newStage.initStyle(StageStyle.UNDECORATED);

            // Close the current stage
            Stage currentStage = (Stage) homeBtn.getScene().getWindow();
            currentStage.close();

            // Show the new stage
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openModalWindowHelperMethod(String fxmlFileName, int faqId) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            Parent root = loader.load();

            // Get the controller for the "Add/Edit FAQ" page
            NewFAQController newFaqController = loader.getController();

            // Pass the selected FAQ's ID to the "Add/Edit FAQ" page
            newFaqController.setFaqIdToEdit(faqId);

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

    @FXML
    public void refreshBtnOnAction(ActionEvent event) {
        loadDataIntoFAQTable();

        notificationLbl.setText("The page was refreshed successfully!");
        notificationLbl.setStyle("-fx-text-fill: green;");
    }

    @FXML
    public void closeBtnOnAction(ActionEvent event) {
        UIControllerHelper.closeWindow(event);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean showConfirmationAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Set custom buttons for the alert
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");
        alert.getButtonTypes().setAll(yesButton, noButton);

        // Show the alert and wait for user response
        Optional<ButtonType> result = alert.showAndWait();

        return result.isPresent() && result.get() == yesButton;
    }

    private void setToBeEditedTrue(int faqId) {
        Connection connection = new DatabaseConnection().getConnection();

        String updateQuery = "UPDATE faq_table SET toBeEdited = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(updateQuery)) {
            statement.setBoolean(1, true);
            statement.setInt(2, faqId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
