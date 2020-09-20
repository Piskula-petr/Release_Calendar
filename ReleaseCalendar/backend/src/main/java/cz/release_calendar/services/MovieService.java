package cz.release_calendar.services;

import java.time.LocalDate;
import java.util.List;

import cz.release_calendar.entities.Movie;

public interface MovieService {

	/**
	 * Seznam filmů v zadaném měsíci
	 * 
	 * @param startOfMonth - první den v měsíci
	 * @param endOfMonth - poslední den v měsíci
	 * 
	 * @return - vrací List filmů
	 */
	public List<Movie> getMovies(LocalDate startOfMonth, LocalDate endOfMonth);
}
