package application;

import java.io.IOException;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import model.Superuser;

public class Photos extends Application {

	public static Superuser photoLibraryUser = new Superuser();
	private Stage mainStage;  // Declare mainStage as an instance variable

	@Override
	public void start(Stage primaryStage) {
		try {
			mainStage = primaryStage;  // Assign primaryStage to mainStage
			System.out.println(getClass().getResource("Login.fxml"));

			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
			AnchorPane root = fxmlLoader.load();

			Scene scene = new Scene(root);
			mainStage.setResizable(false);
			mainStage.setTitle("Photo Library");
			mainStage.setScene(scene);
			mainStage.show();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		mainStage.setOnCloseRequest(windowEvent -> {
			try {
				Superuser.save(photoLibraryUser);
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
			System.out.print("Application Closed");
		});
	}

	public static void main(String[] args) {
		try {
			photoLibraryUser = Superuser.load();
		} catch (IOException | ClassNotFoundException exception) {
			exception.printStackTrace();
		}
		launch(args);
	}
}
