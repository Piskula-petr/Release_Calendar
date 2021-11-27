import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { MovieList } from "src/app/modules/interfaces/moviePreview";
import { MoviesTotalCount } from 'src/app/modules/interfaces/moviesCount';
import { Status } from 'src/app/modules/enums/status';
import { MovieCalendar } from 'src/app/modules/interfaces/movieCalendar';
import { Movie } from 'src/app/modules/interfaces/movie';
import { MovieListDetailed } from 'src/app/modules/interfaces/moviesList';
import { NewMovie } from 'src/app/modules/interfaces/newMovie';
import { MovieNames } from "src/app/modules/interfaces/moiveNames";

@Injectable({
  providedIn: 'root'
})
export class MoviesService {

  
  /**
   * Konstruktor
   */
  constructor(private httpClient: HttpClient) {}


  /**
   * Získání počtu filmů pro seznam
   */
  getMoviesCount(): Observable<number> {
    return this.httpClient.get<number>("/api/movies/list/count");
  }


  /**
   * Získání počtu filmů pro seznam od dnešního dne
   */
  getMoviesCountByToday(): Observable<MoviesTotalCount> {
    return this.httpClient.get<MoviesTotalCount>("/api/movies/list/count/today");
  }


  /**
   * Získání pole filmů pro kalendář
   * 
   * @param year - rok [yyyy]
   * @param month - měsíc [0 - 11]
   */
  getMoviesCalendar(year: number, month: number): Observable<Array<MovieCalendar>> {
    return this.httpClient.get<Array<MovieCalendar>>
      (`/api/movies/calendar/year=${year}&month=${(month + 1)}`);
  }


  /**
   * Získání pole filmů pro seznam
   * 
   * @param startIndex - počáteční index
   */
   getMoviesList(startIndex: number): Observable<Array<MovieList>> {
    return this.httpClient.get<Array<MovieList>>(`api/movies/list/index=${startIndex}`);
  }


  /**
   * Získání filmu pro seznam podle ID
   * 
   * @param movieID - ID filmu
   */
    getMovieList(movieID: number): Observable<MovieList> {
    return this.httpClient.get<MovieList>(`/api/movies/list/movieID=${movieID}`);
  }

  
  /**
   * Získání detailního pole filmů pro seznam
   * 
   * @param startIndex - počáteční index
   * @param status - enum [další / předchozí]
   * @param limit - limit výstupů
   */
  getMoviesListDetailed(startIndex: number, status: Status, limit: number): Observable<Array<MovieListDetailed>> {
    return this.httpClient.get<Array<MovieListDetailed>>
      (`/api/movies/list/detailed/index=${startIndex}&${Status[status]}&limit=${limit}`);
  }


  /**
   * Získání detailu o filmu
   * 
   * @param movieID - ID filmu
   */
  getMovieDetail(movieID: number): Observable<Movie> {
    return this.httpClient.get<Movie>(`/api/movie/${movieID}`);
  }
  

  /**
   * Uložení filmu do databáze
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

  
  /**
   * Odstranění filmu z databáze, podle ID
   * 
   * @param movieID - ID filmu
   */
  postDeleteMovieID(movieID: number): Observable<any> {
    return this.httpClient.post("/api/removeMovie", movieID);
  }


  /**
   * Získání pole názvů filmů, podle zadané hodnoty
   * 
   * @param value - vstupní hodnota
   */
  getMovieNames(value: string): Observable<Array<MovieNames>> {
    return this.httpClient.get<Array<MovieNames>>(`/api/movies/search/value=${value}`);
  }

}
