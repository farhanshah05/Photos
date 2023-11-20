package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

/**
 * Represents a user with albums and related functionality.
 * Serializable to allow object serialization for saving/loading.
 *
 * Author: Farhan Shah
 */
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Current username
	 */
	public String username;

	/**
	 * List of albums belonging to the user
	 */
	public ArrayList<Album> albums;

	/**
	 * Current active album
	 */
	public Album currentAlbum;

	// Directory and file names for storing user data
	public static final String storeDir = "data";
	public static final String storeFile = "users.dat";

	/**
	 * Constructor to initialize a User with a given username.
	 *
	 * @param username The username of the user.
	 */
	public User(String username) {
		this.username = username;
		albums = new ArrayList<>();
	}

	/**
	 * Prints the names of all albums owned by the user.
	 */
	public void printAlbums() {
		for (Album album : albums) {
			System.out.println(album.getAlbumName());
		}
	}

	/**
	 * Adds an album to the user's list of albums.
	 *
	 * @param album The album to be added.
	 */
	public void addAlbum(Album album) {
		albums.add(album);
	}

	/**
	 * Deletes an album at the specified index from the user's list of albums.
	 *
	 * @param index The index of the album to be deleted.
	 */
	public void deleteAlbum(int index) {
		albums.remove(index);
	}

	/**
	 * Gets the username of the user.
	 *
	 * @return The username.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username of the user.
	 *
	 * @param username The new username.
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Gets the list of albums owned by the user.
	 *
	 * @return The list of albums.
	 */
	public ArrayList<Album> getAlbums() {
		return albums;
	}

	/**
	 * Checks if the given album exists in the user's list of albums.
	 *
	 * @param albumname The name of the album to check.
	 * @return True if the album exists, false otherwise.
	 */
	public boolean exists(Album albumname) {
		for (Album album : albums) {
			if (album.getAlbumName().equals(albumname.getAlbumName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Sets the list of albums for the user.
	 *
	 * @param albums The new list of albums.
	 */
	public void setAlbums(ArrayList<Album> albums) {
		this.albums = albums;
	}

	/**
	 * Gets the currently active album.
	 *
	 * @return The current album.
	 */
	public Album getCurrentAlbum() {
		return currentAlbum;
	}

	/**
	 * Gets the album at the specified index.
	 *
	 * @param index The index of the album to retrieve.
	 * @return The album at the specified index.
	 */
	public Album getAlbum(int index) {
		return albums.get(index);
	}

	/**
	 * Sets the currently active album.
	 *
	 * @param currentAlbum The new active album.
	 */
	public void setCurrentAlbum(Album currentAlbum) {
		this.currentAlbum = currentAlbum;
	}

	/**
	 * Returns a list of photos that are tagged with at least one tag from the given list.
	 *
	 * @param taggedlist The list of tags to search for.
	 * @return The list of photos matching the OR condition.
	 */
	public ArrayList<Photo> getOrTaggedPhotos(ArrayList<Tag> taggedlist) {
		ArrayList<Photo> photolist = new ArrayList<>();
		// Used to make sure no duplicates
		HashSet<Photo> check = new HashSet<>();
		for (Tag tag : taggedlist) {
			for (Album album : albums) {
				for (Photo photo : album.getPhotos()) {
					// Check if the photo has a tag with the same name and value
					if (photo.tagExists(tag.name, tag.value)) {
						check.add(photo);
					}
				}
			}
		}
		photolist.addAll(check);
		return photolist;
	}

	/**
	 * Returns a list of photos that are tagged with all tags from the given list.
	 *
	 * @param taggedlist The list of tags to search for.
	 * @return The list of photos matching the AND condition.
	 */
	public ArrayList<Photo> getAndTaggedPhotos(ArrayList<Tag> taggedlist) {
		ArrayList<Photo> photolist = new ArrayList<>();
		// Used to make sure no duplicates
		HashSet<Photo> check = new HashSet<>();

		System.out.println(taggedlist);
		for (Album album : albums) {
			for (Photo photo : album.getPhotos()) {
				System.out.print(photo.getTagList());
				// Check if the photo contains all tags from the given list
				if (photo.getTagList().containsAll(taggedlist)) {
					check.add(photo);
				}
			}
		}
		photolist.addAll(check);
		return photolist;
	}

	/**
	 * Returns a list of photos within the specified date range.
	 *
	 * @param fromDate The start date of the range.
	 * @param toDate   The end date of the range.
	 * @return An ArrayList of photos within the specified date range.
	 */
	public ArrayList<Photo> getPhotosInRange(LocalDate fromDate, LocalDate toDate) {
		ArrayList<Photo> inrange = new ArrayList<>();
		Calendar startdate = Calendar.getInstance();
		startdate.set(fromDate.getYear(), fromDate.getMonthValue(), fromDate.getDayOfMonth());

		Calendar enddate = Calendar.getInstance();
		enddate.set(toDate.getYear(), toDate.getMonthValue(), toDate.getDayOfMonth());

		for (Album album : albums) {
			for (Photo photo : album.getPhotos()) {
				Date date = photo.getDate();
				Calendar pDate = Calendar.getInstance();
				pDate.setTime(date);

				Calendar today = Calendar.getInstance();

				int year = pDate.get(Calendar.YEAR);
				int month = pDate.get(Calendar.MONTH) + 1;
				int dateOfMonth = pDate.get(Calendar.DAY_OF_MONTH);

				today.set(year, month, dateOfMonth);
				if ((today.compareTo(startdate) > 0 && today.compareTo(enddate) < 0) || today.equals(startdate)
						|| today.equals(enddate)) {
					inrange.add(photo);
				}
			}
		}
		return inrange;
	}

	/**
	 * Saves the User object to a .dat file.
	 *
	 * @param pdApp The User object to be saved.
	 * @throws IOException If an I/O error occurs while saving.
	 */
	public static void save(User pdApp) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(
				new FileOutputStream(Paths.get(storeDir, storeFile).toString()));
		oos.writeObject(pdApp);
		oos.close();
	}

	/**
	 * Loads a User object from a .dat file.
	 *
	 * @return The loaded User object.
	 * @throws IOException            If an I/O error occurs while loading.
	 * @throws ClassNotFoundException If the class of a serialized object cannot be found.
	 */
	public static User load() throws IOException, ClassNotFoundException {
		try (ObjectInputStream ois = new ObjectInputStream(
				new FileInputStream(Paths.get(storeDir, storeFile).toString()))) {
			return (User) ois.readObject();
		}
	}
}
