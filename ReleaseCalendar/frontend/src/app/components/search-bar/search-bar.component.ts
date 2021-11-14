import { Component, OnInit } from '@angular/core';

import { MoviesService } from 'src/app/services/movies/movies.service';
import { MovieNames } from 'src/app/modules/interfaces/moiveNames';

@Component({
  selector: 'app-search-bar',
  templateUrl: './search-bar.component.html',
  styleUrls: ['./search-bar.component.css']
})
export class SearchBarComponent implements OnInit {

  // Hledaný název
  public input: string;

  // Všechny názvy filmů
  public movieNames: Array<MovieNames>;

  // Výsledky hledání CZ / EN
  public searchedMovieNamesCZ: Array<MovieNames> = [];
  public searchedMovieNamesEN: Array<MovieNames> = [];


  /**
   * Konstruktor
   * 
   * @param moviesService - service pro získání filmů z databáze
   */
  constructor(private moviesService: MoviesService){}


  /**
   * Inicializační metoda
   */
  ngOnInit(): void {

    // Získání názvů filmů
    this.moviesService.getMovieNames().subscribe((movieNames: Array<MovieNames>) => {
      this.movieNames = movieNames;
    });
  }


  /**
   * Vyhledání názvů, podle vstupu
   */
  search() {
    
    this.searchedMovieNamesCZ = [];
    this.searchedMovieNamesEN = [];

    const input: string = this.input.toLocaleLowerCase();

    if (this.input && !this.input.startsWith(" ")) {

      for (let movieName of this.movieNames) {

        const nameCZ: string = movieName.nameCZ.toLocaleLowerCase();
        const nameEN: string = movieName.nameEN.toLocaleLowerCase();
  
        // Přidání do pole, při schodě CZ jména
        if (nameCZ.includes(input)) {

          this.searchedMovieNamesCZ.push(movieName);
        }
        
        // Přidání do pole, při schodě EN jména
        if (nameEN.includes(input)) {
          
          this.searchedMovieNamesEN.push(movieName)
        }
      }
    }
  }

}
