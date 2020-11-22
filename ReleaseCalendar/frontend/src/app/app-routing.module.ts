import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CalenderComponent } from "src/app/components/calender/calender.component";
import { MovieDetailComponent } from "src/app/components/movie-detail/movie-detail.component";
import { ListComponent } from './components/list/list.component';

const routes: Routes = [
  {path: "", redirectTo: "/kalendar", pathMatch: "full"},
  {path: "kalendar", component: CalenderComponent},
  {path: "seznam", component: ListComponent},
  {path: "detail/:movieID", component: MovieDetailComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
