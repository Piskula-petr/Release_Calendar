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

    this.moviesService.getMovieDetail(movieID).subscribe(
      (movie: Movie) => {

        // Nastavení dodatečného porametru
        this.movie = movie = {
          ...movie,
          imageFolder: movie.nameEN.replace(":", "").replace(/ /g, "_").toUpperCase(),
        };

        // Nastavení titulku
        this.title.setTitle(this.movie.nameCZ + " | Release Calendar");

        // Získání počtu obrázků ze složky filmu
        this.getImageCount();
      },
      (error) => console.log(error)
    )
  }


  /**
   * Získání počtu obrázků ze složky filmu
   * 
   * @param index - index počátečního obrázku (default = 1)
   */
  getImageCount(index: number = 1): void {

    let image: HTMLImageElement = new Image();

    // Cesta k obrázku
    let imagePath: string = "assets/images/" + this.movie.imageFolder + "/image" + index + ".jpg";
    image.src = imagePath;

    // Rekurze, při načtení obrázku
    image.onload = () => {
      this.imageCount = index;
      this.getImageCount(index + 1);
    }
  }


  /**
   * Vytvoření pole se zadanou délkou
   * 
   * @param length - délka pole
   */
  arrayOf(length: number): Array<number> {
    return Array(length);
  }


  /**
   * Přesměrování na předchozí stránku
   */
  previousPage() {
    this.location.back();
  }
}
