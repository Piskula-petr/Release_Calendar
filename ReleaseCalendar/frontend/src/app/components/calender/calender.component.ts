import { MoviesService } from './../../services/movies/movies.service';
import { Component, ElementRef, OnInit, QueryList, Renderer2, ViewChild, ViewChildren } from '@angular/core';
import { Movie } from 'src/app/models/Movie';
import { CalendarService } from "src/app/services/calendar/calendar.service";
import { Status } from "src/app/services/status";

@Component({
  selector: 'app-calender',
  templateUrl: './calender.component.html',
  styleUrls: ['./calender.component.css']
})
export class CalenderComponent implements OnInit {

  @ViewChild("movieElement") movieElementRef: ElementRef;
  @ViewChildren("movieWrapper") movieWrapperRef: QueryList<ElementRef>;

  // Obsah měsíce (dny, filmy)
  public moviesOfMonth: Array<{day: number, movies: Array<Movie>}>;
  
  // Enum - [další / předchozí]
  public status = Status;

  // Informace o měsíci
  public monthString: string;
  private month: number | null = null;
  public year: number | null = null;
  
  /**
   * Konstruktor
   * 
   * @param calendarService - service pro tvorbu kalendáře
   * @param renderer
   */
  constructor(private calendarService: CalendarService, private renderer: Renderer2,
    private moviesService: MoviesService) {}


  /**
   * Inicializační metoda
   */
  ngOnInit(): void {
    
    // Aktuální rok + měsíc
    if (this.year === null && this.month === null) {

      const dateNow: Date = new Date();
      this.year = dateNow.getFullYear();
      this.month = dateNow.getMonth()
    }

    this.setComponentData();
  }

  /**
   * Nastavení potřebných dat pro komponentu
   */
  setComponentData(): void {

    this.monthString = this.calendarService.getMonth(this.month);
    this.moviesService.getMovies(this.year, this.month).subscribe((movies: Array<Movie>) => {

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
   * Změna měsíce
   * 
   * @param status - enum [další / předchozí]
   */
  changeMonth(status: Status): void {

    let date: Date;

    // Nastavení data pro odlišný status
    if (Status[status] === Status.next) {

      date = this.calendarService.nextMonth(this.year, this.month);

    } else if (Status[status] === Status.previous) {

      date = this.calendarService.previousMonth(this.year, this.month);
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
    if (Status[status] === Status.next) {

      cellElement.scrollLeft += elementWidth;

    } else if (Status[status] === Status.previous) {

      cellElement.scrollLeft -= elementWidth;
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
        this.renderer.addClass(nextMovieElement, "hide");
        break;

      case false:
        this.renderer.removeClass(nextMovieElement, "hide");
        break;
    }

    // Zobrazení / skrytí tlačítka pro předchozí film
    switch (cellElement.scrollLeft === scrollLeftMin) {

      case true:
        this.renderer.addClass(previousMovieElement, "hide");
        break;

      case false:
        this.renderer.removeClass(previousMovieElement, "hide");
        break;
    }
  }

}