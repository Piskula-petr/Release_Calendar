package cz.release_calendar.services.impl;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cz.release_calendar.entities.Movie;
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
	 * @return - vrací List filmů
	 */
	@Override
	@Transactional
	public List<Movie> getMovies(LocalDate startOfMonth, LocalDate endOfMonth) {
		
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(
		
		"FROM movies "
	  + "WHERE release_date "
	  + "BETWEEN :startOfMonth AND :endOfMonth");
		
		query.setParameter("startOfMonth", startOfMonth);
		query.setParameter("endOfMonth", endOfMonth);
		
		List<Movie> movies = query.list();
		
		return movies;
	}
}
