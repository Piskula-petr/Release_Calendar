import { Component, Input, OnInit } from '@angular/core';

import { MovieListDetailed } from 'src/app/modules/interfaces/moviesList';
import { Months } from 'src/app/modules/months';

@Component({
  selector: 'app-movies',
  templateUrl: './movies.component.html',
  styleUrls: ['./movies.component.css']
})
export class MoviesComponent implements OnInit {

  // Pole filmů
  @Input() movies: Array<MovieListDetailed>


  /**
   * Konstruktor
   */
  constructor() {}


  /**
   * Inicializační metoda
   */
  ngOnInit(): void {}


  /**
   * Získání měsíce (String)
   * 
   * @param month - měsíc [0 - 11]
   */
  getMonth(month: number): string {
    
    return Months[month - 1];
  }

}
