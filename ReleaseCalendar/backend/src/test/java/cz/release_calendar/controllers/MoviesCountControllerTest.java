package cz.release_calendar.controllers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import cz.release_calendar.services.MovieService;

@WebMvcTest(MoviesCountController.class)
public class MoviesCountControllerTest {

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
	 * Test metody pro získání počtů filmů
	 * 
	 * @throws Exception
	 */
	@Test
	public void getMoviesPreviewCount() throws Exception {
		
		// Počet filmů
		long movieCount = random.nextLong();
		
		when(movieService.getMoviesCount()).thenReturn(movieCount);
		
		// Porovnání výstupních hodnot
		mockMvc.perform(get("/api/movies/count"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").value(movieCount));
		
		verify(movieService, times(1)).getMoviesCount();
	}
	
}
