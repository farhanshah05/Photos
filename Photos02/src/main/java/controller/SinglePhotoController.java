package controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import application.Photos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.Photo;
import model.Superuser;
import model.Tag;

/**
 * This class controls the view/functions of a single image
 *
 * @author Farhan Shah
 */
public class SinglePhotoController implements LogoutController {

	// FXML elements
	@FXML
	public ListView<String> listview;

	@FXML
	public ImageView displayArea;

	@FXML
	public Button mLogOff, mBack, mCaption, mAddTag, mRemoveTag;

	@FXML
	public TextField tfCaption, tfTagName, tfTagValue;

	// Reference to the main user of the application
	public static Superuser adminuser = Photos.photoLibraryUser;

	// Lists to store tags and their display properties
	public static ArrayList<Tag> taglist = new ArrayList<>();
	public static ArrayList<String> tagdisplay = new ArrayList<>();

	// ObservableList for displaying tags in the ListView
	public ObservableList<String> obstag;

	// Current instance of the displayed photo
	public static Photo photo;

	// Method called on scene start
	public void start(Stage app_stage) {
		app_stage.setTitle(adminuser.getCurrent().getCurrentAlbum().getCurrentPhoto().getCaption() + " ");
		update();
		if (!taglist.isEmpty()) {
			listview.getSelectionModel().select(0); // select first user
		}
	}

	// Method to save the edited caption
	public void saveCaption(ActionEvent event) throws IOException {
		String caption = tfCaption.getText().trim();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Caption Confirmation");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to change the caption to: " + caption);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			photo.setCaption(caption);
			photo.save(photo);
		} else {
			return;
		}
	}

	// Method to add a new tag to the photo
	public void addTag(ActionEvent event) throws IOException {
		String tagName = tfTagName.getText().trim();
		String tagValue = tfTagValue.getText().trim();
		if (tagName.isEmpty() || tagValue.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Tag Add Error");
			alert.setContentText("Tag Information Incomplete.");
			alert.showAndWait();
			return;
		} else {
			Tag tag = new Tag(tagName, tagValue);
			adminuser.getCurrent().getCurrentAlbum().getCurrentPhoto().addTag(tag.name, tag.value);
			update();
			Superuser.save(adminuser);
		}
	}

	// Method to remove a tag from the photo
	public void removeTag(ActionEvent event) throws IOException {
		int index = listview.getSelectionModel().getSelectedIndex();
		ArrayList<Tag> taglist = adminuser.getCurrent().getCurrentAlbum().getCurrentPhoto().getTagList();
		adminuser.getCurrent().getCurrentAlbum().getCurrentPhoto().removeTag(taglist.get(index).name,
				taglist.get(index).value);
		update();
		Superuser.save(adminuser);
	}

	// Method to update the display of the photo and tags
	public void update() {
		File file;
		if (photo != null) {
			file = photo.getPic();
			Image image = new Image(file.toURI().toString());
			displayArea.setImage(image);
		}

		// Update the list of tags for display
		tagdisplay.clear();
		ArrayList<Tag> tags = adminuser.getCurrent().getCurrentAlbum().getCurrentPhoto().getTagList();

		for (Tag tag : tags) {
			tagdisplay.add("Name: " + tag.name + " | Value: " + tag.value);
		}
		obstag = FXCollections.observableArrayList(tagdisplay);
		listview.setItems(obstag);
		tfTagName.clear();
		tfTagValue.clear();
	}

	// Method to navigate back to the photo view
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

	// Method to log out the user
	public void logOut(ActionEvent event) throws IOException {
		logMeOut(event);
	}
}
