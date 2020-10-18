import { Movie } from './../../models/movie';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { Title } from "@angular/platform-browser";
import { MoviesService } from 'src/app/services/movies/movies.service';
import { Location } from "@angular/common";

@Component({
  selector: 'app-movie-detail',
  templateUrl: './movie-detail.component.html',
  styleUrls: ['./movie-detail.component.css']
})
export class MovieDetailComponent implements OnInit {

  // Celkový počet obrázků
  public imageCount: number;

  public movie: Movie;

  /**
   * Konstruktor
   * 
   * @param route 
   * @param moviesService - service pro získání filmů
   * @param title
   */
  constructor(
    private route: ActivatedRoute, 
    private moviesService: MoviesService,
    private title: Title,
    private location: Location,
  ) {}

  
  /**
   * Inicializační metoda
   */
  ngOnInit(): void {

    // Získání ID filmu z URL
    const movieID: number = parseInt(this.route.snapshot.paramMap.get("movieID"));

    this.moviesService.getMovieDetail(movieID).subscribe((movie: Movie) => {

        this.movie = movie;

        // Nastavení titulku
        this.title.setTitle(this.movie.nameCZ + " | Release Calendar");
      }, 
      (error) => console.log(error)
    );
  }


  /**
   * Přesměrování na předchozí stránku
   */
  previousPage() {
    this.location.back();
  }

}
