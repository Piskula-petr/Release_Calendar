package cz.release_calendar.controllers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import cz.release_calendar.entities.MovieName;
import cz.release_calendar.services.MovieService;

@WebMvcTest
public class SearchBarControllerTest {

	private Random random;
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private MovieService movieService;
	
	
	/**
	 * Inicializace parametrů
	 */
	@BeforeEach
	public void setUp() {
		
		random = new Random();
	}
	
	
	/**
	 * Test metody pro získání názvů filmů
	 * 
	 * @throws Exception
	 */
	@Test
	public void getMovieNames() throws Exception {
		
		List<MovieName> movieNames = new ArrayList<>();
		
		// Vytvoření testovacího filmu
		MovieName movieName = new MovieName();
		movieName.setId(random.nextLong());
		movieName.setNameCZ("nameCZ");
		movieName.setNameEN("nameEN");
		movieNames.add(movieName);
		
		String value = "value";
		
		when(movieService.getMovieNames(value)).thenReturn(movieNames);
		
		// Porovnání výstupních hodnot
		mockMvc.perform(get("/api/movies/search/value=" + value))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(movieNames.size()))
			.andExpect(jsonPath("$[0].id").value(movieName.getId()))
			.andExpect(jsonPath("$[0].nameCZ").value(movieName.getNameCZ()))
			.andExpect(jsonPath("$[0].nameEN").value(movieName.getNameEN()));
		
		verify(movieService, times(1)).getMovieNames(value);
	}
	
}
