package model;

import java.io.Serializable;

/**
 * Represents tags for users.
 * Author: Farhan Shah
 */
public class Tag implements Serializable {
	// Fields to store tag information
	public String name;
	public String value;

	/**
	 * Constructor to initialize Tag with a name and value.
	 * @param name The name of the tag.
	 * @param value The value of the tag.
	 */
	public Tag(String name, String value) {
		this.name = name;
		this.value = value;
	}

	/**
	 * Compares tag values for equality.
	 * @param o The object to compare with.
	 * @return True if the tags have the same name and value, false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Tag)) {
			return false;
		}

		Tag tag = (Tag) o;

		// Compare the name and value of tags
		return tag.name.equals(this.name) && tag.value.equals(this.value);
	}
}
