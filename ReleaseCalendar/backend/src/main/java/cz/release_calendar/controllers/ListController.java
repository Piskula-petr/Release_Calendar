package cz.release_calendar.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cz.release_calendar.entities.ListMovie;
import cz.release_calendar.enums.Status;
import cz.release_calendar.services.MovieService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api")
public class ListController {
	
	@Autowired
	private MovieService movieService;
	
	/**
	 * Získání filmů
	 * 
	 * @return - vrací pole filmů
	 */
	@GetMapping("/movies/list/index={startIndex}&{status}")
	public List<ListMovie> getMoviesAfterToday(@PathVariable("startIndex") int startIndex,
											   @PathVariable("status") Status status) {
			
		List<ListMovie> movies = movieService.getMoviesFromToday(LocalDate.now(), startIndex, status);
		
		return movies;
	}
	
}
