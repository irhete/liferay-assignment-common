package com.nortal.assignment.messagesource.test;


import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

import com.nortal.assignment.messagesource.VerticalDatabaseMessageSource;

@RunWith(MockitoJUnitRunner.class)
public class MessageSourceTests {

	@Mock
	private JdbcTemplate jdbcTemplate;
	
	@InjectMocks
	private VerticalDatabaseMessageSource messageSource;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	

	
}
