package cz.release_calendar.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
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
import cz.release_calendar.entities.MovieListDetailed;
import cz.release_calendar.enums.Status;
import cz.release_calendar.pojo.MoviesTotalCount;
import cz.release_calendar.services.MovieService;

@WebMvcTest(ListController.class)
public class ListControllerTest {

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
	public void setUp() throws IOException {
		
		random = new Random();
		localDate = LocalDate.now();
		
		poster = Files.readAllBytes(Paths.get("src/test/resources/poster.jpg"));
	}
	
	
	/**
	 * Test metody pro získání počtů filmů
	 * 
	 * @throws Exception
	 */
	@Test
	public void getMoviesCount() throws Exception {
		
		// Počet filmů
		long movieCount = random.nextLong();
		
		when(movieService.getMoviesCount()).thenReturn(movieCount);
		
		// Porovnání výstupních hodnot
		mockMvc.perform(get("/api/movies/list/count"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").value(movieCount));
		
		verify(movieService, times(1)).getMoviesCount();
	}
	
	
	/**
	 * Test metody pro získání počtu filmů od dnešního dne
	 * 
	 * @throws Exception
	 */
	@Test
	public void getMoviesCountByToday() throws Exception {
		
		// Vytvoření testovacího objektu
		MoviesTotalCount moviesTotalCount = new MoviesTotalCount();
		moviesTotalCount.setNextTotal(random.nextLong());
		moviesTotalCount.setPreviousTotal(random.nextLong());
		
		when(movieService.getMoviesCountByToday(eq(Status.next))).thenReturn(moviesTotalCount.getNextTotal());
		when(movieService.getMoviesCountByToday(eq(Status.previous))).thenReturn(moviesTotalCount.getPreviousTotal());
		
		// Porovnání výstupních hodnot
		mockMvc.perform(get("/api/movies/list/count/today"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.nextTotal").value(moviesTotalCount.getNextTotal()))
			.andExpect(jsonPath("$.previousTotal").value(moviesTotalCount.getPreviousTotal()));
	
		verify(movieService, times(2)).getMoviesCountByToday(any(Status.class));
	}
	
	
	/**
	 * Test metody pro získání filmů
	 * 
	 * @throws Exception
	 */
	@Test
	public void getMoviesList() throws Exception {
		
		List<MovieList> movies = new ArrayList<>();
		
		// Vytvoření testovacího filmu
		MovieList movie = new MovieList();
		movie.setId(random.nextLong());
		movie.setNameCZ("Film");
		movie.setReleaseDate(localDate);
		movie.setPlatform("Platform");
		
		String[] genres = {"Action", "Comedy", "Drama"};
		movie.setGenres(genres);
		movie.setImage(poster);
		movies.add(movie);
		
		when(movieService.getMoviesList(anyInt())).thenReturn(movies);
		
		int index = random.nextInt(10);
		
		// Porovnání výstupních hodnot
		mockMvc.perform(get("/api/movies/list/index=" + index))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(movies.size()))
			.andExpect(jsonPath("$[0].id").value(movie.getId()))
			.andExpect(jsonPath("$[0].nameCZ").value(movie.getNameCZ()))
			.andExpect(jsonPath("$[0].releaseDate").value(movie.getReleaseDate().toString()))
			.andExpect(jsonPath("$[0].platform").value(movie.getPlatform()))
			.andExpect(jsonPath("$[0].genres.length()").value(movie.getGenres().length))
			.andExpect(jsonPath("$[0].genres[0]").value(movie.getGenres()[0]))
			.andExpect(jsonPath("$[0].image").value(Base64.getEncoder().encodeToString(movie.getImage())));
		
		verify(movieService, times(1)).getMoviesList(anyInt());
	}
	
	
	/**
	 * Test metody pro získání detailního limitovaného seznamu filmů
	 * 
	 * @throws Exception
	 */
	@Test
	public void getMoviesListDetailed() throws Exception {
		
		List<MovieListDetailed> movies = new ArrayList<>();
		
		// Vytvoření testovacího filmu
		MovieListDetailed movie = getMovieListForTest();
		movies.add(movie);
		
		when(movieService.getMoviesListDetailed(anyInt(), any(Status.class), anyInt())).thenReturn(movies);
		
		int index = random.nextInt(10);
		int limit = random.nextInt(10);
		
		// Porovnání výstupních hodnot
		mockMvc.perform(get("/api/movies/list/detailed/index=" + index + "&" + Status.next + "&limit=" + limit))
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
	
		verify(movieService, times(1)).getMoviesListDetailed(anyInt(), any(Status.class), anyInt());
	}
	
	
	/**
	 * Test metody pro získání filmu, podle ID
	 * 
	 * @throws Exception
	 */
	@Test
	public void getMovieName() throws Exception {
		
		// Vytvoření testovacího filmu
		MovieList movieList = new MovieList();
		movieList.setId(random.nextLong());
		movieList.setNameCZ("Film");
		movieList.setNameEN("Movie");
		movieList.setReleaseDate(localDate);
		movieList.setPlatform("Platform");
		
		String[] genres = {"Action", "Comedy", "Drama"};
		movieList.setGenres(genres);
		movieList.setImage(poster);
		
		when(movieService.getMovieList(anyLong())).thenReturn(movieList);
		
		long movieID = random.nextLong();
		
		// Porovnání výstupních hodnot
		mockMvc.perform(get("/api/movies/list/movieID=" + movieID))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(movieList.getId()))
			.andExpect(jsonPath("$.nameCZ").value(movieList.getNameCZ()))
			.andExpect(jsonPath("$.nameEN").value(movieList.getNameEN()))
			.andExpect(jsonPath("$.releaseDate").value(movieList.getReleaseDate().toString()))
			.andExpect(jsonPath("$.platform").value(movieList.getPlatform()))
			.andExpect(jsonPath("$.genres[0]").value(movieList.getGenres()[0]))
			.andExpect(jsonPath("$.image").value(Base64.getEncoder().encodeToString(movieList.getImage())));
		
		verify(movieService, times(1)).getMovieList(anyLong());
	}
	
	
	/**
	 * Vytvoření testovacího filmu
	 * 
	 * @return - vrací testovací film
	 * 
	 * @throws Exception
	 */
	private MovieListDetailed getMovieListForTest() throws Exception {
		
		MovieListDetailed movie = new MovieListDetailed();
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
