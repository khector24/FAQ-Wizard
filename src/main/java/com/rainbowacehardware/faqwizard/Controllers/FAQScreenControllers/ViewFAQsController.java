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
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
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
        openPageHelperMethod("/com/rainbowacehardware/faqwizard/FAQ-Screens/New-FAQ.fxml");
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
        // Close the current stage
        Stage currentStage = (Stage) homeBtn.getScene().getWindow();
        currentStage.close();
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

            // Show the new stage
            newStage.show();
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

    public void editBtnOnAction(ActionEvent event) {
    }

    @FXML
    public void closeBtnOnAction(ActionEvent event) {
        UIControllerHelper.closeWindow(event);
    }
}
