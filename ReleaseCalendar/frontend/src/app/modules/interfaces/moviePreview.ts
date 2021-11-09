export interface MoviePreview {
    id: number;
    nameCZ: string;
    releaseDate: Date;
    platform: string;
    image: string;
    genres: Array<string>;
}