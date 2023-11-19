package model;

import java.io.*;
import java.util.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;

/**
 * Represents a photo and provides methods to manipulate photo data.
 * Author: Farhan Shah
 */
public class Photo implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String storeDir = "data";
	public static final String storeFile = "users.dat";

	// Photo attributes
	public String photoname;
	public File pic;
	public ArrayList<Tag> taglist;
	public String caption;
	public String filepath;
	public Calendar cal;
	public Date date;
	public boolean isStock = false;

	/**
	 * Photo constructor.
	 * @param pic The file of the picture.
	 * @param photoname The name of the photo.
	 */
	public Photo(File pic, String photoname) {
		this.photoname = photoname;
		if (pic != null) this.pic = new File(photoname);
		else this.pic = pic;
		this.taglist = new ArrayList<Tag>();
		cal = new GregorianCalendar();
		cal.set(Calendar.MILLISECOND, 0);
		this.date = cal.getTime();
	}

	/**
	 * Sets the caption for the photo.
	 * @param caption The caption to set.
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}

	/**
	 * Adds a tag to the photo.
	 * @param name The name of the tag.
	 * @param value The value of the tag.
	 */
	public void addTag(String name, String value) {
		taglist.add(new Tag(name, value));
	}

	/**
	 * Removes a tag from the photo.
	 * @param name The name of the tag.
	 * @param value The value of the tag.
	 */
	public void removeTag(String name, String value) {
		for (int i = 0; i < taglist.size(); i++) {
			Tag cur = taglist.get(i);
			if (cur.name.toLowerCase().equals(name.toLowerCase()) && cur.value.toLowerCase().equals(value.toLowerCase())) {
				taglist.remove(i);
			}
		}
	}

	/**
	 * Checks if a tag exists for the photo.
	 * @param name The name of the tag.
	 * @param value The value of the tag.
	 * @return True if the tag exists, false otherwise.
	 */
	public boolean tagExists(String name, String value) {
		for (int i = 0; i < taglist.size(); i++) {
			Tag cur = taglist.get(i);
			if (cur.name.toLowerCase().equals(name.toLowerCase()) && cur.value.toLowerCase().equals(value.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the date when the photo was taken.
	 * @return The date of the photo.
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Gets the list of tags associated with the photo.
	 * @return The list of tags.
	 */
	public ArrayList<Tag> getTagList() {
		return taglist;
	}

	/**
	 * Sets the file path for the photo.
	 * @param fp The file path to set.
	 */
	public void setFilePath(String fp) {
		this.filepath = fp;
	}

	/**
	 * Gets the file path of the photo.
	 * @return The file path of the photo.
	 */
	public String getFilePath() {
		return filepath;
	}

	/**
	 * Sets the picture file for the photo.
	 * @param pic The picture file to set.
	 */
	public void setPic(File pic) {
		this.pic = pic;
	}

	/**
	 * Gets the picture file of the photo.
	 * @return The picture file of the photo.
	 */
	public File getPic() {
		return this.pic;
	}

	/**
	 * Gets the caption of the photo.
	 * @return The caption of the photo.
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * Gets the name of the photo.
	 * @return The name of the photo.
	 */
	public String getName() {
		return photoname;
	}

	/**
	 * Converts the photo to a string representation (its name).
	 * @return The string representation of the photo.
	 */
	@Override
	public String toString() {
		return getName();
	}

	/**
	 * Saves the state of the photo to a .dat file.
	 * @param pdApp The Photo object to save.
	 * @throws IOException If an I/O error occurs.
	 */
	public static void save(Photo pdApp) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile));
		oos.writeObject(pdApp);
		oos.close();
	}

	/**
	 * Loads a Photo object from a .dat file.
	 * @return The loaded Photo object.
	 * @throws IOException If an I/O error occurs.
	 * @throws ClassNotFoundException If the class of a serialized object cannot be found.
	 */
	public static Photo load() throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storeDir + File.separator + storeFile));
		Photo userList = (Photo) ois.readObject();
		ois.close();
		return userList;
	}
}
