package com.nortal.assignment.messagesource;

import java.util.List;

import org.springframework.dao.DuplicateKeyException;


/**
 * Services for adding and retrieving translations
 */
public interface TranslationDAO {

	/**
	 * Inserts translation to database.
	 * 
	 * @param translation
	 *            Translation.
	 * @throws DuplicateKeyException
	 */
	public void insert(Translation translation) throws DuplicateKeyException;

	/**
	 * Retrieves translations for a language.
	 * 
	 * @param language
	 *            to query the translations for String.
	 * @return List of translations
	 */
	public List<Translation> getTranslations(String locale);

	/**
	 * Updates a translation's value by its key.
	 * 
	 * @param translation
	 *            . Key and value must not be null. Value is the updated value.
	 * @throws DuplicateKeyException
	 */
	public void updateTranslation(Translation translation)
			throws DuplicateKeyException;;

	/**
	 * Deletes a translation from the database.
	 * 
	 * @param id
	 *            . The id of the translation to be deleted.
	 */
	public void deleteTranslation(int translationId);

	public Messages getMessages();
}
