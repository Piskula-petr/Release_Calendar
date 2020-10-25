package cz.release_calendar.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cz.release_calendar.services.MovieService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api")
public class MoviesCountController {

	@Autowired
	private MovieService movieService;
	
	
	/**
	 * Získání počtu filmů v databázi
	 * 
	 * @return - vrací počet filmů
	 */
	@GetMapping("/movies/count")
	public long getMoviesPreviewCount() {
		
		return movieService.getMoviesCount();
	}
	
}
