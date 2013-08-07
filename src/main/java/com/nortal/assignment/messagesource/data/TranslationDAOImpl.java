package com.nortal.assignment.messagesource.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

import com.nortal.assignment.messagesource.Messages;
import com.nortal.assignment.messagesource.model.Translation;

@SuppressWarnings({ "deprecation" })
@Repository
public class TranslationDAOImpl implements TranslationDAO {

	@Resource(name = "dataSource")
	private DriverManagerDataSource dataSource;

	private SimpleJdbcTemplate jdbcTemplate;

	public DriverManagerDataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DriverManagerDataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setJdbcTemplate(SimpleJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@PostConstruct
	public void init() {
		jdbcTemplate = new SimpleJdbcTemplate(dataSource);
	}

	public void insert(final Translation translation)
			throws DuplicateKeyException {
		final String sql = "INSERT INTO translation "
				+ "(language, key, value) VALUES ((SELECT id FROM language WHERE locale = :locale), :key, :value)";
		SqlParameterSource parameters = new BeanPropertySqlParameterSource(
				translation);
		jdbcTemplate.update(sql, parameters);
	}

	public List<Translation> getTranslations(final String locale) {
		String sql = "SELECT t.id, t.key, t.value, l.locale FROM translation t JOIN language l ON l.id = t.language WHERE l.locale = :locale";
		List<Translation> translations = jdbcTemplate.query(sql,
				new BeanPropertyRowMapper<Translation>(Translation.class),
				locale);
		return translations;
	}

	public void updateTranslation(Translation translation) {
		final String sql = "UPDATE translation SET value = :value, key = :key WHERE id = :id";
		SqlParameterSource parameters = new BeanPropertySqlParameterSource(
				translation);
		jdbcTemplate.update(sql, parameters);
	}

	public void deleteTranslation(int translationId) {
		final String sql = "DELETE FROM translation WHERE id = :translationId";
		jdbcTemplate.update(sql, translationId);
	}

	public Messages getMessages() {
		final String sql = "SELECT l.locale, t.key, t.value FROM translation t JOIN language l ON l.id = t.language";
		return jdbcTemplate.getJdbcOperations().query(sql,
				new ResultSetExtractor<Messages>() {

					public Messages extractData(ResultSet rs)
							throws SQLException, DataAccessException {

						Messages messages = new Messages();
						while (rs.next()) {
							Locale locale = new Locale(rs.getString("locale"));
							messages.addMessage(rs.getString("key"), locale,
									rs.getString("value"));
						}
						return messages;
					}
				});

	}
}
