package edu.caltech.cs141b.hw2.gwt.collab.shared;

import java.io.Serializable;

public class UserValue implements Serializable {
	private String key;
	private String name;

	public UserValue(String key, String name) {
		this.key = key;
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public String getName() {
		return name;
	}
	
	public boolean equals(UserValue user) {
		return (this.key).equals(user.getKey());
	}
}
