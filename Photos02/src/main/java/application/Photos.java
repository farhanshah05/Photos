package application;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import model.Superuser;

/**
 * The main class for the Photo Library application.
 * VCS Transfer
 * Author: @Farhan Shah
 */
public class Photos extends Application {

	// Create an instance of the Superuser class for the photo library
	public static Superuser photoLibraryUser = new Superuser();

	// Declare mainStage as an instance variable for easy access
	private Stage mainStage;

	/**
	 * The entry point of the application.
	 * @param primaryStage The primary stage for the application.
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			mainStage = primaryStage;  // Assign primaryStage to mainStage

			// Load the Login.fxml file using FXMLLoader
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
			AnchorPane root = fxmlLoader.load();

			// Create a scene with the loaded FXML file and set it to the main stage
			Scene scene = new Scene(root);
			mainStage.setResizable(false);
			mainStage.setTitle("Photo Library");
			mainStage.setScene(scene);
			mainStage.show();
		} catch (Exception ex) {
			System.err.println("Error during application start: " + ex.getMessage());
			ex.printStackTrace();
		}

		// Set an event handler for the close request of the main stage
		mainStage.setOnCloseRequest(windowEvent -> {
			try {
				// Save the photo library user data on application close
				Superuser.save(photoLibraryUser);
			} catch (IOException ioException) {
				System.err.println("Error saving photo library user data: " + ioException.getMessage());
				ioException.printStackTrace();
			}
			//System.out.print("Application Closed");
		});
	}

	/**
	 * The main method to launch the application.
	 * @param args Command-line arguments.
	 */
	public static void main(String[] args) {
		try {
			// Load the saved photo library user data
			photoLibraryUser = Superuser.load();
		} catch (IOException | ClassNotFoundException exception) {
			System.err.println("Error loading photo library user data: " + exception.getMessage());
			exception.printStackTrace();
		}
		launch(args);
	}
}
