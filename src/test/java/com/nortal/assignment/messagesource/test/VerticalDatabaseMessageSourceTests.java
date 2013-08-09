package com.nortal.assignment.messagesource.test;

import static org.junit.Assert.assertEquals;

import java.text.MessageFormat;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.nortal.assignment.messagesource.VerticalDatabaseMessageSource;
import com.nortal.assignment.messagesource.data.TranslationDAO;

@RunWith(MockitoJUnitRunner.class)
public class VerticalDatabaseMessageSourceTests {

	@Mock
	private TranslationDAO translationDAO;

	@InjectMocks
	private VerticalDatabaseMessageSource messageSource;

	@Test
	public void testResolveCode() {
		String code = "delete";
		Locale locale = new Locale("en_US");
		String msg = "Delete";
		Mockito.when(translationDAO.getMessage(code, locale)).thenReturn(msg);
		MessageFormat actualFormat = messageSource.resolveCode(code, locale);
		MessageFormat expectedFormat = new MessageFormat(msg, locale);
		assertEquals(expectedFormat, actualFormat);
	}
}
