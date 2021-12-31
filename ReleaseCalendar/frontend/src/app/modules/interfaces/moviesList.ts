export interface MovieListDetailed {
    id: number;
    nameCZ: string;
    nameEN: string;
    releaseDate: Date;
    platform: string;
    director: string;
    genres: Array<string>;
    actors: Array<string>;
    image: string;
    
}