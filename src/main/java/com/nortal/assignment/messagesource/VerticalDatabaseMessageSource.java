package com.nortal.assignment.messagesource;

import java.text.MessageFormat;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.context.support.AbstractMessageSource;

public class VerticalDatabaseMessageSource extends AbstractMessageSource {

	private Messages messages;

	@Resource
	private TranslationDAO translationDAO;

	@Override
	protected MessageFormat resolveCode(String code, Locale locale) {
		String msg = messages.getMessage(code, locale);
		return createMessageFormat(msg, locale);
	}

	@PostConstruct
	public void init() {

		messages = translationDAO.getMessages();
	}

}