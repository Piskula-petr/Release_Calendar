package cz.release_calendar.services;

import java.time.LocalDate;
import java.util.List;

import cz.release_calendar.entities.CalendarMovie;
import cz.release_calendar.entities.ListMovie;
import cz.release_calendar.entities.Movie;
import cz.release_calendar.enums.Status;

public interface MovieService {

	/**
	 * Seznam filmů v zadaném měsíci
	 * 
	 * @param startOfMonth - první den v měsíci
	 * @param endOfMonth - poslední den v měsíci
	 * 
	 * @return - vrací List filmů pro kalendář
	 */
	public List<CalendarMovie> getMoviesByDate(LocalDate startOfMonth, LocalDate endOfMonth);
	
	
	/**
	 * Seznam filmů od dnešního data
	 * 
	 * @param today - dnešní datum
	 * @param startIndex - počáteční index
	 * @param staus - enum [další / předchozí]
	 * 
	 * @return - vrací List filmů pro seznam
	 */
	public List<ListMovie> getMoviesFromToday(LocalDate today, int startIndex, Status staus);
	
	
	/**
	 * Detailní informace o filmu
	 * 
	 * @param movieID - ID filmu
	 * 
	 * @return - vrací detailní informace o filmu
	 */
	public Movie getMovieByID(long movieID);
	
}
