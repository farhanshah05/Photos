package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Objects;

import application.Photos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
	public Button loginButton;

	@FXML
	public TextField tfUsername;

	// Constant for admin username
	private static final String ADMIN_USERNAME = "admin";

	// Static reference to the application's Superuser instance
	private static final Superuser photoLibraryUser = Photos.photoLibraryUser;

	/**
	 * Handles the login process when the user clicks the login button.
	 * Redirects users based on their input.
	 *
	 * @param event ActionEvent triggered by the login button
	 * @throws IOException if there is an issue loading the FXML file for the next scene
	 */
	public void login(ActionEvent event) throws IOException {
		String username = Objects.requireNonNull(tfUsername.getText()).trim();

		if (ADMIN_USERNAME.equals(username)) {
			// Load the Admin page if the entered username is the admin
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FxmlPaths.ADMIN_PAGE));
			Parent sceneManager = fxmlLoader.load();
			AdminController adminController = fxmlLoader.getController();
			Scene adminScene = new Scene(sceneManager);
			Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			adminController.start();
			appStage.setScene(adminScene);
			appStage.show();
		} else if (photoLibraryUser.checkUser(username)) {
			// Load the User page if the entered username is a valid user
			User currentUser = photoLibraryUser.getCurrent();
			ArrayList<Album> userAlbums = currentUser.getAlbums();
			UserController.username = username;
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FxmlPaths.USER_PAGE));
			Parent sceneManager = fxmlLoader.load();
			UserController userController = fxmlLoader.getController();
			Scene userScene = new Scene(sceneManager);
			Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			userController.start(appStage);
			appStage.setScene(userScene);
			appStage.show();
		} else if (username.isEmpty()) {
			// Display an error if the username is empty
			showAlert("Error Dialog", "Please enter a username");
		} else {
			// Display an error if the entered username is not valid
			showAlert("Login Issue Encountered", "Please enter a valid username");
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

	/**
	 * Displays an alert with the specified title and content.
	 *
	 * @param title   Alert title.
	 * @param content Alert content.
	 */
	private void showAlert(String title, String content) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}

	/**
	 * Class to hold constant FXML file paths.
	 */
	private static class FxmlPaths {
		static final String ADMIN_PAGE = "/application/Admin.fxml";
		static final String USER_PAGE = "/application/User.fxml";
	}
}
