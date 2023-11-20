package model;

import java.io.*;
import java.util.ArrayList;

/**
 * The Superuser class manages user-related functions and user persistence.
 * Author: Farhan Shah
 */
public class Superuser implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String storeDir = "data";
	public static final String storeFile = "users.dat";

	private ArrayList<User> users;
	private User current;
	private boolean loggedIn;

	/**
	 * Constructor initializes the Superuser object with an "admin" user.
	 */
	public Superuser() {
		users = new ArrayList<>();
		users.add(new User("admin"));
		this.current = null;
		this.loggedIn = false;
	}

	/**
	 * Adds a new user to the list of users.
	 * @param username The username of the new user.
	 */
	public void addUser(String username) {
		users.add(new User(username));
	}

	/**
	 * Deletes a user based on the given index.
	 * @param index The index of the user to be deleted.
	 */
	public void deleteUser(int index) {
		users.remove(index);
	}

	/**
	 * Deletes a user based on the given username.
	 * @param username The username of the user to be deleted.
	 */
	public void deleteUser(String username) {
		User temp = new User(username);
		users.remove(temp);
	}

	/**
	 * Checks if a user with the given username exists.
	 * @param username The username to check.
	 * @return True if the user exists, false otherwise.
	 */
	public boolean exists(String username) {
		for (User user : users) {
			if (user.getUsername().equals(username)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the provided username and password match a user in the list.
	 * If a match is found, sets the current user and logs in.
	 * @param user The username to check.
	 * @return True if login is successful, false otherwise.
	 */
	public boolean checkUser(String user) {
		int index = 0;
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getUsername().equals(user)) {
				index = i;
			}
		}

		if (index == -1) {
			return false;
		}
		this.setCurrent(users.get(index));
		this.loggedIn = true;
		return true;
	}

	/**
	 * Gets the index of the current user in the list.
	 * @return The index of the current user.
	 */
	public int getUserIndex() {
		int index = 0;
		for (User user : users) {
			if (user.getUsername().equals(current.getUsername())) {
				return index;
			}
			index++;
		}
		return -1;
	}

	/**
	 * Gets the list of users.
	 * @return The list of users.
	 */
	public ArrayList<User> getUsers() {
		return users;
	}

	/**
	 * Gets a user based on the provided username.
	 * @param username The username of the user to retrieve.
	 * @return The User object corresponding to the username.
	 */
	public User getUser(String username) {
		for (User user : users) {
			if (user.getUsername().equals(username)) {
				return user;
			}
		}
		return null;
	}

	/**
	 * Sets the list of users.
	 * @param users The new list of users.
	 */
	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}

	/**
	 * Gets the current user.
	 * @return The current user.
	 */
	public User getCurrent() {
		return current;
	}

	/**
	 * Sets the current user.
	 * @param current The new current user.
	 */
	public void setCurrent(User current) {
		this.current = current;
	}

	/**
	 * Checks if a user is currently logged in.
	 * @return True if a user is logged in, false otherwise.
	 */
	public boolean isLoggedIn() {
		return loggedIn;
	}

	/**
	 * Logs out the current user.
	 */
	public void logout() {
		this.loggedIn = false;
	}

	/**
	 * Saves the state of the Superuser object to a .dat file.
	 * @param superuserInstance The Superuser instance to save.
	 * @throws IOException If an I/O error occurs.
	 */
	public static void save(Superuser superuserInstance) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile));
		oos.writeObject(superuserInstance);
		oos.close();
	}

	/**
	 * Loads a Superuser object from a .dat file.
	 * @return The loaded Superuser object.
	 * @throws IOException            If an I/O error occurs.
	 * @throws ClassNotFoundException If the class of the serialized object cannot be found.
	 */
	public static Superuser load() throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storeDir + File.separator + storeFile));
		Superuser userList = (Superuser) ois.readObject();
		ois.close();
		return userList;
	}
}
