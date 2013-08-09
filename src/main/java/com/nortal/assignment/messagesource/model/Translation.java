package com.nortal.assignment.messagesource.model;

import java.io.Serializable;

public class Translation implements Serializable {

	private String locale;
	private String key;
	private String value;
	private int id;

	public Translation(String locale, String key, String value) {
		setLocale(locale);
		setKey(key);
		setValue(value);
	}

	public Translation() {
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (other == this) {
			return true;
		}
		if (!(other instanceof Translation)) {
			return false;
		}
		Translation t1 = (Translation) other;
		return t1.locale.equals(locale) && t1.key.equals(key)
				&& t1.value.equals(value);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
