package cz.release_calendar.services.impl;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cz.release_calendar.entities.File;
import cz.release_calendar.entities.Movie;
import cz.release_calendar.entities.MovieCalendar;
import cz.release_calendar.entities.MovieList;
import cz.release_calendar.entities.MovieListDetailed;
import cz.release_calendar.entities.MovieName;
import cz.release_calendar.enums.FileCategory;
import cz.release_calendar.enums.Status;
import cz.release_calendar.services.MovieService;

@Service
public class MovieServiceImpl implements MovieService {

	@Autowired
	private SessionFactory sessionFactory;
	
	
	/**
	 * Počet filmů
	 */
	@Override
	@Transactional
	public long getMoviesCount() {
		
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(
		
		"SELECT COUNT(*) FROM Movie");
		
		long moviesCount = (long) query.uniqueResult();
		
		return moviesCount;
	}
	
	
	/**
	 * Počet filmů od dnešního dne
	 * 
	 * @param staus - enum [další / předchozí]
	 */
	@Override
	@Transactional
	public long getMoviesCountByToday(Status status) {
		
		String condition = "";
		
		// Načíst další
		if (status.equals(Status.next)) {
			
			condition = ">=";
			
		// Načíst předchozí
		} else if (status.equals(Status.previous)) {

			condition = "<";
		}
		
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(
		
		"SELECT COUNT(*) FROM MovieListDetailed "
	  + "WHERE release_date " + condition + " :today");
		
		query.setParameter("today", LocalDate.now());
		
		long moviesCount =  (long) query.uniqueResult();
		
		return moviesCount;
	}
	
	
	/**
	 * Pole filmů pro kalendář
	 * 
	 * @param startOfMonth - první den v měsíci
	 * @param endOfMonth - poslední den v měsíci
	 */
	@Override
	@Transactional
	public List<MovieCalendar> getMoviesCalendar(LocalDate startOfMonth, LocalDate endOfMonth) {
		
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(
		
	    "FROM MovieCalendar "
	  + "WHERE release_date "
	  + "BETWEEN :startOfMonth AND :endOfMonth "
	  + "ORDER BY release_date", MovieCalendar.class);
		
		query.setParameter("startOfMonth", startOfMonth);
		query.setParameter("endOfMonth", endOfMonth);
		
		List<MovieCalendar> movies = query.list();
		
		// Náhledové obrázky, podle ID filmu
		for (MovieCalendar movieCalendar : movies) {
			
			query = session.createQuery(
			
			"FROM File "
		  + "WHERE movie_id = :movieID AND category = :category", File.class);
			
			query.setParameter("movieID", movieCalendar.getId());
			query.setParameter("category", FileCategory.poster);
			
			File file = (File) query.uniqueResult();
			movieCalendar.setImage(file.getData());
		}
		
		return movies;
	}
	
	
	/**
	 * Pole filmů
	 * 
	 * @param startIndex - počáteční index
	 */
	@Override
	@Transactional
	public List<MovieList> getMoviesList(int startIndex) {
		
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(
		
		"FROM MovieList "
	  + "ORDER BY release_date", MovieList.class);
		
		query.setFirstResult(startIndex);
		query.setMaxResults(8);
		
		List<MovieList> movies = query.list();
		
		// Náhledové obrázky, podle ID filmu
		for (MovieList movieList : movies) {
			
			query = session.createQuery(
			
			"FROM File "
		  + "WHERE movie_id = :movieID AND category = :category", File.class);
			
			query.setParameter("movieID", movieList.getId());
			query.setParameter("category", FileCategory.poster);
			
			File file = (File) query.uniqueResult();
			movieList.setImage(file.getData());
		}
		
		return movies;
	}
	
	
	/**
	 * Detailní pole filmů
	 * 
	 * @param startIndex - počáteční index
	 * @param status - enum [další / předchozí]
	 * @param limit - limit výstupů
	 */
	@Override
	@Transactional
	public List<MovieListDetailed> getMoviesListDetailed(int startIndex, Status status, int limit) {
		
		String condition = "";
		String reverseOrder = "";
		
		// Načíst další
		if (status.equals(Status.next)) {
			
			condition = ">=";
			
		// Načíst předchozí
		} else if (status.equals(Status.previous)) {

			condition = "<";
			reverseOrder = " DESC";
		}
		
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(
		
		"FROM MovieListDetailed "
	  + "WHERE release_date " + condition + " :today "
	  + "ORDER BY release_date" + reverseOrder, MovieListDetailed.class);
		
		query.setParameter("today", LocalDate.now());
		query.setFirstResult(startIndex);
		query.setMaxResults(limit);
		
		List<MovieListDetailed> movies = query.list();
		
		// Převrácení listu
		if (status.equals(Status.previous)) {
			
			Collections.reverse(movies);
		}
		
		// Náhledové obrázky, podle ID filmu
		for (MovieListDetailed movieListDetailed : movies) {
			
			query = session.createQuery(
			
			"FROM File "
		  + "WHERE movie_id = :movieID AND category = :category", File.class);
			
			query.setParameter("movieID", movieListDetailed.getId());
			query.setParameter("category", FileCategory.poster);
			
			File file = (File) query.uniqueResult();
			movieListDetailed.setImage(file.getData());
		}
		
		return movies;
	}
	
	
	/**
	 * Film podle ID, pro seznam
	 * 
	 * @param movieID - ID filmu
	 */
	@Override
	@Transactional
	public MovieList getMovieList(long movieID) {
		
		Session session = sessionFactory.getCurrentSession();
		MovieList movieList = session.get(MovieList.class, movieID);
		
		// Náhledový obrázek, podle ID filmu
		Query query = session.createQuery(
				
		"FROM File "
	  + "WHERE movie_id = :movieID AND category = :category", File.class);
		
		query.setParameter("movieID", movieID);
		query.setParameter("category", FileCategory.poster);
		
		File file = (File) query.uniqueResult();
		movieList.setImage(file.getData());
		
		return movieList;
	}

	
	/**
	 * Detail filmu podle ID
	 * 
	 * @param movieID - ID filmu
	 */
	@Override
	@Transactional
	public Movie getMovieDetail(long movieID) {
		
		Session session = sessionFactory.getCurrentSession();
		Movie movie = session.get(Movie.class, movieID);
		
		// Náhledový obrázek, podle ID filmu
		Query query = session.createQuery(
				
		"FROM File "
	  + "WHERE movie_id = :movieID AND category = :category", File.class);
		
		query.setParameter("movieID", movieID);
		query.setParameter("category", FileCategory.poster);
		
		File image = (File) query.uniqueResult();
		movie.setImage(image.getData());
		
		// List obrázku, podle ID filmu
		query = session.createQuery(
		
		"FROM File "
	  + "WHERE movie_id = :movieID AND category = :category", File.class);
		
		query.setParameter("movieID", movieID);
		query.setParameter("category", FileCategory.image);
		
		List<File> files = query.list();
		
		movie.setImages(files.stream().map(file -> file.getData()).collect(Collectors.toList()));
		
		return movie;
	}
	
	
	/**
	 * Uložení nového filmu (informace + obrázky)
	 * 
	 * @param movie - film
	 * @param images - pole obrázky
	 * @param poster - náhledový obrázek
	 */
	@Override
	@Transactional
	public void saveMovie(Movie movie, List<File> images, File poster) {
		
		// Uložení informací o filmu
		Session session = sessionFactory.getCurrentSession();
		Long movieID = (Long) session.save("Movie", movie);
		
		// Uložení náhledového obrázku
		poster.setMovie_id(movieID);
		session.save("File", poster);
		
		// Uložení obrázků
		for (File image : images) {
			image.setMovie_id(movieID);
			session.save("File", image);
		}
	}
	
	
	/**
	 * Smazání filmu
	 * 
	 * @param movieID - ID filmu
	 */
	@Override
	@Transactional
	public int removeMovie(long movieID) {
		
		// Vymazání souborů, podle ID filmu
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(
		
		"DELETE File "
	  + "WHERE movie_id = :movieID");
		
		query.setParameter("movieID", movieID);
		
		int result = query.executeUpdate();
		
		// Vymazání filmu, podle ID
		if (result != 0) {
			
			result = 0;
			query = session.createQuery(
					
			"DELETE Movie "
		  + "WHERE id = :id");
			
			query.setParameter("id", movieID);
			
			result = query.executeUpdate();
			
			return result;
		}
		
		return result;
	}
	
	
	/**
	 * Pole názvů filmů, podle zadané hodnoty
	 * 
	 * @param value - hodnota názvu filmu
	 */
	@Override
	@Transactional
	public List<MovieName> getMovieNames(String value) {
		
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(
				
		"FROM MovieName "
	  + "WHERE LOWER(name_cz) LIKE :value "
	  + "OR LOWER(name_en) LIKE :value", MovieName.class);
		
		query.setParameter("value", "%" + value + "%");
		
		List<MovieName> movieNames = query.list();
		
		return movieNames;
	}
	
	
}
