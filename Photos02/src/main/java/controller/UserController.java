package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import model.Album;
import model.Superuser;
import model.User;

public class UserController implements LogoutController {

	@FXML
	public ListView<Album> listview;

	@FXML
	public Button mLogOff, mDisplay, mOpenAlbum, mRenameAlbum, mDeleteAlbum, mSearch, mAddAlbum;

	@FXML
	public MenuButton mSortBy;

	@FXML
	public Text tUser, tNumber, tDateSpan;

	@FXML
	public TextField tfName, tfNewAlbum;

	public static String username;

	public static ArrayList<Album> albumlist = new ArrayList<>();

	public ObservableList<Album> observableList;

	public static Superuser adminuser = Photos.photoLibraryUser;

	public static User user;

	public static boolean stock;

	public void start(Stage app_stage) {
		update();

		app_stage.setTitle(adminuser.getCurrent().getUsername() + "'s" + " Photo Albums");
		if (!albumlist.isEmpty()) {
			listview.getSelectionModel().select(0);
		}

		if (albumlist.size() > 0) {
			tfName.setText(albumlist.get(0).albumName);
			tNumber.setText("Number of Photos: " + albumlist.get(0).photoCount);
			tDateSpan.setText("Date Span (First, Last): \n\t" + albumlist.get(0).getFirstDate() + "\n\t" + albumlist.get(0).getLastDate());
		}
		listview.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> updateContent(newValue));
	}

	private void sortAlbums(Comparator<Album> comparator) throws IOException {
		Collections.sort(albumlist, comparator);
		observableList = FXCollections.observableArrayList(albumlist);
		listview.setItems(observableList);
		listview.refresh();
		User.save(user);
	}

	public void sortByAZ() throws IOException {
		sortAlbums(Album.sortByAZ);
	}

	public void sortByZA() throws IOException {
		sortAlbums(Album.sortByZA);
	}

	public void sortByIP() throws IOException {
		sortAlbums(Album.sortByIP);
	}

	public void sortByDP() throws IOException {
		sortAlbums(Album.sortByDP);
	}

	public void sortByID() throws IOException {
		sortAlbums(Album.sortByID);
	}

	public void sortByDD() throws IOException {
		sortAlbums(Album.sortByDD);
	}

	private void updateContent(Album album) {
		if (album != null) {
			tfName.setText(album.albumName);
			tNumber.setText("Number of Photos: " + album.photoCount);
			tDateSpan.setText("Date Span: \n\t" + album.getFirstDate() + " \n\t" + album.getLastDate());
		}
	}

	public void updateContentBack() {
		if (albumlist.size() > 0) {
			Album alb = listview.getSelectionModel().getSelectedItem();
			tNumber.setText("Number of Photos: " + alb.photoCount);
			tDateSpan.setText("Date Span: \n\t" + alb.getFirstDate() + "\n\t" + alb.getLastDate());
		}
	}

	private void showErrorAlert(String title, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setContentText(content);
		alert.showAndWait();
	}

	public void addAlbum() throws IOException {
		String albumname = tfNewAlbum.getText().trim();
		Album album = new Album(albumname);

		if (albumname.isEmpty() || albumname == null) {
			showErrorAlert("Empty Field", "Please enter an album name.");
			return;
		} else if (user.exists(album)) {
			showErrorAlert("Album already exists.", "Try entering a new album!");
			return;
		} else {
			user.addAlbum(album);
			update();
			tfNewAlbum.clear();
		}
		User.save(user);
	}

	public void renameAlbum() throws IOException {
		String newName = tfName.getText().trim();

		int index = listview.getSelectionModel().getSelectedIndex();
		Album album = user.getAlbum(index);
		Optional<ButtonType> result;
		Album tempAlbum = new Album(newName);

		if (newName.length() == 0) {
			showErrorAlert("Rename Error", "Please enter a valid album name.");
			return;
		} else if (newName.equals(album.albumName)) {
			showErrorAlert("Rename Error", "No changes made. Please enter a valid album name before clicking 'Rename'.");
			return;
		} else if (user.exists(tempAlbum)) {
			showErrorAlert("Rename Error", "Album name already in use.");
			return;
		} else {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirm Rename");
			alert.setHeaderText(null);
			alert.setContentText("Are you sure you want to rename this album?");
			result = alert.showAndWait();
		}

		if (result.get() == ButtonType.OK) {
			album.rename(newName);
			update();
			User.save(user);
		} else {
			return;
		}
		return;
	}

	public void openAlbum(ActionEvent event) throws IOException {
		PhotoViewController.user = user;
		PhotoViewController.album = listview.getSelectionModel().getSelectedItem();
		PhotoViewController.albumlist = albumlist;

		int albumindex = listview.getSelectionModel().getSelectedIndex();
		int currentuserindex = adminuser.getUserIndex();
		if (adminuser.getUsers().get(currentuserindex).getAlbums().size() == 0) {
			showErrorAlert("Empty Deletion", "Cannot delete something that isn't there");
			return;
		}
		Album album = adminuser.getUsers().get(currentuserindex).getAlbums().get(albumindex);

		adminuser.getUsers().get(currentuserindex).setCurrentAlbum(album);

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/application/PhotoView.fxml"));
		Parent sceneManager = (Parent) fxmlLoader.load();
		PhotoViewController photoController = fxmlLoader.getController();
		Scene adminScene = new Scene(sceneManager);
		Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		photoController.start(appStage);
		appStage.setScene(adminScene);
		appStage.show();
	}

	public void deleteAlbum() throws IOException {
		int index = listview.getSelectionModel().getSelectedIndex();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirm Delete");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to delete this album?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			user.deleteAlbum(index);
			update();
			User.save(user);

			if (user.getAlbums().size() == 0) {
				mDeleteAlbum.setVisible(false);
			} else {
				int lastuserindex = user.getAlbums().size();
				if (user.getAlbums().size() == 1) {
					listview.getSelectionModel().select(0);
				} else if (index == lastuserindex) {
					listview.getSelectionModel().select(lastuserindex - 1);
				} else {
					listview.getSelectionModel().select(index);
				}
			}
		} else {
			return;
		}
		return;
	}

	public void search(ActionEvent event) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/application/Search.fxml"));
		Parent sceneManager = (Parent) fxmlLoader.load();
		SearchController searchController = fxmlLoader.getController();
		Scene adminScene = new Scene(sceneManager);
		Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		searchController.start();
		appStage.setScene(adminScene);
		appStage.show();
	}

	public void logOut(ActionEvent event) throws IOException {
		logMeOut(event);
	}

	public void update() {
		tUser.setText(username + "'s Album List:");
		user = adminuser.getUser(username);

		albumlist.clear();
		for (int i = 0; i < user.getAlbums().size(); i++) {
			albumlist.add(user.getAlbums().get(i));
		}
		observableList = FXCollections.observableArrayList(albumlist);
		listview.setItems(observableList);
		listview.refresh();
	}
}
