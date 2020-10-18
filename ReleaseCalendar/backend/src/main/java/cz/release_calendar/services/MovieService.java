package cz.release_calendar.services;

import java.time.LocalDate;
import java.util.List;

import cz.release_calendar.entities.CalendarMovie;
import cz.release_calendar.entities.File;
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
	 * Počet záznamů od dnešního data
	 * 
	 * @param today - dnešní datum
	 * @param status - enum [další / předchozí]
	 * 
	 * @return - vrací počet záznamů
	 */
	public Long getMoviesFromTodayCount(LocalDate today, Status status);
	
	
	/**
	 * Seznam filmů od dnešního data
	 * 
	 * @param today - dnešní datum
	 * @param startIndex - počáteční index
	 * @param status - enum [další / předchozí]
	 * 
	 * @return - vrací List filmů pro seznam
	 */
	public List<ListMovie> getMoviesFromToday(LocalDate today, int startIndex, Status status);
	
	
	/**
	 * Limitovaný seznam filmů od dnešního data
	 * 
	 * @param today - dnešní datum
	 * @param limit - limit výstupů
	 * @param staus - enum [další / předchozí]
	 * 
	 * @return - vrací limitovaný List filmů pro seznam
	 */
	public List<ListMovie> getMoviesFromTodayLimited(LocalDate today, int limit, Status status);
	
	
	/**
	 * Detailní informace o filmu
	 * 
	 * @param movieID - ID filmu
	 * 
	 * @return - vrací detailní informace o filmu
	 */
	public Movie getMovieByID(long movieID);
	
	
	/**
	 * Uložení nového filmu (informace + obrázky)
	 * 
	 * @param movie - film
	 * @param images - obrázky
	 * @param poster - náhledový obrázek
	 */
	public void saveMovie(Movie movie, List<File> images, File poster);
}
