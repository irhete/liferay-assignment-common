package com.nortal.assignment.messagesource.test;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import com.nortal.assignment.messagesource.data.TranslationDAOImpl;
import com.nortal.assignment.messagesource.model.Translation;

public class TranslationDAOtests {

	TranslationDAOImpl translationDAO;
	private EmbeddedDatabase database;

	@Before
	public void setUp() throws SQLException {
		database = new EmbeddedDatabaseBuilder().addScript("schema.sql")
				.addScript("test-data.sql").build();
		configureDB();
	}

	@After
	public void tearDown() throws Exception {
		database.shutdown();
	}

	private void configureDB() {
		SimpleJdbcTemplate jdbcTemplate = new SimpleJdbcTemplate(database);
		translationDAO = new TranslationDAOImpl();
		translationDAO.setJdbcTemplate(jdbcTemplate);
	}

	@Test
	public void testInsert() {
		int beforeCount = translationDAO.getTranslations("fr_FR").size();
		Translation translation = new Translation("fr_FR", "delete",
				"Supprimer");
		translationDAO.insert(translation);
		int afterCount = translationDAO.getTranslations("fr_FR").size();
		assertEquals(1, afterCount - beforeCount);
	}

	@Test
	public void testGetTranslations() {
		List<Translation> translations = translationDAO
				.getTranslations("et_EE");
		assertEquals("Näita tõlkeid", translations.get(1).getValue());
	}

	@Test
	public void testUpdateTranslationValue() {
		Translation translation = new Translation("en_US", "delete", "Remove");
		translation.setId(2);
		translationDAO.updateTranslation(translation);
		List<Translation> translations = translationDAO
				.getTranslations("en_US");
		assertEquals(true, translations.contains(translation));
	}

	@Test
	public void testUpdateTranslationKeyAndValue() {
		int beforeCount = translationDAO.getTranslations("en_US").size();
		Translation translation = new Translation("en_US", "remove", "Remove");
		translation.setId(2);
		translationDAO.updateTranslation(translation);
		List<Translation> translations = translationDAO
				.getTranslations("en_US");
		int afterCount = translations.size();
		assertEquals(0, afterCount - beforeCount);
		assertEquals(true, translations.contains(translation));
	}

	@Test
	public void testDeleteTranslation() {
		int beforeCount = translationDAO.getTranslations("en_US").size();
		translationDAO.deleteTranslation(2);
		int afterCount = translationDAO.getTranslations("en_US").size();
		assertEquals(-1, afterCount - beforeCount);
	}

	@Test
	public void testGetMessageExists() {
		String code = "delete";
		Locale locale = new Locale("et_EE");
		String msg = translationDAO.getMessage(code, locale);
		assertEquals("Kustuta", msg);
	}

	@Test
	public void testGetMessageOnlyEnglishExists() {
		String code = "delete";
		Locale locale = new Locale("fr_FR");
		String msg = translationDAO.getMessage(code, locale);
		assertEquals("Delete", msg);
	}
}
