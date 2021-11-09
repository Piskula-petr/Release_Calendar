import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from 'src/app/app-routing.module';
import { AppComponent } from 'src/app/app.component';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

import { UrlPipe } from 'src/app/pipes/url.pipe';
import { MoviesService } from 'src/app/services/movies/movies.service';
import { CalendarService } from "src/app/services/calendar/calendar.service";
import { CalenderComponent } from 'src/app/components/calender/calender.component';
import { MovieDetailComponent } from 'src/app/components/movie-detail/movie-detail.component';
import { ListComponent } from 'src/app/components/list/list.component';
import { MoviesComponent } from 'src/app/components/list/movies/movies.component';
import { NewMovieComponent } from 'src/app/components/new-movie/new-movie.component';
import { RemoveMovieComponent } from 'src/app/components/remove-movie/remove-movie.component';
import { SearchBarComponent } from 'src/app/components/search-bar/search-bar.component';

@NgModule({
  declarations: [
    AppComponent,
    CalenderComponent,
    MovieDetailComponent,
    UrlPipe,
    ListComponent,
    MoviesComponent,
    NewMovieComponent,
    RemoveMovieComponent,
    SearchBarComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [
    CalendarService,
    MoviesService,
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
