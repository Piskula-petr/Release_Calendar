export class Movie {
    id: number;
    nameCZ: string;
    nameEN: string;
    releaseDate: Date;
    imageFolder: string | null = null;
    platform: string;
    genres: Array<string>;
    csfdLink: string;
    imdbLink: string;
    director: string;
    actors: Array<string>;
    content: string;
    videoLink: string;
}