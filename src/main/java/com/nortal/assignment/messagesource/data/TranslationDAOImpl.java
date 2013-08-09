package com.nortal.assignment.messagesource.data;

import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

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

	@CacheEvict(value = "retrieveMessage", allEntries = true)
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

	@CacheEvict(value = "retrieveMessage", allEntries = true)
	public void updateTranslation(Translation translation) {
		final String sql = "UPDATE translation SET value = :value, key = :key WHERE id = :id";
		SqlParameterSource parameters = new BeanPropertySqlParameterSource(
				translation);
		jdbcTemplate.update(sql, parameters);
	}

	@CacheEvict(value = "retrieveMessage", allEntries = true)
	public void deleteTranslation(int translationId) {
		final String sql = "DELETE FROM translation WHERE id = :translationId";
		jdbcTemplate.update(sql, translationId);
	}

	@Override
	@Cacheable("retrieveMessage")
	public String getMessage(String code, Locale locale) {
		final String sql = "SELECT t.value FROM translation t JOIN language l ON l.id = t.language WHERE t.key = :code AND LOWER(l.locale) = :locale";
		try {
			return jdbcTemplate.queryForObject(sql, String.class, code, locale
					.toString().toLowerCase());
		} catch (EmptyResultDataAccessException e) {
			try {
				return jdbcTemplate.queryForObject(sql, String.class, code,
						"en_us");
			} catch (EmptyResultDataAccessException e2) {
				return null;
			}
		}
	}
}
