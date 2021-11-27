package cz.release_calendar.controllers;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import cz.release_calendar.services.MovieService;

@WebMvcTest(RemoveMovieController.class)
public class RemoveMovieControllerTest {

	private Random random;
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private MovieService movieService;
	
	/**
	 * Inicializace parametrů
	 *
	 */
	@BeforeEach
	public void setUp() {
		
		random = new Random();
	}
	
	
	/**
	 * Test metody pro odstranění filmu
	 * 
	 * @throws Exception
	 */
	@Test
	public void removeMovie() throws Exception {
		
		// ID filmu
		long movieID = random.nextLong();
		
		when(movieService.removeMovie(anyLong())).thenReturn(1);
		
		// Porovnání výstupních hodnot
		mockMvc.perform(post("/api/removeMovie")
			.contentType(MediaType.APPLICATION_JSON)
			.content(new ObjectMapper().writeValueAsString(movieID)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value(200))
			.andExpect(jsonPath("$.message").value("success"))
			.andExpect(jsonPath("$.timestamp").exists());
		
		verify(movieService, times(1)).removeMovie(anyLong());
	}
	
}
