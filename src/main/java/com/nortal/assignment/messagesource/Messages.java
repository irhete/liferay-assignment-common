package com.nortal.assignment.messagesource;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class Messages {

	/* <code, <locale, message>> */
	private Map<String, Map<Locale, String>> messages = new HashMap<String, Map<Locale, String>>();

	public void addMessage(String key, Locale locale, String value) {
		Map<Locale, String> data = messages.get(key);
		if (data == null) {
			data = new HashMap<Locale, String>();
			messages.put(key, data);
		}
		data.put(locale, value);
	}

	public String getMessage(String code, Locale locale) {
		Locale currentLocale = new Locale(locale.toString());
		Map<Locale, String> data = messages.get(code);
		if (data == null) {
			return null;
		} else if (data.get(currentLocale) != null) {
			return data.get(currentLocale);
		} else {
			return data.get(new Locale("en_US"));
		}
	}
}
