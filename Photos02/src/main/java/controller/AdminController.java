package controller;

import java.io.IOException;
import java.util.*;

import application.Photos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import model.Superuser;
import java.util.Objects;

/**
 * Controller class for handling actions on the Admin Page.
 * @author Farhan Shah
 */
public class AdminController implements LogoutController {

    @FXML
    private ListView<String> listview;

    @FXML
    private Button createButton, deleteButton, logOffButton;

    @FXML
    private TextField tfUsername;

    // Static list to store usernames
    private static final ArrayList<String> userList = new ArrayList<>();

    // Observable list to display usernames in the ListView
    private ObservableList<String> observableList;

    // Static instance of the Superuser class
    private static final Superuser adminUser = Photos.photoLibraryUser;

    /**
     * Initializes the Admin Page.
     */
    public void start() {
        update();
        // Select the first user if the list is not empty
        if (!userList.isEmpty()) {
            listview.getSelectionModel().select(0);
        }
    }

    /**
     * Logs the user out.
     * @param event Action event triggering the method.
     * @throws IOException If an I/O error occurs.
     */
    public void logOut(ActionEvent event) throws IOException {
        logMeOut(event);
    }

    /**
     * Adds a new user to the system.
     * @param event Action event triggering the method.
     * @throws IOException If an I/O error occurs.
     */
    public void addUser(ActionEvent event) throws IOException {
        // Get and trim the entered username
        String username = Objects.requireNonNull(tfUsername.getText()).trim();

        // Check for an empty username
        if (username.isEmpty()) {
            showErrorAlert("Admin Error", "Empty Field", "Please enter a username.");
            return;
        }

        // Check if the username already exists
        if (adminUser.exists(username)) {
            showErrorAlert("Admin Error", "Username Exists", "Username already exists. Try entering a new username!");
            return;
        }

        // Check if the username is "admin"
        if ("admin".equals(username)) {
            showErrorAlert("Admin Error", "Invalid Username", "Cannot add 'admin' to Users.");
            return;
        }

        // Add the user, update the view, and save the changes
        adminUser.addUser(username);
        update();
        tfUsername.clear();
        Superuser.save(adminUser);
    }

    /**
     * Deletes a user from the system.
     * @param event Action event triggering the method.
     * @throws IOException If an I/O error occurs.
     */
    public void deleteUser(ActionEvent event) throws IOException {
        int index = listview.getSelectionModel().getSelectedIndex();

        // Check if a user is selected
        if (index == -1) {
            return; // No user selected
        }

        // Confirm user deletion with a dialog
        if (confirmDeleteUser()) {
            // Delete the user, update the view, and save the changes
            adminUser.deleteUser(index);
            update();
            Superuser.save(adminUser);

            // Adjust the selection in the ListView
            adjustListViewSelection(index);
        }
    }

    /**
     * Displays a confirmation dialog for user deletion.
     * @return True if the user confirms deletion, false otherwise.
     */
    private boolean confirmDeleteUser() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this User?");

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * Adjusts the selection in the ListView after a user is deleted.
     * @param index Index of the deleted user.
     */
    private void adjustListViewSelection(int index) {
        if (adminUser.getUsers().size() == 1) {
            listview.getSelectionModel().select(0);
        } else if (index == adminUser.getUsers().size()) {
            listview.getSelectionModel().select(index - 1);
        } else {
            listview.getSelectionModel().select(index);
        }
    }

    /**
     * Updates the ListView with the latest user information.
     */
    public void update() {
        userList.clear();
        // Populate the list with usernames from the Superuser instance
        for (int i = 0; i < adminUser.getUsers().size(); i++) {
            userList.add(adminUser.getUsers().get(i).getUsername());
        }

        // Refresh the ListView with the updated user list
        observableList = FXCollections.observableArrayList(userList);
        listview.setItems(observableList);
        listview.refresh();
    }

    /**
     * Displays an error alert with the specified title, header, and content.
     * @param title Alert title.
     * @param header Alert header.
     * @param content Alert content.
     */
    private void showErrorAlert(String title, String header, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
