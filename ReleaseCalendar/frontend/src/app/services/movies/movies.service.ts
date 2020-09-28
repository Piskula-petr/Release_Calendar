import { Status } from 'src/app/models/status';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { MovieCalendar } from 'src/app/models/movieCalendar';
import { Movie } from 'src/app/models/movie';
import { MovieList } from 'src/app/models/moviesList';

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

    //return this.httpClient.get<Array<MovieCalendar>>("assets/moviesCalendar.json");
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

    //return this.httpClient.get<Array<MovieList>>("assets/movieList.json");
  }


  /**
   * Získání detailu o filmu
   * 
   * @param movieID - ID filmu
   */
  getMovieDetail(movieID: number): Observable<Movie> {
    return this.httpClient.get<Movie>("/api/movie/" + movieID);

    //return this.httpClient.get<Movie>("assets/movieDetail.json");
  }
}
