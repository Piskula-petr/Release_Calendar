package cz.release_calendar.controllers;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import cz.release_calendar.entities.MoviePreview;
import cz.release_calendar.services.MovieService;

@WebMvcTest(RemoveMovieController.class)
public class RemoveMovieControllerTest {

	private Random random;
	private LocalDate localDate;
	
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
		localDate = LocalDate.now();
		
		poster = Files.readAllBytes(Paths.get("src/test/resources/poster.jpg"));
	}
	
	/**
	 * Test metody pro získání filmů
	 * 
	 * @throws Exception
	 */
	@Test
	public void getMoviesPreview() throws Exception {
		
		List<MoviePreview> movies = new ArrayList<>();
		
		// Vytvoření testovacího filmu
		MoviePreview movie = new MoviePreview();
		movie.setId(random.nextLong());
		movie.setNameCZ("Film");
		movie.setReleaseDate(localDate);
		movie.setPlatform("Platform");
		
		String[] genres = {"Action", "Comedy", "Drama"};
		movie.setGenres(genres);
		movie.setImage(poster);
		movies.add(movie);
		
		when(movieService.getMoviePreview(anyInt())).thenReturn(movies);
		
		int index = random.nextInt(10);
		
		// Porovnání výstupních hodnot
		mockMvc.perform(get("/api/movies/review/index=" + index))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(movies.size()))
			.andExpect(jsonPath("$[0].id").value(movie.getId()))
			.andExpect(jsonPath("$[0].nameCZ").value(movie.getNameCZ()))
			.andExpect(jsonPath("$[0].releaseDate").value(movie.getReleaseDate().toString()))
			.andExpect(jsonPath("$[0].platform").value(movie.getPlatform()))
			.andExpect(jsonPath("$[0].genres.length()").value(movie.getGenres().length))
			.andExpect(jsonPath("$[0].genres[0]").value(movie.getGenres()[0]))
			.andExpect(jsonPath("$[0].image").value(Base64.getEncoder().encodeToString(movie.getImage())));
		
		verify(movieService, times(1)).getMoviePreview(anyInt());
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
