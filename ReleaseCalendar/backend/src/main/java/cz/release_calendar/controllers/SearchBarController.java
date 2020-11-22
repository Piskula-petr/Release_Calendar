package cz.release_calendar.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
	 * @return - vrací List názvů filmů
	 */
	@GetMapping("/movies/names")
	public List<MovieName> getMovieNames() {
		
		List<MovieName> movieNames = movieService.getMovieNames();
		
		return movieNames;
	}
	
}
