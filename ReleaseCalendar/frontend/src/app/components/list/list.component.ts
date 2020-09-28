import { Status } from 'src/app/models/status';
import { Title } from '@angular/platform-browser';
import { Component, OnInit } from '@angular/core';
import { Months } from 'src/app/models/months';
import { MovieList } from 'src/app/models/moviesList';
import { MoviesService } from 'src/app/services/movies/movies.service';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css']
})
export class ListComponent implements OnInit {
  
  // Startovní index pro načtení předchozích filmů
  public indexForPrevious: number = 0;

  // Startovní index pro načtení dalších filmů
  public indexForNext: number = 0;

  // Enum - [další / předchozí]
  public status = Status;

  // Pole filmů
  public movies: Array<MovieList> = [];
  
  // Měsíce (textově)
  public monthsString = Months;


  /**
   * Konstruktor
   * 
   * @param moviesService - service pro získání filmů z databáze
   * @param title 
   */
  constructor(
    private moviesService: MoviesService, 
    private title: Title
  ) {}


  /**
   * Inicializační metoda
   */
  ngOnInit(): void {

    // Nastavení titulku
    this.title.setTitle("Release List");

    // Nastavení filmů (dalších / předchozích)
    this.setMovies(this.indexForNext, this.status.next);
  }


  /**
   * Nastavení filmů (dalších / předchozích)
   * 
   * @param index - startovní index pro další / předchozí
   * @param status - enum - [další / předchozí]
   */
  setMovies(index: number, status: Status) {

    this.moviesService.getMoviesForList(index, status).subscribe((movies: Array<MovieList>) => {

      // Nastavení dodatečného porametru
      movies = movies.map(movie => ({
        ...movie,
        imageFolder: movie.nameEN.replace(":", "").replace(/ /g, "_").toUpperCase(),
      }));

      // Přidání novýcg filmů na konec pole
      if (Status[status] === Status.next) {

        this.indexForNext = this.indexForNext + movies.length;
        this.movies = this.movies.concat(movies);

      // Přidání předchozích filmů na konec pole
      } else if (Status[status] === Status.previous) {

        this.indexForPrevious = this.indexForPrevious + movies.length
        this.movies = movies.concat(this.movies);
      }
    });
  }


  /**
   * Nastavení dalších filmů (dalších / předchozích)
   * 
   * @param status - enum - [další / předchozí]
   */
  getMoreMovies(status: Status): void {

    // Další filmy
    if (Status[status] === Status.next) {

      this.setMovies(this.indexForNext, status);

    // Předchozí filmy
    } else if (Status[status] === Status.previous) {

      this.setMovies(this.indexForPrevious, status);
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
}
