import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from "@angular/router";

@Component({
  selector: 'app-movie-detail',
  templateUrl: './movie-detail.component.html',
  styleUrls: ['./movie-detail.component.css']
})
export class MovieDetailComponent implements OnInit {

  public movieID: number;


  /**
   * Konstruktor
   * 
   * @param route 
   */
  constructor(private route: ActivatedRoute) {}

  
  /**
   * Inicializační metoda
   */
  ngOnInit(): void {

    this.movieID = parseInt(this.route.snapshot.paramMap.get("movieID"));
  }

}
