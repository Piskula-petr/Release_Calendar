import { Injectable } from '@angular/core';

import { MovieCalendar } from 'src/app/modules/interfaces/movieCalendar';

@Injectable({
  providedIn: 'root'
})
export class CalendarService {

  // Obsah měsíce (den, index zobrazeného filmu, filmy)
  private monthContent: Array<{day: number, index: number, movies: Array<MovieCalendar>}>;

  // Pole filmů
  private movies: Array<MovieCalendar> = [];


  /**
   * Konstruktor
   */
  constructor() {}


  /**
   * Získání pole dnů ze zadaného měsíce (včetně prázdných počátečních dnů)
   * 
   * @param movies - pole filmů
   * @param year - rok [yyyy]
   * @param month - měsíc [0 - 11]
   */
  getMonthContent(movies: Array<MovieCalendar>, year: number, month: number): Array<{day: number, index: number, movies: Array<MovieCalendar>}> {

    this.monthContent = [];

    // Počáteční den měsíce [0 - 6], 0 = Neděle, 1 = Pondělí...
    let monthStartDay: number = new Date(year, month).getDay();

    // Ošetření začátku měsíce v něděli
    if (monthStartDay === 0) monthStartDay = 7;

    // Počet dnů měsíce
    const monthLenght: number = new Date(year, (month + 1), 0).getDate();

    let day: number = 0;

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
      this.monthContent[i] = ({
        day: day, 
        index: 0,
        movies: this.movies,
      });
    }

    return this.monthContent;
  }


  /**
   * Další měsíc
   * 
   * @param year - rok [yyyy]
   * @param month - měsíc [0 - 11]
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
   * @param year - rok [yyyy]
   * @param month - měsíc [0 - 11]
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