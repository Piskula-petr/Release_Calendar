import { Component, OnInit, Output, EventEmitter } from '@angular/core';

import { Status } from 'src/app/modules/enums/status';
import { MoviesService } from 'src/app/services/movies/movies.service';
import { MovieList } from 'src/app/modules/interfaces/moviePreview';
import { Months } from 'src/app/modules/months';
import { SearchBehavior } from 'src/app/modules/enums/searchBehavior';

@Component({
  selector: 'app-remove-movie',
  templateUrl: './remove-movie.component.html',
  styleUrls: ['./remove-movie.component.css']
})
export class RemoveMovieComponent implements OnInit {

  // Zobrazení / skrytí modalů, pro odebrání filmů
  @Output() isRemoveMovieModalClosed = new EventEmitter<boolean>();

  // ID hledaného filmu
  public movieID: number;

  // Enum vyhledávání - [přesměrování / vrácení ID]
  public searchBehavior = SearchBehavior;

  // Enum - [další / předchozí]
  public status = Status;

  // Pole filmů
  public movies: Array<MovieList>;

  // Index prvního zobrazeného filmu
  private moviesIndex: number = 0;

  // Počet filmů
  public moviesCount: number;

  // Celkový počet filmů
  public moviesCountTotal: number;

  // Vybraný film na odstranění
  public movieToDelete: MovieList | null = null;

  // Ověření
  public verifyNumber: number;
  public verifyNumberInput: string;

  
  /**
   *  Konstruktor
   * 
   * @param moviesService
   */
  constructor(private moviesService: MoviesService) {}


  /**
   * Inicializační metoda
   */
  ngOnInit(): void {

    // Nastavení filmů
    this.moviesService.getMoviesList(0).subscribe((movies: Array<MovieList>) => {
      this.movies = movies;
      this.moviesCount = movies.length;
    });

    // Nastavení počtu celkových záznamů
    this.moviesService.getMoviesCount().subscribe((moviesCountTotal: number) => {
      this.moviesCountTotal = moviesCountTotal;
    });
  }


  /**
   * Vyhledání filmu
   * 
   * @param movieID - ID filmu
   */
  findMovie(movieID: number): void {

    // Vyhledání ze zobrazených filmů
    let movieToDelete: MovieList = this.movies.find(movie => movie.id === movieID);
  
    if (!movieToDelete) {

      // Získání filmu, podle ID
      this.moviesService.getMovieList(movieID).subscribe((movie: MovieList) => {
        movieToDelete = movie;
        this.selectMovie(movieToDelete);
      });

    } else this.selectMovie(movieToDelete);
  }


  /**
   * Zavření modalu
   */
  closeModal() {
    
    this.isRemoveMovieModalClosed.emit(true);
  }


  /**
   * Nastavení vybraného filmu
   * 
   * @param movie - vybraný film
   */
  selectMovie(movie: MovieList): void {

    this.movieToDelete = movie;

    // Vygenerování 5 místného čísla
    this.verifyNumber = Math.floor(Math.random() * (99999 - 10000 + 1) + 10000);
  }


  /**
   * Zrušení vybraného filmu
   */
  unselectMovie(): void {

    this.movieToDelete = null;
  }


  /**
   * Načtení více filmů (dalších / předchozích)
   * 
   * @param status - enum - [další / předchozí]
   */
  getMoreMovies(status: Status): void {

    switch (Status[status]) {

      case Status.previous:

        // Dekrementace indexu / ošetření nulové hodnoty
        this.moviesIndex = this.moviesIndex - 8;
        if (this.moviesIndex < 0) this.moviesIndex = 0;
        
        // Request - získání filmů
        this.moviesService.getMoviesList(this.moviesIndex).subscribe((movies: Array<MovieList>) => {
          this.movies = movies;
          this.moviesCount = this.moviesCount - movies.length;
        });
        break;

      case Status.next:

        // Inkrementace indexu
        this.moviesIndex = this.moviesIndex + this.movies.length;

        // Request - získání filmů
        this.moviesService.getMoviesList(this.moviesIndex).subscribe((movies: Array<MovieList>) => {
          this.movies = movies;
          this.moviesCount = this.moviesCount + movies.length;
        });
        break;
    }
  }


  /**
   * Získání měsíce (String)
   * 
   * @param month - měsíc [0 - 11]
   */
  getMonth(month: number): string {

    return Months[month - 1];
  }


  /**
   * Odebrání filmu
   */
  removeMovie(): void {

    this.closeModal();

    // Request - odstranění filmu
    //this.moviesService.postDeleteMovieID(this.movieToDelete.id).subscribe((response: Response) => {

    //   // Response - OK
    //   if (response.status === 200) {

          // Zavření modalu
    //     this.closeModal();

          // Inicializační metoda
    //     this.ngOnInit();
    //   }
    // }, (error) => console.log(error));
  }

}