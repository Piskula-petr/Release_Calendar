import { Component, OnInit, Output, EventEmitter } from '@angular/core';

import { Status } from 'src/app/modules/enums/status';
import { MoviesService } from 'src/app/services/movies/movies.service';
import { MoviePreview } from 'src/app/modules/interfaces/moviePreview';
import { Months } from 'src/app/modules/months';

@Component({
  selector: 'app-remove-movie',
  templateUrl: './remove-movie.component.html',
  styleUrls: ['./remove-movie.component.css']
})
export class RemoveMovieComponent implements OnInit {

  @Output() isRemoveMovieModalClosed = new EventEmitter<boolean>();

  // Enum - [další / předchozí]
  public status = Status;

  // Pole filmů
  public movies: Array<MoviePreview>;

  // Index prvního zobrazeného filmu
  private moviesIndex: number = 0;

  // Počet filmů
  public moviesCount: number;

  // Celkový počet filmů
  public moviesCountTotal: number;

  // Vybraný film na odstranění
  public movieToDelete: MoviePreview | null = null;

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

    this.moviesService.getMoviesForPreview(0).subscribe((movies: Array<MoviePreview>) => {
      this.movies = movies;
      this.moviesCount = movies.length;
    });

    this.moviesService.getMoviesCount().subscribe((moviesCountTotal: number) => {
      this.moviesCountTotal = moviesCountTotal;
    });
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
  selectMovie(movie: MoviePreview): void {

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

      // Předchozí filmy
      case Status.previous:

        // Dekrementace indexu / ošetření nulové hodnoty
        this.moviesIndex = this.moviesIndex - 8;
        if (this.moviesIndex < 0) this.moviesIndex = 0;

        // Dekrementace počtu filmů
        this.moviesCount = this.moviesCount - this.movies.length;

        this.moviesService.getMoviesForPreview(this.moviesIndex).subscribe((movies: Array<MoviePreview>) => {
          this.movies = movies;
        });
        break;

      // Další filmy
      case Status.next:

        // Inkrementace indexu
        this.moviesIndex = this.moviesIndex + this.movies.length;

        this.moviesService.getMoviesForPreview(this.moviesIndex).subscribe((movies: Array<MoviePreview>) => {
          this.movies = movies;
          this.moviesCount = this.moviesCount + movies.length;
        });
        break;
    }
  }


  /**
   * Získání měsíce (String)
   * 
   * @param month - měsíc (0 - 11)
   */
  getMonth(month: number): string {
    return Months[month - 1];
  }


  /**
   * Odebrání filmu
   */
  removeMovie(): void {

    this.moviesService.postDeleteMovieID(this.movieToDelete.id).subscribe((response: Response) => {

      // Response - OK
      if (response.status === 200) {
        this.closeModal();

        this.ngOnInit();
      }
    }, (error) => console.log(error));
  }

}