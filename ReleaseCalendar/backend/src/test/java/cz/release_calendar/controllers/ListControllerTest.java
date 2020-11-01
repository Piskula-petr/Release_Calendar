package cz.release_calendar.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
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

import cz.release_calendar.entities.MovieList;
import cz.release_calendar.enums.Status;
import cz.release_calendar.pojo.MoviesTotalCount;
import cz.release_calendar.services.MovieService;

@WebMvcTest(ListController.class)
public class ListControllerTest {

	private Random random;
	private LocalDate localDate;
	
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
		localDate = LocalDate.now();
	}
	
	
	/**
	 * Test metody pro získání počtu filmů
	 * 
	 * @throws Exception
	 */
	@Test
	public void getMoviesFromTodayCount() throws Exception {
		
		// Vytvoření testovacího objektu
		MoviesTotalCount moviesTotalCount = new MoviesTotalCount();
		moviesTotalCount.setNextTotal(random.nextLong());
		moviesTotalCount.setPreviousTotal(random.nextLong());
		
		when(movieService.getMoviesFromTodayCount(any(LocalDate.class), eq(Status.next))).thenReturn(moviesTotalCount.getNextTotal());
		when(movieService.getMoviesFromTodayCount(any(LocalDate.class), eq(Status.previous))).thenReturn(moviesTotalCount.getPreviousTotal());
		
		// Porovnání výstupních hodnot
		mockMvc.perform(get("/api/movies/list/count"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.nextTotal").value(moviesTotalCount.getNextTotal()))
			.andExpect(jsonPath("$.previousTotal").value(moviesTotalCount.getPreviousTotal()));
	
		verify(movieService, times(2)).getMoviesFromTodayCount(any(LocalDate.class), any(Status.class));
	}
	
	
	/**
	 * Test metody pro získání seznamu filmů
	 * 
	 * @throws Exception
	 */
	@Test
	public void getMoviesFromToday() throws Exception {
		
		List<MovieList> movies = new ArrayList<>();
		
		// Vytvoření testovacího objektu
		MovieList movie = getMovieListForTest();
		movies.add(movie);
		
		when(movieService.getMoviesFromToday(any(LocalDate.class), anyInt(), any(Status.class))).thenReturn(movies);
		
		int index = random.nextInt(10);
		
		// Porovnání výstupních hodnot
		mockMvc.perform(get("/api/movies/list/index=" + index + "&" + Status.next))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(movies.size()))
			.andExpect(jsonPath("$[0].id").value(movie.getId()))
			.andExpect(jsonPath("$[0].nameCZ").value(movie.getNameCZ()))
			.andExpect(jsonPath("$[0].nameEN").value(movie.getNameEN()))
			.andExpect(jsonPath("$[0].releaseDate").value(movie.getReleaseDate().toString()))
			.andExpect(jsonPath("$[0].platform").value(movie.getPlatform()))
			.andExpect(jsonPath("$[0].director").value(movie.getDirector()))
			.andExpect(jsonPath("$[0].genres.length()").value(movie.getGenres().length))
			.andExpect(jsonPath("$[0].genres[0]").value(movie.getGenres()[0]))
			.andExpect(jsonPath("$[0].actors.length()").value(movie.getActors().length))
			.andExpect(jsonPath("$[0].actors[0]").value(movie.getActors()[0]))
			.andExpect(jsonPath("$[0].image").value(Base64.getEncoder().encodeToString(movie.getImage())));
	
		verify(movieService, times(1)).getMoviesFromToday(any(LocalDate.class), anyInt(), any(Status.class));
	}
	
	
	/**
	 * Test metody pro získání limitovaného seznamů filmů
	 * 
	 * @throws Exception
	 */
	@Test
	public void getMoviesFromTodayLimited() throws Exception {
		
		List<MovieList> movies = new ArrayList<>();
		
		// Vytvoření testovacího objektu
		MovieList movie = getMovieListForTest();
		movies.add(movie);
		
		when(movieService.getMoviesFromTodayLimited(any(LocalDate.class), anyInt(), any(Status.class))).thenReturn(movies);
		
		int limit = random.nextInt(10);
		
		// Porovnání výstupních hodnot
		mockMvc.perform(get("/api//movies/list/limit=" + limit + "&" + Status.next))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(movies.size()))
			.andExpect(jsonPath("$[0].id").value(movie.getId()))
			.andExpect(jsonPath("$[0].nameCZ").value(movie.getNameCZ()))
			.andExpect(jsonPath("$[0].nameEN").value(movie.getNameEN()))
			.andExpect(jsonPath("$[0].releaseDate").value(movie.getReleaseDate().toString()))
			.andExpect(jsonPath("$[0].platform").value(movie.getPlatform()))
			.andExpect(jsonPath("$[0].director").value(movie.getDirector()))
			.andExpect(jsonPath("$[0].genres.length()").value(movie.getGenres().length))
			.andExpect(jsonPath("$[0].genres[0]").value(movie.getGenres()[0]))
			.andExpect(jsonPath("$[0].actors.length()").value(movie.getActors().length))
			.andExpect(jsonPath("$[0].actors[0]").value(movie.getActors()[0]))
			.andExpect(jsonPath("$[0].image").value(Base64.getEncoder().encodeToString(movie.getImage())));
	
		verify(movieService, times(1)).getMoviesFromTodayLimited(any(LocalDate.class), anyInt(), any(Status.class));
	}
	
	
	/**
	 * Vytvoření testovacího objektu
	 * 
	 * @return - vrací testovací objektu
	 * 
	 * @throws Exception
	 */
	private MovieList getMovieListForTest() throws Exception {
		
		MovieList movie = new MovieList();
		movie.setId(random.nextLong());
		movie.setNameCZ("Film");
		movie.setNameEN("Movie");
		movie.setReleaseDate(localDate);
		movie.setPlatform("Platform");
		movie.setDirector("Director");
		
		String[] genres = {"Action", "Comedy", "Drama"};
		movie.setGenres(genres);
		
		String[] actors = {"Actor1", "Actor2", "Actor3"};
		movie.setActors(actors);
		movie.setImage(Files.readAllBytes(Paths.get("src/test/resources/poster.jpg")));
		
		return movie;
	}
	
}
