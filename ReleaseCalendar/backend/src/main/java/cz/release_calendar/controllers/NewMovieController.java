package cz.release_calendar.controllers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cz.release_calendar.entities.File;
import cz.release_calendar.entities.Movie;
import cz.release_calendar.enums.FileCategory;
import cz.release_calendar.services.MovieService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api")
public class NewMovieController {

	@Autowired
	private MovieService movieService;

	
	/**
	 * Uložení filmu do databáze
	 * 
	 * @param file - náhledový obrázek
	 * @param movieJSON - informace o filmu ve formátu JSON
	 * @param files - obrázky k filmu
	 */
	@PostMapping("/saveMovie")
	public ResponseEntity<Object> saveMovie(@RequestParam(value = "newMovie") String movieJSON,
						  					@RequestParam(value = "file") MultipartFile file,
				  							@RequestParam(value = "files") List<MultipartFile> files) {
		
		// Response - BAD REQUEST
		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
		int status = 400;
		String message = "failure";
		
		try {
			
			// Film
			Movie movie = new ObjectMapper().readValue(movieJSON, Movie.class);
			
			// Náhledový obrázek
			File poster = new File();
			poster.setData(file.getBytes());
			poster.setCategory(FileCategory.poster);
			
			// Obrázky
			List<File> imagesList = new ArrayList<>();
			
			for (MultipartFile multipartFile : files) {
				
				File image = new File();
				image.setData(multipartFile.getBytes());
				image.setCategory(FileCategory.image);
				imagesList.add(image);
			}

			// Uložení do databáze
			movieService.saveMovie(movie, imagesList, poster);
			
			// Response - OK
			httpStatus = HttpStatus.OK;
			status = 200;
			message = "success";
			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("status", status);
		body.put("message", message);
		
		return new ResponseEntity<>(body, httpStatus);
	}
	
}
