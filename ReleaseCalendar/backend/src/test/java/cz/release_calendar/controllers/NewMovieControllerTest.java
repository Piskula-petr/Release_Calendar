package cz.release_calendar.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import cz.release_calendar.entities.File;
import cz.release_calendar.entities.Movie;
import cz.release_calendar.services.MovieService;

@WebMvcTest(NewMovieController.class)
public class NewMovieControllerTest {
	
	private Random random;
	private LocalDate localDate;
	
	private byte[] poster;
	private byte[] image;
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private MovieService movieService;
	
	@Mock
	private Clock clock;
	
	
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
		image = Files.readAllBytes(Paths.get("src/test/resources/image.jpg"));
	}
	
	
	/**
	 * Test metody pro uložení filmu
	 * 
	 * @throws Exception
	 */
	@Test
	public void saveMovie() throws Exception {
		
		// Vytvoření testovacích dat
		Movie movie = new Movie();
		movie.setId(random.nextLong());
		movie.setNameCZ("Film");
		movie.setNameEN("Movie");
		movie.setReleaseDate(localDate);
		movie.setPlatform("Platform");
		
		String[] genres = {"Action", "Comedy", "Drama"};
		movie.setGenres(genres);
		
		movie.setCsfdLink("https://www.csfd.cz/");
		movie.setImdbLink("https://www.imdb.com/");
		movie.setDirector("Director");
		
		String[] actors = {"Actor1", "Actor2", "Actor3"};
		movie.setActors(actors);
		movie.setContent("Lorem ipsum dolor sit amet");
		movie.setVideoLink("https://www.youtube.com");
		
		MockMultipartFile file = new MockMultipartFile("file", poster);
		MockMultipartFile files = new MockMultipartFile("files", image);
		
		// Porovnání výstupních hodnot
		mockMvc.perform(multipart("/api/saveMovie")
			.file(file)
			.file(files)
			.param("newMovie", new ObjectMapper().writeValueAsString(movie)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.timestamp").exists())
			.andExpect(jsonPath("$.status").value(200))
			.andExpect(jsonPath("$.message").value("success"));
		
		verify(movieService, times(1)).saveMovie(any(Movie.class), anyList(), any(File.class));
	}
	
}
