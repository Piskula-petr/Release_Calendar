export interface Movie {
    id: number;
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
    image: string;
    images: Array<string>
}