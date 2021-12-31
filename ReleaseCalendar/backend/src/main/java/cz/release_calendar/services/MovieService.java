package cz.release_calendar.services;

import java.time.LocalDate;
import java.util.List;

import cz.release_calendar.entities.File;
import cz.release_calendar.entities.Movie;
import cz.release_calendar.entities.MovieCalendar;
import cz.release_calendar.entities.MovieListDetailed;
import cz.release_calendar.entities.MovieName;
import cz.release_calendar.entities.MovieList;
import cz.release_calendar.enums.Status;

public interface MovieService {

	
	/**
	 * Počet filmů
	 */
	public long getMoviesCount();
	
	
	/**
	 * Počet filmů od dnešního dne
	 * 
	 * @param staus - enum [další / předchozí]
	 */
	public long getMoviesCountByToday(Status status);
	
	
	/**
	 * Pole filmů pro kalendář
	 * 
	 * @param startOfMonth - první den v měsíci
	 * @param endOfMonth - poslední den v měsíci
	 */
	public List<MovieCalendar> getMoviesCalendar(LocalDate startOfMonth, LocalDate endOfMonth);
	
	
	/**
	 * Pole filmů
	 * 
	 * @param startIndex - počáteční index
	 */
	public List<MovieList> getMoviesList(int startIndex);
	
	
	/**
	 * Detailní pole filmů
	 * 
	 * @param startIndex - počáteční index
	 * @param limit - limit výstupů
	 * @param status - enum [další / předchozí]
	 */
	public List<MovieListDetailed> getMoviesListDetailed(int startIndex, Status status, int limit);
	
	
	/**
	 * Film podle ID, pro seznam
	 * 
	 * @param movieID - ID filmu
	 */
	public MovieList getMovieList(long movieID);
	
	
	/**
	 * Detail filmu podle ID
	 * 
	 * @param movieID - ID filmu
	 */
	public Movie getMovieDetail(long movieID);
	
	
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
	 */
	public int removeMovie(long movieID);
	
	
	/**
	 * Pole názvů filmů, podle zadané hodnoty
	 * 
	 * @param value - hodnota názvu filmu
	 */
	public List<MovieName> getMovieNames(String value);
	
	
}
