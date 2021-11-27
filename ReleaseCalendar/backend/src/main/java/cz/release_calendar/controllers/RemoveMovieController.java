package cz.release_calendar.controllers;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cz.release_calendar.services.MovieService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api")
public class RemoveMovieController {

	@Autowired
	private MovieService movieService;
	
	
	/**
	 * Vymazání filmu z databáze
	 * 
	 * @param movieID - id filmu
	 */
	@PostMapping("/removeMovie")
	public ResponseEntity<Object> removeMovie(@RequestBody long movieID) {
		
		// Vymazání filmu
		int result = movieService.removeMovie(movieID);
		
		Map<String, Object> body = new LinkedHashMap<>();
		HttpStatus status;
		
		// Response - OK
		if (result != 0) {
			
			body.put("status", 200);
			body.put("message", "success");
			status = HttpStatus.OK;
			
		// Response - Bad request
		} else {
			
			body.put("status", 404);
			body.put("message", "failure");
			status = HttpStatus.BAD_REQUEST;
		}
		
		body.put("timestamp", LocalDateTime.now());
		
		return new ResponseEntity<>(body, status);
	}
	
}
