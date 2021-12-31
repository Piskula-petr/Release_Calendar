package cz.release_calendar.controllers;

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
public class DetailController {

	@Autowired
	private MovieService movieService;
	
	
	/**
	 * Získání detailu filmu
	 * 
	 * @param movieID - ID filmu
	 */
	@GetMapping("/movie/{movieID}")
	public Movie getMovieDatail(@PathVariable("movieID") long movieID) {
		
		Movie movie = movieService.getMovieDetail(movieID);
		
		return movie;
	}
	
}
