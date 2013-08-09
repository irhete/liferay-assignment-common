package com.nortal.assignment.messagesource.test;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.List;

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
		int beforeCount = translationDAO.getTranslations("FR").size();
		Translation translation = new Translation("FR", "delete", "Supprimer");
		translationDAO.insert(translation);
		int afterCount = translationDAO.getTranslations("FR").size();
		assertEquals(1, afterCount - beforeCount);
	}

	@Test
	public void testGetTranslations() {
		List<Translation> translations = translationDAO.getTranslations("ET");
		assertEquals("Näita tõlkeid", translations.get(1).getValue());
	}

	@Test
	public void testUpdateTranslationValue() {
		Translation translation = new Translation("EN", "delete", "Remove");
		translationDAO.updateTranslation(translation);
		List<Translation> translations = translationDAO.getTranslations("EN");
		assertEquals(true, translations.contains(translation));
	}

	@Test
	public void testUpdateTranslationKeyAndValue() {
		int beforeCount = translationDAO.getTranslations("EN").size();
		Translation translation = new Translation("EN", "remove", "Remove");
		translationDAO.updateTranslation(translation);
		List<Translation> translations = translationDAO.getTranslations("EN");
		int afterCount = translations.size();
		assertEquals(0, afterCount - beforeCount);
		assertEquals(true, translations.contains(translation));
	}

	@Test
	public void testDeleteTranslation() {
		int beforeCount = translationDAO.getTranslations("EN").size();
		translationDAO.deleteTranslation(1);
		int afterCount = translationDAO.getTranslations("EN").size();
		assertEquals(-1, afterCount - beforeCount);
	}

}
