package cz.release_calendar.services;

import java.time.LocalDate;
import java.util.List;

import cz.release_calendar.entities.File;
import cz.release_calendar.entities.Movie;
import cz.release_calendar.entities.MovieCalendar;
import cz.release_calendar.entities.MovieList;
import cz.release_calendar.entities.MovieName;
import cz.release_calendar.entities.MoviePreview;
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
	public List<MovieCalendar> getMoviesByDate(LocalDate startOfMonth, LocalDate endOfMonth);
	
	
	/**
	 * Počet filmů od dnešního data
	 * 
	 * @param today - dnešní datum
	 * @param staus - enum [další / předchozí]
	 * 
	 * @return - vrací počet filmů
	 */
	public long getMoviesFromTodayCount(LocalDate today, Status status);
	
	
	/**
	 * Počet filmů
	 * 
	 * @return - vrací počet filmů
	 */
	public long getMoviesCount();
	
	
	/**
	 * Seznam filmů od počátečního indexu
	 * 
	 * @param startIndex - počáteční index
	 * 
	 * @return - vrací List filmů pro náhled
	 */
	public List<MoviePreview> getMoviePreview(int startIndex);
	
	
	/**
	 * Seznam filmů od dnešního data
	 * 
	 * @param today - dnešní datum
	 * @param startIndex - počáteční index
	 * @param status - enum [další / předchozí]
	 * 
	 * @return - vrací List filmů pro seznam
	 */
	public List<MovieList> getMoviesFromToday(LocalDate today, int startIndex, Status status);
	
	
	/**
	 * Limitovaný seznam filmů od dnešního data
	 * 
	 * @param today - dnešní datum
	 * @param limit - limit výstupů
	 * @param staus - enum [další / předchozí]
	 * 
	 * @return - vrací limitovaný List filmů pro seznam
	 */
	public List<MovieList> getMoviesFromTodayLimited(LocalDate today, int limit, Status status);
	
	
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
	
	
	/**
	 * Smazání filmu
	 * 
	 * @param movieID - ID filmu
	 * 
	 * @return - vrací výsledek operace [0 = neúspěch, > 0 = úspěch]
	 */
	public int removeMovie(long movieID);
	
	
	/**
	 * Seznam názvů filmů
	 * 
	 * @return - vrací List názvů filmů
	 */
	public List<MovieName> getMovieNames();
	
}
