package controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Photo;

/**
 * SlideshowController manages the display and navigation of a photo slideshow.
 * It allows users to navigate forward and backward through photos, go back to a
 * previous page, and log out. The controller updates the displayed image and
 * status text based on the current photo index in the album.
 *
 * @author Farhan Shah
 */
public class SlideshowController implements LogoutController {

	@FXML
	public ImageView displayArea;

	@FXML
	public Button mForward, mBackward, mBack, mLogOff;

	@FXML
	public TextField tfCaption, tfTagName, tfTagValue;

	@FXML
	public Text tStatus;

	public static ArrayList<Photo> album = new ArrayList<>();
	public static final int frontIndex = 0;
	public static int backIndex;
	public static int currentIndex;

	public void start() {
		updateSlideshow();
	}

	public void updateSlideshow() {
		currentIndex = 0;
		backIndex = album.size() - 1;

		updateImageView();
		updateStatusText();
	}

	private void updateImageView() {
		File file;
		Photo photo = album.get(currentIndex);
		if (photo != null) {
			file = photo.getPic();
			Image image = new Image(file.toURI().toString());
			displayArea.setImage(image);
		}
	}

	private void updateStatusText() {
		int ci = currentIndex + 1;
		int bi = backIndex + 1;
		tStatus.setText("Photo: " + ci + " of " + bi);
	}

	public void forward() {
		if (currentIndex + 1 > backIndex) {
			return;
		}
		currentIndex++;
		updateImageView();
		updateStatusText();
	}

	public void backward() {
		if (currentIndex - 1 < frontIndex) {
			return;
		}
		currentIndex--;
		updateImageView();
		updateStatusText();
	}

	public void back(ActionEvent event) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/application/PhotoView.fxml"));
		Parent sceneManager = (Parent) fxmlLoader.load();
		PhotoViewController photoViewController = fxmlLoader.getController();
		Scene adminScene = new Scene(sceneManager);
		Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		photoViewController.start(appStage);
		appStage.setScene(adminScene);
		appStage.show();
	}

	public void logOut(ActionEvent event) throws IOException {
		logMeOut(event);
	}
}
