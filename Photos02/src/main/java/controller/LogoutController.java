package controller;

import java.io.IOException;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * Interface that helps logging the user out.
 *
 * @author Farhan Shah
 */
public interface LogoutController {

	String LOGIN_FXML_PATH = "/application/Login.fxml";

	/**
	 * A helper method that logs the user out of the program and redirects them to
	 * the login screen.
	 *
	 * @param e
	 * @throws IOException
	 */
	default void logMeOut(ActionEvent e) throws IOException {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirm Logout");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to logout of your account?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(LOGIN_FXML_PATH));
			Parent sceneManager = fxmlLoader.load();
			Scene loginScene = new Scene(sceneManager);
			Stage appStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
			appStage.setScene(loginScene);
			appStage.show();
		} else {
			alert.close();
		}
	}
}
