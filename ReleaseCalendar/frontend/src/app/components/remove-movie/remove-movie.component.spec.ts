import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RemoveMovieComponent } from './remove-movie.component';

describe('RemoveMovieComponent', () => {
  let component: RemoveMovieComponent;
  let fixture: ComponentFixture<RemoveMovieComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RemoveMovieComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RemoveMovieComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
