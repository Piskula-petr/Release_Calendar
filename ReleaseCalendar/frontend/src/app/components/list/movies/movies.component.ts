import { MovieList } from './../../../models/moviesList';
import { Component, Input, OnInit } from '@angular/core';
import { Months } from 'src/app/models/months';

@Component({
  selector: 'app-movies',
  templateUrl: './movies.component.html',
  styleUrls: ['./movies.component.css']
})
export class MoviesComponent implements OnInit {

  // Vstup - pole filmů
  @Input() movies: Array<MovieList>


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
   * @param month - měsíc (0 - 11)
   */
  getMonth(month: number): string {
    return Months[month - 1];
  }

}
