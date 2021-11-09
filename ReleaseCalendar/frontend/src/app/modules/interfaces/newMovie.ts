export interface NewMovie {
    nameCZ: string;
    nameEN: string;
    releaseDate: Date;
    platform: string;
    genres: Array<string>;
    csfdLink: string;
    imdbLink: string;
    director: string;
    actors: Array<string>;
    content: string;
    videoLink: string;
}