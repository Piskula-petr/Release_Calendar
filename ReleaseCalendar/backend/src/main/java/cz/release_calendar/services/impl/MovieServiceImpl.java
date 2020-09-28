package cz.release_calendar.services.impl;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cz.release_calendar.entities.CalendarMovie;
import cz.release_calendar.entities.ListMovie;
import cz.release_calendar.entities.Movie;
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
	public List<ListMovie> getMoviesFromToday(LocalDate today, int startIndex, Status status) {
		
		String queryString = "";
		
		// Načíst další
		if (status.equals(Status.next)) {
			
			queryString = "FROM ListMovie "
						+ "WHERE release_date >= :today "
						+ "ORDER BY release_date";
			
		// Načíst předchozí
		} else if (status.equals(Status.previous)) {
			
			queryString = "FROM ListMovie "
						+ "WHERE release_date <= :today "
						+ "ORDER BY release_date";
		}
		
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(queryString, ListMovie.class);
		
		query.setParameter("today", today);
		query.setFirstResult(startIndex);
		query.setMaxResults(8);
		
		List<ListMovie> movies = query.list();
		
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
		
		return movie;
	}
	
}
