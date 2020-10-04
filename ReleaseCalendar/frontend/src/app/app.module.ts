import { MoviesService } from './services/movies/movies.service';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CalenderComponent } from './components/calender/calender.component';
import { CalendarService } from "./services/calendar/calendar.service";
import { MovieDetailComponent } from './components/movie-detail/movie-detail.component';
import { HttpClientModule } from '@angular/common/http';
import { UrlPipe } from './pipes/url.pipe';
import { ListComponent } from './components/list/list.component';
import { MoviesComponent } from './components/list/movies/movies.component';

@NgModule({
  declarations: [
    AppComponent,
    CalenderComponent,
    MovieDetailComponent,
    UrlPipe,
    ListComponent,
    MoviesComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
  ],
  providers: [
    CalendarService,
    MoviesService,
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
