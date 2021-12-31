package cz.release_calendar.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cz.release_calendar.entities.MovieName;
import cz.release_calendar.services.MovieService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api")
public class SearchBarController {

	@Autowired
	private MovieService movieService;
	
	
	/**
	 * Získání názvů filmů
	 * 
	 * @param value - hodnota názvu filmu
	 */
	@GetMapping("/movies/search/value={value}")
	public List<MovieName> getMovieNames(@PathVariable("value") String value) {
		
		List<MovieName> movieNames = movieService.getMovieNames(value);
		
		return movieNames;
	}
	
}
