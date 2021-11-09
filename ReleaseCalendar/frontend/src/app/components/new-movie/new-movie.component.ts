import { Component, ElementRef, EventEmitter, OnInit, Output, Renderer2, ViewChild } from '@angular/core';

import { Platforms } from 'src/app/modules/platforms';
import { MoviesService } from 'src/app/services/movies/movies.service';
import { NewMovie } from "src/app/modules/interfaces/newMovie";
import { Genres } from 'src/app/modules/genres';
import { FileStatus } from 'src/app/modules/enums/fileStatus';

@Component({
  selector: 'app-new-movie',
  templateUrl: './new-movie.component.html',
  styleUrls: ['./new-movie.component.css']
})
export class NewMovieComponent implements OnInit {

  @Output() isNewMovieModalClosed = new EventEmitter<boolean>();

  @ViewChild("file") fileRef: ElementRef;
  @ViewChild("files") filesRef: ElementRef;

  // Enum - [náhledový obrázek / obrázky]
  public fileStatus = FileStatus;

  public newMovie: NewMovie;

  // Náhledový obrázek
  public poster: File = null;

  // Pole obrázků
  public images: Array<File> = [];

  // Žánry + platformy
  public genres = Genres;
  public platforms = Platforms;

  // Vybrané žánry
  public genreSelect: string;
  public selectedGenres: Array<string> = [];

  // Vybraní herci
  public actorInput: string;
  public selectedActors: Array<string> = [];

  public posterFormatError: boolean = false;
  public imagesFormatError: boolean = false;

  /**
   * Konstruktor
   * 
   * @param renderer 
   * @param moviesService - service pro získání filmů z databáze
   */
  constructor(private renderer: Renderer2, private moviesService: MoviesService) {}


  /**
   * Inicializační metoda
   */
  ngOnInit(): void {}


  /**
   * Zavření Modalu
   */
  closeModal() {
    this.isNewMovieModalClosed.emit(true);
  }


  /**
   * Zpracování souborů + vytvoření nového
   * 
   * @param file - nahraný soubor
   * @param fileStatus - enum - [náhledový obrázek / obrázky]
   */
  processFiles(file: FileList, fileStatus: FileStatus): void {

    let lastElement: Element;

    const duplicateFile = this.images.find(image => image.name === file[0].name);

    // Ochrana null hondoty, duplicity, počtu souborů
    if (file[0] && !duplicateFile && (!this.poster || this.images.length !== 5)) {

      // Náhledový obrázek
      if (FileStatus[fileStatus] === FileStatus.poster) {

        lastElement = this.fileRef.nativeElement.lastElementChild;

        if (file[0].type !== "image/jpeg") this.posterFormatError = true;

        this.poster = file[0];

      // Obrázky
      } else if (FileStatus[fileStatus] === FileStatus.images) {

        lastElement = this.filesRef.nativeElement.lastElementChild;

        if (file[0].type !== "image/jpeg") this.imagesFormatError = true;

        this.images.push(file[0]);
      }

      // Jméno posledního nahraného souboru
      const lastFileName: string = file[0].name

      const spanElement: Element = lastElement.getElementsByTagName("span")[0];

      // Vytvoření elementu tlačítka pro odebrání souboru
      const button: Element = this.renderer.createElement("button");
      const buttonText: Element = this.renderer.createText("x");
      this.renderer.appendChild(button, buttonText);

      // Click event, pro odebrání souboru
      this.renderer.listen(button, "click", () => this.removeFiles(lastFileName, fileStatus));

      this.renderer.appendChild(lastElement, button);

      // Nastavení jména souboru
      spanElement.innerHTML = lastFileName;

      // Maximálně 5 obrázků
      if (this.images.length < 5 && FileStatus[fileStatus] === FileStatus.images) {

        // Vytvoření nového elementu pro přidání souboru
        this.createFileInputElement(this.filesRef.nativeElement);
      }
    }
  }

  /**
   * Vytvoření nového elementu pro přidání souboru
   */
  createFileInputElement(element: Element): void {

    // Label - obalový element
    const label: Element = this.renderer.createElement("label");
    this.renderer.addClass(label, "fileContainer");
    this.renderer.appendChild(element, label);

    // Span - vnitřní element (název souboru)
    const span: Element = this.renderer.createElement("span");
    const spanText: Element = this.renderer.createText("+ Přidat soubor");
    this.renderer.appendChild(span, spanText);
    this.renderer.appendChild(label, span);

    // Input - vnitřní element (skrytý)
    const inputFile: Element = this.renderer.createElement("input");
    this.renderer.addClass(inputFile, "hide");
    this.renderer.setAttribute(inputFile, "type", "file");
    this.renderer.appendChild(label, inputFile);
  }


  /**
   * Odebrání souboru z listu i z DOM
   * 
   * @param fileName - název souboru
   * @param fileStatus - enum - [náhledový obrázek / obrázky]
   */
  removeFiles(fileName: string, fileStatus: FileStatus) {

    let elementRef: Element;

      // Náhledový obrázek
    if (FileStatus[fileStatus] === FileStatus.poster) {

      this.createFileInputElement(this.fileRef.nativeElement);

      this.posterFormatError = false;

      elementRef = this.fileRef.nativeElement;

      this.poster = null;

    // Obrázky
    } else if (FileStatus[fileStatus] === FileStatus.images) {

      if (this.images.length === 5) {
        this.createFileInputElement(this.filesRef.nativeElement);
      }

      elementRef = this.filesRef.nativeElement;

      for (let index in this.images) {

        // Odebrání souboru z listu, při shodě jména
        if (this.images[index].name === fileName) {
          this.images.splice(parseInt(index), 1);
        }
      }

      // Kontrola formátu
      let isError = this.images.find((image: File) => image.type !== "image/jpeg");

      if (isError) {

        this.imagesFormatError = true;

      } else this.imagesFormatError = false;
     }

    const elements: NodeList = elementRef.querySelectorAll(".fileContainer span");

    elements.forEach((element: Element) => {
      
      // Odebrání elementu z DOM, při shodě jména
      if (element.innerHTML === fileName) {
        this.renderer.removeChild(elementRef, element.parentElement);
      }
    });
  }


  /**
   *  Přidání hodnoty do pole
   * 
   * @param value - hodnota
   * @param array - pole
   */
  addValue(value: string, array: Array<string>): void {

    if (!array.includes(value) && value) {
      array.push(value);
    }
  }


  /**
   * Odebrání hondoty z pole
   * 
   * @param index - index prvku
   * @param array - pole
   */
  removeValue(index: number, array: Array<string>): void {
    array.splice(index, 1);
  }


  /**
   * Odeslání formuláře na server
   */
  onSubmit(): void {

    this.newMovie.genres = this.selectedGenres;
    this.newMovie.actors = this.selectedActors;

    this.moviesService.postNewMovie(this.newMovie, this.poster, this.images).subscribe((response: Response) => {

      // Response - OK
      if (response.status === 200) {
        this.closeModal();
      }
    }, (error) => console.log(error));
  }

}
