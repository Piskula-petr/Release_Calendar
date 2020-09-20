package cz.release_calendar.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cz.release_calendar.entities.Movie;
import cz.release_calendar.services.MovieService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api")
public class MovieController {

	@Autowired
	private MovieService movieService;
	
	/**
	 * Získání filmů v zadaném měsíci
	 * 
	 * @param year - rok
	 * @param month - měsíc
	 * 
	 * @return - vrací List filmů
	 */
	@GetMapping("/movies/year={year}&month={month}")
	public List<Movie> getMoviesOfMonth(@PathVariable("year") int year, 
								 		@PathVariable("month") int month) {
		
		// Začátek měsíce "2020-01-01"
		LocalDate startOfMonth = LocalDate.of(year, month, 1);
		
		// Konec měsíce "2020-01-31"
		LocalDate endOfMonth = LocalDate.of(year, month, startOfMonth.lengthOfMonth());
		
		List<Movie> movies = movieService.getMovies(startOfMonth, endOfMonth);
		
		return movies;
	}
	
}
