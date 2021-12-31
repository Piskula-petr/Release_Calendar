package cz.release_calendar.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cz.release_calendar.entities.MovieList;
import cz.release_calendar.entities.MovieListDetailed;
import cz.release_calendar.enums.Status;
import cz.release_calendar.pojo.MoviesTotalCount;
import cz.release_calendar.services.MovieService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api")
public class ListController {
	
	@Autowired
	private MovieService movieService;
	
	
	/**
	 * Získání počtu filmů v databázi
	 */
	@GetMapping("/movies/list/count")
	public long getMoviesCount() {
		
		return movieService.getMoviesCount();
	}
	
	
	/**
	 * Získání počtu filmů od dnešního dne
	 */
	@GetMapping("/movies/list/count/today")
	public MoviesTotalCount getMoviesCountByToday() {
		
		MoviesTotalCount moviesTotalCount = new MoviesTotalCount();
		moviesTotalCount.setNextTotal(movieService.getMoviesCountByToday(Status.next));
		moviesTotalCount.setPreviousTotal(movieService.getMoviesCountByToday(Status.previous));
		
		return moviesTotalCount;
	}

	
	/**
	 * Získání pole filmů
	 * 
	 * @param startIndex - počáteční index
	 */
	@GetMapping("/movies/list/index={startIndex}")
	public List<MovieList> getMoviesList(@PathVariable("startIndex") int startIndex) {
		
		List<MovieList> movies = movieService.getMoviesList(startIndex);
		
		return movies;
	}
	
	
	/**
	 * Získání detailního pole filmů
	 * 
	 * @param startIndex - počáteční index
	 * @param status - enum [další / předchozí]
	 */
	@GetMapping("/movies/list/detailed/index={startIndex}&{status}&limit={limit}")
	public List<MovieListDetailed> getMoviesListDetailed(@PathVariable("startIndex") int startIndex,
													     @PathVariable("status") Status status, 
														 @PathVariable("limit") int limit) {
			
		List<MovieListDetailed> movies = movieService.getMoviesListDetailed(startIndex, status, limit);
		
		return movies;
	}
	
	
	/**
	 * Získání filmu, podle ID
	 * 
	 * @param movieID - id filmu
	 */
	@GetMapping("/movies/list/movieID={movieID}")
	public MovieList getMovieName(@PathVariable("movieID") long movieID) {
		
		MovieList movieList = movieService.getMovieList(movieID);
		
		return movieList;
	}
	
}
