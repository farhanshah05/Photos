package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import application.Photos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import model.Album;
import model.Superuser;
import model.User;

/**
 * Controller class for the login functionality.
 * Manages user authentication and navigation to different pages.
 *
 * @author Farhan Shah
 */
public class LoginController {

	@FXML
	public Button mLogIn;

	@FXML
	public TextField tfUsername;

	// Constant for admin username
	public final String admin = "admin";

	// Static reference to the application's Superuser instance
	public static Superuser driver = Photos.photoLibraryUser;

	/**
	 * Handles the login process when the user clicks the login button.
	 * Redirects users based on their input.
	 *
	 * @param event ActionEvent triggered by the login button
	 * @throws IOException if there is an issue loading the FXML file for the next scene
	 */
	public void login(ActionEvent event) throws IOException {
		String username = tfUsername.getText().trim();

		if (username.equals(admin)) {
			// Load the Admin page if the entered username is the admin
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/application/Admin.fxml"));
			Parent sceneManager = (Parent) fxmlLoader.load();
			AdminController adminController = fxmlLoader.getController();
			Scene adminScene = new Scene(sceneManager);
			Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			adminController.start();
			appStage.setScene(adminScene);
			appStage.show();
		} else if (driver.checkUser(username)) {
			// Load the User page if the entered username is a valid user
			User currentUser = driver.getCurrent();
			ArrayList<Album> useralbums = currentUser.getAlbums();
			UserController.username = username;
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/application/User.fxml"));
			Parent sceneManager = (Parent) fxmlLoader.load();
			UserController userController = fxmlLoader.getController();
			Scene userScene = new Scene(sceneManager);
			Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			userController.start(appStage);
			appStage.setScene(userScene);
			appStage.show();
		} else if (username.isEmpty() || username == null) {
			// Display an error if the username is empty
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Please enter a username");
			Optional<ButtonType> buttonClicked = alert.showAndWait();
			if (buttonClicked.get() == ButtonType.OK) {
				alert.close();
			} else {
				alert.close();
			}
		} else {
			// Display an error if the entered username is not valid
			System.out.println("Incorrect Input");
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Login Issue Encountered");
			alert.setHeaderText("Please enter a valid username");
			Optional<ButtonType> buttonClicked = alert.showAndWait();
			if (buttonClicked.get() == ButtonType.OK) {
				alert.close();
			} else {
				alert.close();
			}

		}
	}

	/**
	 * Handles the Enter key press event in the username TextField.
	 * Calls the login method when Enter is pressed.
	 *
	 * @param keyEvent KeyEvent triggered by pressing a key
	 * @throws IOException if there is an issue loading the FXML file for the next scene
	 */
	@FXML
	public void handleEnterKey(javafx.scene.input.KeyEvent keyEvent) throws IOException {
		if (keyEvent.getCode() == KeyCode.ENTER) {
			login(new ActionEvent(tfUsername, null));
		}
	}
}
