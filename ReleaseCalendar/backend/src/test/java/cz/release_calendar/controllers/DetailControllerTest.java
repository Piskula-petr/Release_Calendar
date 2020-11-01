package cz.release_calendar.controllers;

import static org.mockito.ArgumentMatchers.anyLong;
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

import cz.release_calendar.entities.Movie;
import cz.release_calendar.services.MovieService;

@WebMvcTest(DetailController.class)
public class DetailControllerTest {

	private Random random;
	private LocalDate localDate;
	
	private byte[] poster;
	private byte[] image;
	private List<byte[]> images;
	
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
	private void setUp() throws Exception {
		
		random = new Random();
		localDate = LocalDate.now();
		
		poster = Files.readAllBytes(Paths.get("src/test/resources/poster.jpg"));
		image = Files.readAllBytes(Paths.get("src/test/resources/image.jpg"));
		
		images = new ArrayList<>();
		images.add(image);
	}
	
	
	/**
	 * Test metody pro získání detailu filmu
	 * 
	 * @throws Exception
	 */
	@Test
	public void getMovieDatail() throws Exception {
		
		// Vytvoření testovacího filmu
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
		movie.setImage(poster);
		movie.setImages(images);
		
		when(movieService.getMovieByID(anyLong())).thenReturn(movie);
		
		long movieID = random.nextLong();
		
		// Porovnání výstupních hodnot
		mockMvc.perform(get("/api/movie/" + movieID))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(movie.getId()))
			.andExpect(jsonPath("$.nameCZ").value(movie.getNameCZ()))
			.andExpect(jsonPath("$.nameEN").value(movie.getNameEN()))
			.andExpect(jsonPath("$.releaseDate").value(movie.getReleaseDate().toString()))
			.andExpect(jsonPath("$.platform").value(movie.getPlatform()))
			.andExpect(jsonPath("$.genres.length()").value(movie.getGenres().length))
			.andExpect(jsonPath("$.genres[0]").value(movie.getGenres()[0]))
			.andExpect(jsonPath("$.csfdLink").value(movie.getCsfdLink()))
			.andExpect(jsonPath("$.imdbLink").value(movie.getImdbLink()))
			.andExpect(jsonPath("$.director").value(movie.getDirector()))
			.andExpect(jsonPath("$.actors.length()").value(movie.getActors().length))
			.andExpect(jsonPath("$.actors[0]").value(movie.getActors()[0]))
			.andExpect(jsonPath("$.content").value(movie.getContent()))
			.andExpect(jsonPath("$.videoLink").value(movie.getVideoLink()))
			.andExpect(jsonPath("$.image").value(Base64.getEncoder().encodeToString(movie.getImage())))
			.andExpect(jsonPath("$.images.length()").value(movie.getImages().size()))
			.andExpect(jsonPath("$.images[0]").value(Base64.getEncoder().encodeToString(movie.getImages().get(0))));
	
		verify(movieService, times(1)).getMovieByID(anyLong());
	}
	
}
