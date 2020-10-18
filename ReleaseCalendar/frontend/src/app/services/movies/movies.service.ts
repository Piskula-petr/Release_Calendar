import { MoviesTotalCount } from './../../models/moviesCount';
import { Status } from 'src/app/models/status';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { MovieCalendar } from 'src/app/models/movieCalendar';
import { Movie } from 'src/app/models/movie';
import { MovieList } from 'src/app/models/moviesList';
import { NewMovie } from 'src/app/models/newMovie';

@Injectable({
  providedIn: 'root'
})
export class MoviesService {

  /**
   * Konstruktor
   */
  constructor(private httpClient: HttpClient) {}


  /**
   * Získání pole filmů pro kalendář
   * 
   * @param year - rok (yyyy)
   * @param month - měsíc (0 - 11)
   */
  getMoviesForCalendar(year: number, month: number): Observable<Array<MovieCalendar>> {
    return this.httpClient.get<Array<MovieCalendar>>
      ("/api/movies/calendar/year=" + year + "&month=" + (month + 1));
  }


  /**
   * Získání počtu záznamů
   */
  getMoviesForListCount(): Observable<MoviesTotalCount> {
    return this.httpClient.get<MoviesTotalCount>("/api/movies/list/count");
  }

  
  /**
   * Získání pole filmů pro seznam
   * 
   * @param startIndex - počáteční index
   * @param status - enum [další / předchozí]
   */
  getMoviesForList(startIndex: number, status: Status): Observable<Array<MovieList>> {
    return this.httpClient.get<Array<MovieList>>
      ("/api/movies/list/index=" + startIndex + "&" + Status[status]);
  }

  
  /**
   * Získání limitovaného pole filmů pro seznam
   * 
   * @param limit - limit výstupů
   * @param status - enum [další / předchozí]
   */
  getMoviesForListLimited(limit: number, status: Status): Observable<Array<MovieList>> {
    return this.httpClient.get<Array<MovieList>>
      ("/api/movies/list/limit=" + limit + "&" + Status[status]);
  }


  /**
   * Získání detailu o filmu
   * 
   * @param movieID - ID filmu
   */
  getMovieDetail(movieID: number): Observable<Movie> {
    return this.httpClient.get<Movie>("/api/movie/" + movieID);
  }
  

  /**
   * Odeslání dat na server
   * 
   * @param newMovie - informace o filmu
   * @param poster - náhledový obrázek
   * @param images - obrázky
   */
  postNewMovie(newMovie: NewMovie, poster: File, images: Array<File>): Observable<any> {

    const formData: FormData = new FormData();

    // Informace o filmu
    formData.append("newMovie", JSON.stringify(newMovie));

    // Náhledový obrázek
    formData.append("file", poster);

    // Obrázky
    for (const image of images) {
      formData.append("files", image);
    }

    return this.httpClient.post("/api/saveMovie", formData);
  }

}
