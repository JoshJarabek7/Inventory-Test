package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.InHouse;
import models.Inventory;
import models.Outsource;
import models.Part;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

// Creates a new instance of the AddPart class
public class AddPart implements Initializable {


    // The functions for the "Add Part" screen

    @FXML
    private AnchorPane IHRadio;
    @FXML
    private Label aLabel;
    @FXML
    private TextField aLabelText;
    @FXML
    private TextField partIdText;
    @FXML
    private TextField partNameText;
    @FXML
    private TextField partInvText;
    @FXML
    private TextField partPriceText;
    @FXML
    private TextField partMaxText;
    @FXML
    private TextField partMinText;
    @FXML
    private RadioButton aIHRadio;
    @FXML
    private ToggleGroup SourceToggle;
    @FXML
    private RadioButton aOSRadio;
    @FXML
    private Button aSaveButton;
    @FXML
    private Button aCancelButton;


    /*
    The method for what happens when each radio button is clicked

    If the In-House radio button is clicked:
    The Label is set to "Machine ID" and the Prompt Text in the Text Field is set to "Mach ID"

    If the OutSourced radio button is clicked:
    The Label is set to "Company Name" and the Prompt Text in the Text Field is set to "Company Name"
     */
    @FXML
    private void sourceToggle(ActionEvent event) {

        // In-House Button is clicked
        if (aIHRadio.isSelected()) {
            aLabelText.setVisible(true);
            aLabel.setText("Machine ID");
            aLabelText.setPromptText("Mach ID");
        }

        // Outsourced Button is clicked
        else if (aOSRadio.isSelected()) {
            aLabel.setText("Company Name");
            aLabelText.setPromptText("Company Name");

        }
    }

    /*
    The method for what happens when the "Save" button is clicked

    Error alerts set in place for:
    1. The part's maximum inventory entered is less than the part's minimum inventory entered.
    2. The part's current inventory entered is greater than the part's maximum inventory entered.
    3. The part's current inventory entered is less than the part's minimum inventory entered.
    4. User did not select a part's source (In-House or Outsourced).
    5. Values entered are not valid (ex. Text was entered in an integer only field).

    If the In-House radio button is clicked, the corresponding fields are loaded for In-House parts.
    If the Outsourced radio button is clicked, the corresponding fields are loaded for Outsourced parts.

    If no alerts are triggered after clicking save, the parts are then saved and the user is returned to the Main Screen.
     */
    @FXML
    private void onActionSave(ActionEvent event) throws IOException {

        try {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");

            // Maximum inventory entered is less than the minimum inventory entered
            if (Integer.parseInt(partMaxText.getText()) < Integer.parseInt(partMinText.getText())) {
                alert.setContentText("Max value must be greater than the minimum");
                alert.showAndWait();
                return;
            }
            // Current inventory entered is greater than the maximum inventory entered
            if (Integer.parseInt(partInvText.getText()) > Integer.parseInt(partMaxText.getText())) {
                alert.setContentText("Inventory value must be less than the maximum");
                alert.showAndWait();
                return;
            }
            // Current inventory entered is less than the minimum inventory entered
            if (Integer.parseInt(partInvText.getText()) < Integer.parseInt(partMinText.getText())) {
                alert.setContentText("Inventory value must be greater than the minimum");
                alert.showAndWait();
                return;
            }
            // User must choose if the part is sourced In-House or if it's Outsourced
            if (!aIHRadio.isSelected() && !aOSRadio.isSelected()) {
                alert.setContentText("Please click either InHouse or Outsourced.");
                alert.showAndWait();
                return;
            }
            // If In-House is clicked and no errors are thrown, the part is added to the inventory
            if (aIHRadio.isSelected()) {
                Part p = new InHouse(

                        Inventory.partIdCounter(),                      // Part ID Field (Auto-Generated)
                        partNameText.getText(),                         // Part Name Field
                        Double.parseDouble(partPriceText.getText()),    // Part Price/CPU Field
                        Integer.parseInt(partInvText.getText()),        // Part Inventory Field
                        Integer.parseInt(partMinText.getText()),        // Part Minimum Inv Field
                        Integer.parseInt(partMaxText.getText()),        // Part Maximum Inv Field
                        Integer.parseInt(aLabelText.getText()));        // Machine ID Field

                Inventory.addPart(p);
            }
            // If Outsourced is clicked and no errors are thrown, the part is added to the inventory
            else if (aOSRadio.isSelected()) {
                Part p = new Outsource(

                        Inventory.partIdCounter(),                      // Part ID Field (Auto-Generated)
                        partNameText.getText(),                         // Part Name Field
                        Double.parseDouble(partPriceText.getText()),    // Part Price/CPU Field
                        Integer.parseInt(partInvText.getText()),        // Part Inventory Field
                        Integer.parseInt(partMinText.getText()),        // Part Minimum Inv Field
                        Integer.parseInt(partMaxText.getText()),        // Part Maximum Inv field
                        aLabelText.getText());                          // Company Name Field

                Inventory.addPart(p);
            }

            // The goToMain method is called if the part is saved without errors
            goToMain(event);

        }

        // Exception thrown if the values entered are not valid
        catch (NumberFormatException e) {
            System.out.println("Please enter valid values in text box.");

        }
    }

    /*
    The method for what happens when the "Cancel" button is clicked

    Asks the user if they want to cancel adding a part to the inventory system
    If the user clicks "OK", then the user is taken back to MainScreen
     */
    @FXML
    private void onActionCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Cancel Adding Part");
        alert.setHeaderText("Return to Main Screen");
        alert.setContentText("Are you sure you want to cancel?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            goToMain(event);
        }

    }

    /*
    The method for what happens when the goToMain method is actioned

    The MainScreen view is loaded into a Node object and is passed to the goToMain() method
    This sends the application back to the MainScreen
     */
    public void goToMain(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/Main Screen.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setTitle("");
        stage.setScene(new Scene(root));
        stage.show();
    }


    //The code will initialize the application with a URL and ResourceBundle
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //TODO

    }
}