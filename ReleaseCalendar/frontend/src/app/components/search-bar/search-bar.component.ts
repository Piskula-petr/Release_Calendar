import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Router } from '@angular/router';

import { MoviesService } from 'src/app/services/movies/movies.service';
import { MovieNames } from 'src/app/modules/interfaces/moiveNames';
import { SearchBehavior } from 'src/app/modules/enums/searchBehavior';

@Component({
  selector: 'app-search-bar',
  templateUrl: './search-bar.component.html',
  styleUrls: ['./search-bar.component.css']
})
export class SearchBarComponent implements OnInit {

  // Šířka lišty
  @Input() public width: number;

  // Enum vyhledávání - [přesměrování / vrácení ID]
  @Input() public searchBehavior: SearchBehavior;

  // ID hledaného filmu
  @Output() public movieID = new EventEmitter<number>();

  // Předchozí vstupní hodnota
  public oldInput: string;

  // Aktuální vstupní hodnota
  public input: string;

  // Všechny názvy filmů
  public movieNames: Array<MovieNames>;

  // Výsledky hledání CZ / EN
  public searchedMovieCZ: Array<MovieNames> = [];
  public searchedMovieEN: Array<MovieNames> = [];


  /**
   * Konstruktor
   * 
   * @param moviesService - service pro získání filmů z databáze
   * @param router
   */
  constructor(
    private moviesService: MoviesService,
    private router: Router
    ){}


  /**
   * Inicializační metoda
   */
  ngOnInit(): void {}


  /**
   * Event změny vstupné hodnoty
   * 
   * @param input - aktuální hodnota vstupu
   */
  changeEvent(input: string): void {

    this.searchedMovieCZ = [];
    this.searchedMovieEN = [];

    this.input = input;

    if (!input.startsWith(" ")) {

      if (!this.oldInput) {
  
        // Získání pole názvů filmů
        this.moviesService.getMovieNames(this.input.toLocaleLowerCase()).subscribe((movieNames: Array<MovieNames>) => {
          this.movieNames = movieNames;
          this.search();
        });
  
      } else if (this.input) this.search();
    }
  }


  /**
   * Event kliknutí na hledaný název
   * 
   * @param movieID - ID filmu
   */
  clickEvent(movieID: number): void {

    switch (this.searchBehavior) {

      case SearchBehavior.redirect: 
        this.router.navigate([`/detail/${movieID}`]); break;

      case SearchBehavior.returnID:
        this.movieID.emit(movieID); break;
    }
  }


  /**
   * Vyhledání názvů, podle vstupu
   */
  search(): void {

    for (let movieName of this.movieNames) {

      const nameCZ: string = movieName.nameCZ?.toLocaleLowerCase();
      const nameEN: string = movieName.nameEN?.toLocaleLowerCase();

      // Přidání do pole, při schodě CZ jména
      if (nameCZ?.includes(this.input.toLocaleLowerCase())) {

        this.searchedMovieCZ.push(movieName);
      }
      
      // Přidání do pole, při schodě EN jména
      if (nameEN?.includes(this.input.toLocaleLowerCase())) {
        
        this.searchedMovieEN.push(movieName)
      }
    }
  }  
}
