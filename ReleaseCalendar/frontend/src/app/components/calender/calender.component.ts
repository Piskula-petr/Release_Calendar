import { ActivatedRoute } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { MovieCalendar } from 'src/app/modules/interfaces/movieCalendar';
import { MoviesService } from 'src/app/services/movies/movies.service';
import { CalendarService } from "src/app/services/calendar/calendar.service";
import { Status } from "src/app/modules/enums/status";
import { Months } from "src/app/modules/months";

@Component({
  selector: 'app-calender',
  templateUrl: './calender.component.html',
  styleUrls: ['./calender.component.css']
})
export class CalenderComponent implements OnInit {

  // Zobrazení / skrytí modalů, pro přidání a odebrání filmů
  public isNewMovieModalClosed: boolean = true;
  public isRemoveMovieModalClosed: boolean = true;

  // Obsah měsíce (den, index zobrazeného filmu, filmy)
  public monthContent: Array<{day: number, index: number, movies: Array<MovieCalendar>}>;

  // Enum - [další / předchozí]
  public status = Status;

  // Datum
  public monthString: string;
  public month: number;
  public year: number;
  
  
  /**
   * Konstruktor
   * 
   * @param calendarService - service pro tvorbu kalendáře
   * @param moviesService - service pro získání filmů z databáze
   * @param router
   * @param route
   *
   */
  constructor(
    private calendarService: CalendarService, 
    private moviesService: MoviesService,
    private router: Router,
    private route: ActivatedRoute,
  ) {}


  /**
   * Inicializační metoda
   */
  ngOnInit(): void {

    // Získání parametrů z URL
    this.route.queryParams.subscribe(params => {

      if (params.month && params.year) {

        this.month = parseInt(params.month) - 1;
        this.year = parseInt(params.year);

        // Nastavení potřebných dat pro komponentu
        this.setComponentData();

      } else {

        const date = new Date();
        this.month = date.getMonth();
        this.year = date.getFullYear();

        // Nastavení URL parametrů
        this.setQueryParametrs(this.month + 1, this.year);
      }
    });
  }


  /**
   * Zavření zobrazeného modalu
   * 
   * @param value - hodnota
   */
  closeModal(value: boolean) {

    // Zavření modalu pro přidání nového filmu
    if (!this.isNewMovieModalClosed) {

      this.isNewMovieModalClosed = value;

    // Zavření modalu pro odebrání filmu
    } else if (!this.isRemoveMovieModalClosed) {
      
      this.isRemoveMovieModalClosed = value;
    }

    this.ngOnInit();
  }


  /**
   * Nastavení potřebných dat pro komponentu
   */
  setComponentData(): void {

    this.monthString = this.getMonth(this.month);
    this.moviesService.getMoviesForCalendar(this.year, this.month).subscribe((movies: Array<MovieCalendar>) => {

      // Přenastavení parametrů
      movies = movies.map(movie => ({
        ...movie,
        releaseDate: new Date(movie.releaseDate),
      }));

      this.monthContent = this.calendarService.getMonthContent(movies, this.year, this.month);
    });
  }


  /**
   * Získání měsíce (String)
   * 
   * @param month - měsíc [0 - 11]
   */
  getMonth(month: number): string {
    
    return Months[month];
  }


  /**
   * Změna měsíce
   * 
   * @param status - enum [další / předchozí]
   */
  changeMonth(status: Status): void {

    let date: Date;

    // Nastavení data pro odlišný status
    switch (Status[status]) {

      case Status.previous:
        date = this.calendarService.previousMonth(this.year, this.month);
        break;

      case Status.next:
        date = this.calendarService.nextMonth(this.year, this.month);
        break;
    }

    this.month = date.getMonth();
    this.year = date.getFullYear();

    // Nastavení URL parametrů
    this.setQueryParametrs(this.month + 1, this.year);
  }


  /**
   * Změna filmu 
   * 
   * @param day - den vydání
   * @param status - enum [další / předchozí]
   */
  changeMovie(day: number, status: Status): void {

    const monthIndex = this.monthContent.findIndex(dayContent => dayContent.day === day);

    switch (Status[status]) {

      case Status.previous:

        this.monthContent[monthIndex].index--;
        break;

      case Status.next: 

        this.monthContent[monthIndex].index++;
        break;
    }
  }

  /**
   * Nastavení URL parametrů
   * 
   * @param month - měsíc [1 - 12]
   * @param year rok [yyyy]
   */
  setQueryParametrs(month: number, year: number): void {

    this.router.navigate([], {

      queryParams: {
        month,
        year
      },
    });
  }

}