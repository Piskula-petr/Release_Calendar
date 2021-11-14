import { ActivatedRoute, Router, UrlTree } from '@angular/router';
import { Title } from '@angular/platform-browser';
import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';

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

  // Zobrazení / skrytí modalů, pro přidání a odebrání filmů
  public isNewMovieModalClosed: boolean = true;
  public isRemoveMovieModalClosed: boolean = true;

  // Enum - [další / předchozí]
  public status = Status;

  public moviesPrevious: Array<MovieList> = [];
  public moviesNext: Array<MovieList> = [];

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
    private location: Location
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

      const previous: number = (params.previous ? parseInt(params.previous) : 0);
      const next: number = (params.next ? parseInt(params.next) : 5);

        if (previous !== 0)
          this.setMovies(previous, Status.previous);

        this.setMovies(next, Status.next);

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
    this.moviesNext = null;
    this.moviesPrevious = null;

    this.moviesPreviousTotal = null;
    this.moviesNextTotal = null;
    
    // Nastavení URL parametrů 
    this.setQueryParametrs(this.moviesPrevious.length, this.moviesNext.length)

    this.ngOnInit();
  }


  /**
   * Nastavení filmů
   * 
   * @param limit - limit zobrazených filmů
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
    });
  }


  /**
   * Načtení více filmů (dalších / předchozích)
   * 
   * @param startIndex - počáteční index
   * @param status - enum - [další / předchozí]
   */
  getMoreMovies(startIndex: number, status: Status): void {

    // Nastavení nových filmů
    this.moviesService.getMoviesForList(startIndex, status).subscribe((movies: Array<MovieList>) => {

      switch (Status[status]) {

        case Status.previous:
          this.moviesPrevious = movies.concat(this.moviesPrevious);
          break;

        case Status.next:
          this.moviesNext = this.moviesNext.concat(movies);
          break;
      }

      // Nastavení URL parametrů
      this.setQueryParametrs(this.moviesPrevious.length, this.moviesNext.length);
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