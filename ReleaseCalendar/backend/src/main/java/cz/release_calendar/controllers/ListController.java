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
import cz.release_calendar.pojo.MoviesTotalCount;
import cz.release_calendar.services.MovieService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api")
public class ListController {
	
	@Autowired
	private MovieService movieService;
	
	
	/**
	 * Získání počtu záznamů
	 * 
	 * @return - vrací počet záznamů
	 */
	@GetMapping("/movies/list/count")
	public MoviesTotalCount getMoviesFromTodayCount() {
		
		MoviesTotalCount moviesTotalCount = new MoviesTotalCount();
		moviesTotalCount.setNextTotal(movieService.getMoviesFromTodayCount(LocalDate.now(), Status.next));
		moviesTotalCount.setPreviousTotal(movieService.getMoviesFromTodayCount(LocalDate.now(), Status.previous));
		
		return moviesTotalCount;
	}
	
	
	/**
	 * Získání filmů od dnešního data
	 * 
	 * @param startIndex - počáteční index
	 * @param status - enum [další / předchozí]
	 * 
	 * @return - vrací pole filmů
	 */
	@GetMapping("/movies/list/index={startIndex}&{status}")
	public List<ListMovie> getMoviesFromToday(@PathVariable("startIndex") int startIndex,
											  @PathVariable("status") Status status) {
			
		List<ListMovie> movies = movieService.getMoviesFromToday(LocalDate.now(), startIndex, status);
		
		return movies;
	}
	
	
	/**
	 * Získání limitovaného seznamu filmů od dnešního data
	 * 
	 * @param limit - limit výstupů
	 * @param staus - enum [další / předchozí]
	 * 
	 * @return - vrací limitované pole filmů
	 */
	@GetMapping("/movies/list/limit={limit}&{status}")
	public List<ListMovie> getMoviesFromTodayLimited(@PathVariable("limit") int limit,
													 @PathVariable("status") Status status) {
		
		List<ListMovie> movies = movieService.getMoviesFromTodayLimited(LocalDate.now(), limit, status);
		
		return movies;
	}
	
}
