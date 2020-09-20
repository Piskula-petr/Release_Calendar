import { MoviesService } from './services/movies/movies.service';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CalenderComponent } from './components/calender/calender.component';
import { CalendarService } from "./services/calendar/calendar.service";
import { MovieDetailComponent } from './components/movie-detail/movie-detail.component';
import { HttpClientModule } from '@angular/common/http';

@NgModule({
  declarations: [
    AppComponent,
    CalenderComponent,
    MovieDetailComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
  ],
  providers: [
    CalendarService,
    MoviesService]
  ,
  bootstrap: [AppComponent]
})
export class AppModule { }
