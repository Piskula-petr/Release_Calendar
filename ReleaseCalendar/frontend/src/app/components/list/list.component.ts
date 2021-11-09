import { ActivatedRoute, Router } from '@angular/router';
import { Title } from '@angular/platform-browser';
import { Component, OnInit } from '@angular/core';

import { Status } from 'src/app/modules/enums/status';
import { Months } from 'src/app/modules/months';
import { MovieList } from 'src/app/modules/interfaces/moviesList';
import { MoviesService } from 'src/app/services/movies/movies.service';
import { MoviesTotalCount } from 'src/app/modules/interfaces/moviesCount';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css']
})
export class ListComponent implements OnInit {

  public isNewMovieModalClosed: boolean = true;
  public isRemoveMovieModalClosed: boolean = true;

  // Enum - [další / předchozí]
  public status = Status;

  public moviesPrevious: Array<MovieList> = [];
  public moviesNext: Array<MovieList> = [];

  // Počty zobrazených filmů
  public moviesPreviousCount: number;
  public moviesNextCount: number;

  // Celkový počet filmů
  public moviesPreviousTotal: number;
  public moviesNextTotal: number;
  
  // Měsíce (textově)
  public monthsString = Months;
  

  /**
   * Konstruktor
   * 
   * @param moviesService - service pro získání filmů z databáze
   * @param title 
   * @param router
   * @param route
   */
  constructor(
    private moviesService: MoviesService, 
    private title: Title,
    private router: Router,
    private route: ActivatedRoute,
  ) {}


  /**
   * Inicializační metoda
   */
  ngOnInit(): void {

    // Nastavení počtu celkových záznamů
    this.moviesService.getMoviesFromTodayCount().subscribe((moviesCount: MoviesTotalCount) => {
      
      this.moviesPreviousTotal = moviesCount.previousTotal;
      this.moviesNextTotal = moviesCount.nextTotal;
    });

    // Nastavení titulku
    this.title.setTitle("Release List");

    // Získání parametrů z URL
    this.route.queryParams.subscribe(params => {

      this.moviesPreviousCount = (params.previous ? parseInt(params.previous) : 0);
      this.moviesNextCount = (params.next ? parseInt(params.next) : 8);
    
      this.setMovies(this.moviesPreviousCount, Status.previous);
      this.setMovies(this.moviesNextCount, Status.next);
    });
  }


  /**
   * Zavření zobrazeného Modalu
   * 
   * @param value - hondota
   */
  closeModal(value: boolean) {

    // Zavření modalu pro přidání nového filmu
    if (!this.isNewMovieModalClosed) {

      this.isNewMovieModalClosed = value;

    // Zavření modalu pro odebrání filmu
    } else if (!this.isRemoveMovieModalClosed) {
      
      this.isRemoveMovieModalClosed = value;
    }

    // Vynulování stávajících hodnot
    this.moviesNext = null;
    this.moviesPrevious = null;

    this.moviesPreviousCount = null;
    this.moviesNextCount = null;

    this.moviesPreviousTotal = null;
    this.moviesNextTotal = null;

    // Vynulování URL parametrů
    this.router.navigate([], {queryParams: {
      previous: this.moviesPreviousCount,
      next: this.moviesNextCount,
    }});

    this.ngOnInit();
  }


  /**
   * Nastavení filmů
   * 
   * @param limit - limit
   * @param status - enum - [další / předchozí]
   */
  setMovies(limit: number, status: Status): void {

    this.moviesService.getMoviesForListLimited(limit, status).subscribe((movies: Array<MovieList>) => {

      switch (Status[status]) {

        case Status.previous: 
          this.moviesPrevious = movies; break;

        case Status.next: 
          this.moviesNext = movies; break;
      }

      // Přidání parametrů do URL
      this.router.navigate([], {queryParams: {
        previous: this.moviesPreviousCount,
        next: this.moviesNextCount,
      }});
    });
  }


  /**
   * Načtení více filmů (dalších / předchozích)
   * 
   * @param status - enum - [další / předchozí]
   */
  getMoreMovies(status: Status): void {

    let startIndex: number;

    // Nastavení počátečního indexu
    switch (Status[status]) {

      case Status.previous: 
        startIndex = this.moviesPreviousCount; break;

      case Status.next:
        startIndex = this.moviesNextCount; break;
    }

    this.moviesService.getMoviesForList(startIndex, status).subscribe((movies: Array<MovieList>) => {

      switch (Status[status]) {

        case Status.previous:
          this.moviesPreviousCount = this.moviesPreviousCount + movies.length;
          this.moviesPrevious = movies.concat(this.moviesPrevious);
          break;

        case Status.next:
          this.moviesNextCount = this.moviesNextCount + movies.length;
          this.moviesNext = this.moviesNext.concat(movies);
          break;
      }

      // Přidání upravených parametrů do URL
      this.router.navigate([], {queryParams: {
        previous: this.moviesPreviousCount,
        next: this.moviesNextCount,
      }});
    });
  }

}