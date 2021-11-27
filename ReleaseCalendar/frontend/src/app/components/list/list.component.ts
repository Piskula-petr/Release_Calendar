import { ActivatedRoute, Router, UrlTree } from '@angular/router';
import { Title } from '@angular/platform-browser';
import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';

import { Status } from 'src/app/modules/enums/status';
import { MovieListDetailed } from 'src/app/modules/interfaces/moviesList';
import { MoviesService } from 'src/app/services/movies/movies.service';
import { MoviesTotalCount } from 'src/app/modules/interfaces/moviesCount';
import { SearchBehavior } from 'src/app/modules/enums/searchBehavior';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css']
})
export class ListComponent implements OnInit {

  // Limit filmů
  private MOVIE_FETCH_LIMIT: number = 5;

  // Zobrazení / skrytí modalů, pro přidání a odebrání filmů
  public isNewMovieModalClosed: boolean = true;
  public isRemoveMovieModalClosed: boolean = true;

  // Enum - [další / předchozí]
  public status = Status;

  // Enum vyhledávání - [přesměrování / vrácení ID]
  public searchBehavior = SearchBehavior;

  public moviesPrevious: Array<MovieListDetailed> = [];
  public moviesNext: Array<MovieListDetailed> = [];

  // Počet zobrazených filmů
  private moviesPreviousCount: number = 0;
  private moviesNextCount: number = 0;

  // Celkový počet filmů
  public moviesPreviousTotal: number = 0;
  public moviesNextTotal: number = 0;
  

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
    private location: Location
  ) {}


  /**
   * Inicializační metoda
   */
  ngOnInit(): void {

    // Request - získání celkového počtu filmů
    this.moviesService.getMoviesCountByToday().subscribe((moviesCount: MoviesTotalCount) => {
      this.moviesPreviousTotal = moviesCount.previousTotal;
      this.moviesNextTotal = moviesCount.nextTotal;
    });

    // Nastavení titulku
    this.title.setTitle("Release List");

    // Získání parametrů z URL
    this.route.queryParams.subscribe(params => {

      const previous: number = (params.previous ? parseInt(params.previous) : 0);
      const next: number = (params.next ? parseInt(params.next) : this.MOVIE_FETCH_LIMIT);

        if (previous !== 0)
          this.setMovies(0, Status.previous, previous, false);

        this.setMovies(0, Status.next, next, false);

        // Nastavení URL parametrů 
        this.setQueryParametrs(previous, next);
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
    this.moviesNext = [];
    this.moviesPrevious = [];

    this.moviesNextCount = 0;
    this.moviesPreviousCount = 0;

    this.moviesPreviousTotal = 0;
    this.moviesNextTotal = 0;
    
    // Nastavení URL parametrů 
    this.setQueryParametrs(this.moviesPrevious.length, this.moviesNext.length)

    // Inicializační metoda
    this.ngOnInit();
  }


  /**
   * Nastavení filmů
   * 
   * @param startIndex - počáteční index
   * @param status - enum - [další / předchozí]
   * @param limit - limit zobrazených filmů
   * @param setQueryParametrs - nastavit URL parametry
   */
  setMovies(startIndex: number, status: Status, limit: number, setQueryParametrs: boolean): void {

    // Request - získání filmů
    this.moviesService.getMoviesListDetailed(startIndex, status, limit).subscribe((movies: Array<MovieListDetailed>) => {

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

      // Nastavení URL parametrů
      if (setQueryParametrs) 
        this.setQueryParametrs(this.moviesPrevious.length, this.moviesNext.length)
    });
  }


  /**
   * Nastavení URL parametrů
   * 
   * @param previous - velikost starších filmů
   * @param next - velikost nadcházejícíh filmů
   */
  setQueryParametrs(previous: number, next: number): void {

    let urlTree: UrlTree = this.router.parseUrl(this.router.url);

    urlTree.queryParams = {
      previous,
      next
    }; 

    this.location.go(urlTree.toString());
  }

}