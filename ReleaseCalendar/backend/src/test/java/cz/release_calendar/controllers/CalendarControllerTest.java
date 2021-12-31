package cz.release_calendar.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import cz.release_calendar.entities.MovieCalendar;
import cz.release_calendar.services.MovieService;

@WebMvcTest(CalendarController.class)
public class CalendarControllerTest {
	
	private List<MovieCalendar> movies;
	
	private Random random;
	private LocalDate date;
	
	private byte[] poster;
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MovieService movieService;
	
	
	/**
	 * Inicializace parametrů
	 * 
	 * @throws Exception
	 */
	@BeforeEach 
	public void setUp() throws Exception {
		
		random = new Random();
		movies = new ArrayList<>();
		date = LocalDate.now();
		
		poster = Files.readAllBytes(Paths.get("src/test/resources/poster.jpg"));
	}
	
	
	/**
	 * Test metody pro získání filmů v zadaném měsíci
	 * 
	 * @throws Exception
	 */
	@Test
	public void getMoviesCalendar() throws Exception {
		
		// Vytvoření testovacího filmu
		MovieCalendar movie = new MovieCalendar();
		movie.setId(random.nextLong());
		movie.setNameCZ("Film");
		movie.setNameEN("Movie");
		movie.setReleaseDate(date);
		movie.setImage(poster);
		movies.add(movie);
		
		when(movieService.getMoviesCalendar(any(LocalDate.class), any(LocalDate.class))).thenReturn(movies);
		
		int year = date.getYear();
		int month = date.getMonthValue();

		// Porovnání výstupních hodnot
		mockMvc.perform(get("/api/movies/calendar/year=" + year + "&month=" + month))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(movies.size()))
			.andExpect(jsonPath("$[0].id").value(movie.getId()))
			.andExpect(jsonPath("$[0].nameCZ").value(movie.getNameCZ()))
			.andExpect(jsonPath("$[0].nameEN").value(movie.getNameEN()))
			.andExpect(jsonPath("$[0].releaseDate").value(movie.getReleaseDate().toString()))
			.andExpect(jsonPath("$[0].image").value(Base64.getEncoder().encodeToString(movie.getImage())));
		
		verify(movieService, times(1)).getMoviesCalendar(any(LocalDate.class), any(LocalDate.class));
	}
	
}
