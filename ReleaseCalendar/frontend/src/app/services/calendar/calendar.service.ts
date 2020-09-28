import { Injectable } from '@angular/core';
import { MovieCalendar } from 'src/app/models/movieCalendar';

@Injectable({
  providedIn: 'root'
})
export class CalendarService {

  // Obsah měsíce (dny, filmy[])
  private moviesOfMonth: Array<{day: number, movies: Array<MovieCalendar>}>;

  // Pole filmů
  private movies: Array<MovieCalendar> = [];


  /**
   * Konstruktor
   * 
   */
  constructor() {}


  /**
   * Získání pole dnů ze zadaného měsíce (včetně prázdných počátečních dnů)
   * 
   * @param movies - pole filmů
   * @param year - rok (yyyy)
   * @param month - měsíc (0 - 11)
   */
  getMoviesOfMonth(movies: Array<MovieCalendar>, year: number, month: number): Array<{day: number, movies: Array<MovieCalendar>}> {

    this.moviesOfMonth = [];

    // Počáteční den měsíce (0-6), 0 = Pondělí, 1 = Úterý...
    const monthStartDay: number = new Date(year, month).getDay();
    
    // Počet dnů měsíce
    const monthLenght: number = new Date(year, (month + 1), 0).getDate();
    
    let day: number = 1;

    // Délka pole = délka měsíce + počet prázdných dnů
    for (let i = 0; i < (monthLenght + monthStartDay) - 1; i++) {

      if (i >= (monthStartDay - 1) && day <= monthLenght) {

        // Přidání filmů se shodným dnem
        for (let movie of movies) {

          if (day === movie.releaseDate.getDate()) {
            this.movies.push(movie);
          }
        }

        this.movies = [];
        day++;

      } else day = null;

      // Nastavení parametrů
      this.moviesOfMonth[i] = ({
        day: day, 
        movies: this.movies,
      });
    }

    return this.moviesOfMonth;
  }


  /**
   * Další měsíc
   * 
   * @param year - rok (yyyy)
   * @param month - měsíc (0 - 11)
   */
  nextMonth(year: number, month: number): Date {

    month++;

    if (month > 11) {
      month = 0;
      year = year + 1;
    }

    return new Date(year, month);
  }


  /**
   * Předchozí měsíc
   * 
   * @param year - rok (yyyy)
   * @param month - měsíc (0 - 11)
   */
  previousMonth(year: number, month: number): Date {

    month--;

    if (month < 0) {
      month = 11;
      year = year -1;
    }

    return new Date(year, month);
  }
}