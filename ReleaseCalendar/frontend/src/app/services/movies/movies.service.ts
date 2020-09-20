import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { Movie } from 'src/app/models/Movie';

@Injectable({
  providedIn: 'root'
})
export class MoviesService {

  /**
   * Konstruktor
   */
  constructor(private httpClient: HttpClient) {}


  /**
   * Získání pole filmů
   * 
   * @param year - rok (yyyy)
   * @param month - měsíc (0 - 11)
   */
  getMovies(year: number, month: number): Observable<Array<Movie>> {
    return this.httpClient
      .get<Array<Movie>>("/api/movies/year=" + year +"&month=" + (month + 1));
  }
}
