export class Movie {
    id: number;
    nameCZ: string;
    nameEN: string;
    releaseDate: string; // Date
    imageFolder: string | null;
    platform: string;
    genres: Array<string>;
    csfdLink: string;
    imdbLink: string;
    director: string;
    actors: Array<string>;
    content: string;
    videoLink: string;
}