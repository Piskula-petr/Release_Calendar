package cz.release_calendar.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ControllersTest {

	@Autowired
	private CalendarController calendarController;
	
	@Autowired
	private ListController listController;
	
	@Autowired
	private DetailController detailController;
	
	@Autowired
	private NewMovieController newMovieController;
	
	@Autowired
	private RemoveMovieController removeMovieController;
	
	
	@Test
	public void contextLoads() throws Exception {
		
		assertThat(calendarController).isNotNull();
		assertThat(listController).isNotNull();
		assertThat(detailController).isNotNull();
		assertThat(newMovieController).isNotNull();
		assertThat(removeMovieController).isNotNull();
	}

}
