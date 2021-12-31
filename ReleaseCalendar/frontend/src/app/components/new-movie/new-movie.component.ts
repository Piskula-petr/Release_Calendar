import { Component, ElementRef, EventEmitter, OnInit, Output, Renderer2, ViewChild } from '@angular/core';

import { Platforms } from 'src/app/modules/platforms';
import { MoviesService } from 'src/app/services/movies/movies.service';
import { NewMovie } from "src/app/modules/interfaces/newMovie";
import { Genres } from 'src/app/modules/genres';
import { FileType } from 'src/app/modules/enums/fileType';

@Component({
  selector: 'app-new-movie',
  templateUrl: './new-movie.component.html',
  styleUrls: ['./new-movie.component.css']
})
export class NewMovieComponent implements OnInit {

  // Zobrazení / skrytí modalů, pro přidání filmu
  @Output() isNewMovieModalClosed = new EventEmitter<boolean>();

  @ViewChild("file") fileRef: ElementRef;
  @ViewChild("files") filesRef: ElementRef;

  // Enum typ souboru - [náhledový obrázek / obrázky]
  public fileType = FileType;

  public newMovie = <NewMovie>{};

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

  // Chyby formátů souborů
  public posterFormatError: boolean = false;
  public imagesFormatError: boolean = false;


  /**
   * Konstruktor
   * 
   * @param renderer 
   * @param moviesService - service pro získání filmů z databáze
   */
  constructor(
    private renderer: Renderer2, 
    private moviesService: MoviesService
  ) {}


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
   * @param fileType - enum [náhledový obrázek / obrázky]
   */
  processFiles(file: FileList, fileType: FileType): void {

    let containerElement: Element;

    const duplicateFile: File = this.images.find((image: File) => image.name === file[0].name);

    // Ochrana null hondoty, duplicity, počtu souborů
    if (file[0] && !duplicateFile && (!this.poster || this.images.length !== 5)) {

      switch (fileType) {

        // Náhledový obrázek
        case FileType.poster:

          containerElement = this.fileRef.nativeElement.lastElementChild;

          this.poster = file[0]; 

          this.posterFormatError = (file[0].type !== "image/jpeg") ? true : false;
          break;

        // Obrázky
        case FileType.images:

          containerElement = this.filesRef.nativeElement.lastElementChild;

          this.images.push(file[0]); 

          const wrongFileType: File = this.images.find((image: File) => image.type !== "image/jpeg");
          this.imagesFormatError = (wrongFileType) ? true : false;
          break;
      }

      // Vytvoření tlačítka pro odebrání souboru
      this.createRemoveButton(containerElement, file[0].name, fileType);

      // Zakázání dialogu, pro znovu vybrání souboru
      const inputElement: HTMLInputElement = containerElement.getElementsByTagName("input")[0];
      this.renderer.setAttribute(inputElement, "disabled", "true");

      // Odstranení hover efektu
      this.renderer.removeClass(containerElement, "hoverEffect");

      // Nastavení jména souboru
      const spanElement: HTMLSpanElement = containerElement.getElementsByTagName("span")[0]; 
      spanElement.innerHTML = file[0].name;

      // Maximálně 5 obrázků
      if (this.images.length < 5 && FileType[fileType] === FileType.images) {

        // Vytvoření nového elementu pro přidání souboru
        this.createFileInputElement(this.filesRef.nativeElement);
      }
    }
  }


  /**
   * Vytvoření tlačítka pro odebrání souboru
   * 
   * @param containerElement - obalový element
   * @param fileName - název souboru
   * @param fileType - enum [náhledový obrázek / obrázky]
   */
  createRemoveButton(containerElement: Element, fileName: string, fileType: FileType): void {

    // Vytvoření tlačítka
    const button: Element = this.renderer.createElement("button");
    const buttonText: Element = this.renderer.createText("x");
    this.renderer.appendChild(button, buttonText);

    // Click event, pro odebrání souboru
    this.renderer.listen(button, "click", () => this.removeFiles(fileName, fileType));

    this.renderer.appendChild(containerElement, button);
  }


  /**
   * Vytvoření nového elementu pro přidání souboru
   * 
   * @param containerElement - obalový element
   */
  createFileInputElement(containerElement: Element): void {

    // Label - obalový element
    const label: Element = this.renderer.createElement("label");
    this.renderer.addClass(label, "fileContainer");
    this.renderer.addClass(label, "hoverEffect");
    this.renderer.appendChild(containerElement, label);

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
   * Odebrání souboru z listu
   *
   * @param fileName - název souboru
   * @param fileType - enum [náhledový obrázek / obrázky]
   */
  removeFiles(fileName: string, fileType: FileType) {

    let elementRef: Element;

    // Náhledový obrázek
    switch (fileType) {

      case FileType.poster:

        // Vytvoření nového elementu pro přidání souboru
        this.createFileInputElement(this.fileRef.nativeElement);

        this.poster = null;
        this.posterFormatError = false;
        elementRef = this.fileRef.nativeElement;
        break;

      // Obrázky
      case FileType.images:

        // Vytvoření nového elementu pro přidání souboru
        if (this.images.length === 5) this.createFileInputElement(this.filesRef.nativeElement);

        // Odebrání souboru z listu, při shodě jména
        this.images = this.images.filter((image: File) => image.name !== fileName);
  
        // Kontrola formátu
        const wrongFileType: File = this.images.find((image: File) => image.type !== "image/jpeg");
        this.imagesFormatError = (wrongFileType) ? true : false;
        elementRef = this.filesRef.nativeElement;
        break;
    }

    // Odebrání elementu z DOM
    elementRef.querySelectorAll(".fileContainer span").forEach((element: Element) => {
      
      // Odebrání elementu z DOM
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

    this.actorInput = "";
  }


  /**
   * Odebrání hondoty z pole
   * 
   * @param index - index prvku
   * @param array - pole
   */
  removeValue(index: number, array: Array<string>): void {

    array.splice(index, 1);

    if (array.length === 0) this.genreSelect = "";
  }


  /**
   * Odeslání formuláře na server
   */
  onSubmit(): void {

    this.newMovie.genres = this.selectedGenres;
    this.newMovie.actors = this.selectedActors;

    // Request - uložení nového filmu
    this.moviesService.postNewMovie(this.newMovie, this.poster, this.images).subscribe((response: Response) => {
      
      // Response - OK
      if (response.status === 200) {

        // Zavření Modalu
        this.closeModal();
      }
    })
  }

}
