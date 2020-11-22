package cz.release_calendar.services.impl;

import java.time.LocalDate;
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
import cz.release_calendar.entities.MovieName;
import cz.release_calendar.entities.MoviePreview;
import cz.release_calendar.enums.FileCategory;
import cz.release_calendar.enums.Status;
import cz.release_calendar.services.MovieService;

@Service
public class MovieServiceImpl implements MovieService {

	@Autowired
	private SessionFactory sessionFactory;
	
	
	/**
	 * Seznam filmů v zadaném měsíci
	 * 
	 * @param startOfMonth - první den v měsíci
	 * @param endOfMonth - poslední den v měsíci
	 * 
	 * @return - vrací List filmů pro kalendář
	 */
	@Override
	@Transactional
	public List<MovieCalendar> getMoviesByDate(LocalDate startOfMonth, LocalDate endOfMonth) {
		
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
	 * Počet filmů od dnešního data
	 * 
	 * @param today - dnešní datum
	 * @param staus - enum [další / předchozí]
	 * 
	 * @return - vrací počet filmů
	 */
	@Override
	@Transactional
	public long getMoviesFromTodayCount(LocalDate today, Status status) {
		
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
		
		"SELECT COUNT(*) FROM MovieList "
	  + "WHERE release_date " + condition + " :today");
		
		query.setParameter("today", today);
		
		long moviesCount =  (long) query.uniqueResult();
		
		return moviesCount;
	}
	
	
	/**
	 * Počet filmů
	 * 
	 * @return - vrací počet filmů
	 */
	@Override
	@Transactional
	public long getMoviesCount() {
		
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(
		
		"SELECT COUNT(*) FROM Movie");
		
		long moivesCount = (long) query.uniqueResult();
		
		return moivesCount;
	}
	
	
	/**
	 * Seznam filmů od počátečního indexu
	 * 
	 * @param startIndex - počáteční index
	 * 
	 * @return - vrací List filmů pro náhled
	 */
	@Override
	@Transactional
	public List<MoviePreview> getMoviePreview(int startIndex) {
		
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(
		
		"FROM MoviePreview "
	  + "ORDER BY release_date", MoviePreview.class);
		
		query.setFirstResult(startIndex);
		query.setMaxResults(8);
		
		List<MoviePreview> movies = query.list();
		
		// Náhledové obrázky, podle ID filmu
		for (MoviePreview moviePreview : movies) {
			
			query = session.createQuery(
			
			"FROM File "
		  + "WHERE movie_id = :movieID AND category = :category", File.class);
			
			query.setParameter("movieID", moviePreview.getId());
			query.setParameter("category", FileCategory.poster);
			
			File file = (File) query.uniqueResult();
			moviePreview.setImage(file.getData());
		}
		
		return movies;
	}
	
	
	/**
	 * Seznam filmů od dnešního data
	 * 
	 * @param today - dnešní datum
	 * @param startIndex - počáteční index
	 * @param staus - enum [další / předchozí]
	 * 
	 * @return - vrací List filmů pro seznam
	 */
	@Override
	@Transactional
	public List<MovieList> getMoviesFromToday(LocalDate today, int startIndex, Status status) {
		
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
		
		"FROM MovieList "
	  + "WHERE release_date " + condition + " :today "
	  + "ORDER BY release_date", MovieList.class);
		
		query.setParameter("today", today);
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
	 * Limitovaný seznam filmů od dnešního data
	 * 
	 * @param today - dnešní datum
	 * @param limit - limit výstupů
	 * @param status - enum [další / předchozí]
	 * 
	 * @return - vrací limitovaný List filmů pro seznam
	 */
	@Override
	@Transactional
	public List<MovieList> getMoviesFromTodayLimited(LocalDate today, int limit, Status status) {
		
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
				
		"FROM MovieList "
	  + "WHERE release_date " + condition + " :today "
	  + "ORDER BY release_date", MovieList.class);
		
		query.setParameter("today", today);
		query.setMaxResults(limit);
		
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
	 * Detailní informace o filmu
	 * 
	 * @param movieID - ID filmu
	 * 
	 * @return - vrací detailní informace o filmu
	 */
	@Override
	@Transactional
	public Movie getMovieByID(long movieID) {
		
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
	 * @param images - obrázky
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
	 * 
	 * @return - vrací výsledek operace [0 = neúspěch, > 0 = úspěch]
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
	 * Seznam názvů filmů
	 * 
	 * @return - vrací List názvů filmů
	 */
	@Override
	@Transactional
	public List<MovieName> getMovieNames() {
		
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(
				
		"FROM MovieName "
	  + "ORDER BY id", MovieName.class);
		
		List<MovieName> movieNames = query.list();
		
		return movieNames;
	}
	
}
