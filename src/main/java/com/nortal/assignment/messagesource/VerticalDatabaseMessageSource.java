package com.nortal.assignment.messagesource;

import java.text.MessageFormat;
import java.util.Locale;

import javax.annotation.Resource;

import org.springframework.context.support.AbstractMessageSource;

import com.nortal.assignment.messagesource.data.TranslationDAO;

public class VerticalDatabaseMessageSource extends AbstractMessageSource {

	@Resource
	private TranslationDAO translationDAO;

	@Override
	protected MessageFormat resolveCode(String code, Locale locale) {
		String msg = translationDAO.getMessage(code, locale);
		return createMessageFormat(msg, locale);
	}

}