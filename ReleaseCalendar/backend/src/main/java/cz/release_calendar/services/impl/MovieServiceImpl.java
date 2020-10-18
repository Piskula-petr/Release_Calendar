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

import cz.release_calendar.entities.CalendarMovie;
import cz.release_calendar.entities.File;
import cz.release_calendar.entities.ListMovie;
import cz.release_calendar.entities.Movie;
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
	public List<CalendarMovie> getMoviesByDate(LocalDate startOfMonth, LocalDate endOfMonth) {
		
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(
		
	    "FROM CalendarMovie "
	  + "WHERE release_date "
	  + "BETWEEN :startOfMonth AND :endOfMonth "
	  + "ORDER BY release_date", CalendarMovie.class);
		
		query.setParameter("startOfMonth", startOfMonth);
		query.setParameter("endOfMonth", endOfMonth);
		
		List<CalendarMovie> movies = query.list();
		
		// Náhledové obrázky, podle ID filmu
		for (CalendarMovie calendarMovie : movies) {
			
			query = session.createQuery(
			
			"FROM File "
		  + "WHERE movie_id = :movieID AND category = :category", File.class);
			
			query.setParameter("movieID", calendarMovie.getId());
			query.setParameter("category", FileCategory.poster);
			
			File file = (File) query.uniqueResult();
			calendarMovie.setImage(file.getData());
		}
		
		return movies;
	}

	
	/**
	 * Počet záznamů od dnešního data
	 * 
	 * @param today - dnešní datum
	 * @param staus - enum [další / předchozí]
	 * 
	 * @return - vrací počet záznamů
	 */
	@Override
	@Transactional
	public Long getMoviesFromTodayCount(LocalDate today, Status status) {
		
		String condition = "";
		
		// Načíst další
		if (status.equals(Status.next)) {
			
			condition = ">=";
			
		// Načíst předchozí
		} else if (status.equals(Status.previous)) {

			condition = "<=";
		}
		
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(
		
		"SELECT Count(*) FROM ListMovie "
	  + "WHERE release_date " + condition + " :today");
		
		query.setParameter("today", today);
		
		Long moviesCount =  (Long) query.uniqueResult();
		
		return moviesCount;
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
	public List<ListMovie> getMoviesFromToday(LocalDate today, int startIndex, Status status) {
		
		String condition = "";
		
		// Načíst další
		if (status.equals(Status.next)) {
			
			condition = ">=";
			
		// Načíst předchozí
		} else if (status.equals(Status.previous)) {

			condition = "<=";
		}
		
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(
		
		"FROM ListMovie "
	  + "WHERE release_date " + condition + " :today "
	  + "ORDER BY release_date", ListMovie.class);
		
		query.setParameter("today", today);
		query.setFirstResult(startIndex);
		query.setMaxResults(8);
		
		List<ListMovie> movies = query.list();
		
		// Náhledové obrázky, podle ID filmu
		for (ListMovie listMovie : movies) {
			
			query = session.createQuery(
			
			"FROM File "
		  + "WHERE movie_id = :movieID AND category = :category", File.class);
			
			query.setParameter("movieID", listMovie.getId());
			query.setParameter("category", FileCategory.poster);
			
			File file = (File) query.uniqueResult();
			listMovie.setImage(file.getData());
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
	public List<ListMovie> getMoviesFromTodayLimited(LocalDate today, int limit, Status status) {
		
		String condition = "";
		
		// Načíst další
		if (status.equals(Status.next)) {
			
			condition = ">=";
			
		// Načíst předchozí
		} else if (status.equals(Status.previous)) {

			condition = "<=";
		}
		
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(
				
		"FROM ListMovie "
	  + "WHERE release_date " + condition + " :today "
	  + "ORDER BY release_date", ListMovie.class);
		
		query.setParameter("today", today);
		query.setMaxResults(limit);
		
		List<ListMovie> movies = query.list();
		
		// Náhledové obrázky, podle ID filmu
		for (ListMovie listMovie : movies) {
			
			query = session.createQuery(
			
			"FROM File "
		  + "WHERE movie_id = :movieID AND category = :category", File.class);
			
			query.setParameter("movieID", listMovie.getId());
			query.setParameter("category", FileCategory.poster);
			
			File file = (File) query.uniqueResult();
			listMovie.setImage(file.getData());
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
	
}
