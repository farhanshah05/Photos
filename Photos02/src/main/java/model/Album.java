package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Represents an Album that users can add pictures to.
 *
 * @author Farhan Shah
 */
public class Album implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String storeDir = "data";
	public static final String storeFile = "users.dat";

	public String albumName;
	public ArrayList<Photo> photoslist;
	public int photoCount = 0;
	public Photo currentPhoto;

	/**
	 * Constructor for creating an Album.
	 *
	 * @param albumName The name of the album.
	 */
	public Album(String albumName) {
		this.albumName = albumName;
		photoslist = new ArrayList<Photo>();
	}

	// Comparators for sorting albums
	public static Comparator<Album> sortByAZ = Comparator.comparing(album -> album.albumName);
	public static Comparator<Album> sortByZA = (a1, a2) -> a2.albumName.compareTo(a1.albumName);
	public static Comparator<Album> sortByIP = Comparator.comparingInt(album -> album.photoCount);
	public static Comparator<Album> sortByDP = (a1, a2) -> Integer.compare(a2.photoCount, a1.photoCount);
	public static Comparator<Album> sortByID = (a1, a2) -> compareDates(a1, a2, false);
	public static Comparator<Album> sortByDD = (a1, a2) -> compareDates(a1, a2, true);

	// Method to compare dates for sorting
	private static int compareDates(Album a1, Album a2, boolean reverse) {
		Date date1 = findDate(a1, reverse);
		Date date2 = findDate(a2, reverse);

		if (date1 != null && date2 != null) {
			return reverse ? date2.compareTo(date1) : date1.compareTo(date2);
		} else if (date1 == null && date2 != null) {
			return reverse ? -1 : 1;
		} else if (date1 != null && date2 == null) {
			return reverse ? 1 : -1;
		}
		return 0;
	}

	// Helper method to find the extreme date for sorting
	private static Date findDate(Album album, boolean reverse) {
		Date date = null;
		if (!album.photoslist.isEmpty()) {
			date = album.getPhotos().get(0).date;
			for (Photo photo : album.photoslist) {
				if ((reverse && photo.date.after(date)) || (!reverse && photo.date.before(date))) {
					date = photo.date;
				}
			}
		}
		return date;
	}

	/**
	 * Checks if a photo exists in the album.
	 *
	 * @param fp The file path of the photo.
	 * @return true if the photo exists, false otherwise.
	 */
	public boolean exists(String fp) {
		if (photoslist.size() > 0 && !fp.isEmpty()) {
			for (Photo photos : photoslist) {
				if (photos.getFilePath().equals(fp)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Gets the date of the first added photo in the album.
	 *
	 * @return The formatted date string or "No Date" if no photos are present.
	 */
	public String getFirstDate() {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("E, M-d-y 'at' h:m:s a");
		Date date = findExtremeDate(true);
		return date != null ? dateFormatter.format(date) : "No Date";
	}

	/**
	 * Gets the date of the last altered photo in the album.
	 *
	 * @return The formatted date string or "No Date" if no photos are present.
	 */
	public String getLastDate() {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("E, M-d-y 'at' h:m:s a");
		Date date = findExtremeDate(false);
		return date != null ? dateFormatter.format(date) : "No Date";
	}

	// Helper method to find the extreme date for first or last date
	private Date findExtremeDate(boolean findMin) {
		Date date = null;
		if (!photoslist.isEmpty()) {
			date = this.getPhotos().get(0).date;
			for (Photo photo : photoslist) {
				if ((findMin && photo.date.before(date)) || (!findMin && photo.date.after(date))) {
					date = photo.date;
				}
			}
		}
		return date;
	}

	/**
	 * Gets the name of the album.
	 *
	 * @return The name of the album.
	 */
	public String getAlbumName() {
		return albumName;
	}

	/**
	 * Sets the name of the album.
	 *
	 * @param albumName The new name for the album.
	 */
	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	/**
	 * Gets the name of the album.
	 *
	 * @return The name of the album.
	 */
	public String getName() {
		return this.albumName;
	}

	/**
	 * Renames the album.
	 *
	 * @param name The new name for the album.
	 */
	public void rename(String name) {
		this.albumName = name;
	}

	/**
	 * Adds a photo to the album.
	 *
	 * @param photo The photo to be added.
	 */
	public void addPhoto(Photo photo) {
		photoslist.add(photo);
		photoCount++;
	}

	/**
	 * Removes a photo from the album.
	 *
	 * @param index The index of the photo to be removed.
	 */
	public void deletePhoto(int index) {
		photoslist.remove(index);
		photoCount--;
	}

	/**
	 * Gets the list of photos in the album.
	 *
	 * @return The list of photos in the album.
	 */
	public ArrayList<Photo> getPhotos() {
		return photoslist;
	}

	/**
	 * Gets the current photo.
	 *
	 * @return The current photo.
	 */
	public Photo getCurrentPhoto() {
		return currentPhoto;
	}

	/**
	 * Sets the current photo.
	 *
	 * @param currentPhoto The new current photo.
	 */
	public void setCurrentPhoto(Photo currentPhoto) {
		this.currentPhoto = currentPhoto;
	}

	/**
	 * Returns the name of the album as a string representation.
	 *
	 * @return The name of the album.
	 */
	@Override
	public String toString() {
		return getName();
	}

	/**
	 * Saves the state of the album to a .dat file.
	 *
	 * @param album The album to be saved.
	 * @throws IOException If an I/O error occurs while saving.
	 */
	public static void save(Album album) throws IOException {
		try (ObjectOutputStream oos = new ObjectOutputStream(
				new FileOutputStream(storeDir + File.separator + storeFile))) {
			oos.writeObject(album);
		}
	}

	/**
	 * Loads the album from a .dat file.
	 *
	 * @return The loaded album.
	 * @throws IOException            If an I/O error occurs while loading.
	 * @throws ClassNotFoundException If the class of the serialized object cannot
	 *                                be found.
	 */
	public static Album load() throws IOException, ClassNotFoundException {
		try (ObjectInputStream ois = new ObjectInputStream(
				new FileInputStream(storeDir + File.separator + storeFile))) {
			return (Album) ois.readObject();
		}
	}
}
