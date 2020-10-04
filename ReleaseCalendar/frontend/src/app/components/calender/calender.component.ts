import { ActivatedRoute } from '@angular/router';
import { MovieCalendar } from './../../models/movieCalendar';
import { Component, ElementRef, OnInit, QueryList, Renderer2, ViewChild, ViewChildren } from '@angular/core';
import { MoviesService } from 'src/app/services/movies/movies.service';
import { CalendarService } from "src/app/services/calendar/calendar.service";
import { Status } from "src/app/models/status";
import { Months } from "../../models/months";
import { Router } from '@angular/router';

@Component({
  selector: 'app-calender',
  templateUrl: './calender.component.html',
  styleUrls: ['./calender.component.css']
})
export class CalenderComponent implements OnInit {

  @ViewChild("movieElement") movieElementRef: ElementRef;
  @ViewChildren("movieWrapper") movieWrapperRef: QueryList<ElementRef>;

  // Obsah měsíce (dny, filmy)
  public moviesOfMonth: Array<{day: number, movies: Array<MovieCalendar>}>;

  // Enum - [další / předchozí]
  public status = Status;

  // Informace o měsíci
  public monthString: string;
  public month: number;
  public year: number;
  
  
  /**
   * Konstruktor
   * 
   * @param calendarService - service pro tvorbu kalendáře
   * @param moviesService - service pro získání filmů z databáze
   * @param renderer
   * @param router
   * @param route
   *
   */
  constructor(
    private calendarService: CalendarService, 
    private moviesService: MoviesService,
    private renderer: Renderer2,
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

        this.month = parseInt(params.month);
        this.year = parseInt(params.year);

      } else {

        const date = new Date();
        this.month = date.getMonth();
        this.year = date.getFullYear();
      }

      // Nastavení potřebných dat pro komponentu
      this.setComponentData();
    });
  }


  /**
   * Nastavení potřebných dat pro komponentu
   */
  setComponentData(): void {

    // Přidání parametrů do URL
    this.router.navigate([], {queryParams: {
      month: this.month,
      year: this.year
    }});

    this.monthString = this.getMonth(this.month);
    this.moviesService.getMoviesForCalendar(this.year, this.month).subscribe((movies: Array<MovieCalendar>) => {

      // Přenastavení parametrů
      movies = movies.map(movie => ({
        ...movie,
        releaseDate: new Date(movie.releaseDate),
        imageFolder: movie.nameEN.replace(":", "").replace(/ /g, "_").toUpperCase(),
      }));

      this.moviesOfMonth = this.calendarService.getMoviesOfMonth(movies, this.year, this.month);
    });
  }


  /**
   * Získání měsíce (String)
   * 
   * @param month - měsíc (0 - 11)
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

    this.year = date.getFullYear();
    this.month = date.getMonth();

    this.setComponentData();
  }


  /**
   * Změna filmu 
   * 
   * @param movieID - ID zobrazeného filmu
   * @param status - enum [další / předchozí]
   */
  changeMovie(movieID: number, status: Status): void {

    // Šířka elementu
    const elementWidth: number = this.movieElementRef.nativeElement.offsetWidth;

    const movieElement: ElementRef = this.movieWrapperRef.find(movie => movie.nativeElement.movieID === movieID);
    const cellElement: Element = movieElement.nativeElement.parentElement.parentElement;

    // Posun na další / předchozí film
    switch (Status[status]) {

      case Status.previous:
        cellElement.scrollLeft -= elementWidth; break;

      case Status.next:
        cellElement.scrollLeft += elementWidth; break;
    }

    // Elementy tlačítek
    const nextMovieElement: Element = cellElement.querySelector(".nextMovie");
    const previousMovieElement: Element = cellElement.querySelector(".previousMovie");

    // Maximální / minimální šířka elementu
    const scrollLeftMin: number = 0;
    const scrollLeftMax: number = movieElement.nativeElement.offsetWidth - elementWidth;

    // Zobrazení / skrytí tlačítka pro další film
    switch (cellElement.scrollLeft === scrollLeftMax) {

      case true:
        this.renderer.addClass(nextMovieElement, "hide"); break;

      case false:
        this.renderer.removeClass(nextMovieElement, "hide"); break;
    }

    // Zobrazení / skrytí tlačítka pro předchozí film
    switch (cellElement.scrollLeft === scrollLeftMin) {

      case true:
        this.renderer.addClass(previousMovieElement, "hide"); break;

      case false:
        this.renderer.removeClass(previousMovieElement, "hide"); break;
    }
  }

}