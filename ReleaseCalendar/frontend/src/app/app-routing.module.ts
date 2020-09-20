import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { CalenderComponent } from "src/app/components/calender/calender.component";
import { MovieDetailComponent } from "src/app/components/movie-detail/movie-detail.component";

const routes: Routes = [
  {path: "", redirectTo: "/calendar", pathMatch: "full"},
  {path: "calendar", component: CalenderComponent},
  {path: "movie-detail/:movieID", component: MovieDetailComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
