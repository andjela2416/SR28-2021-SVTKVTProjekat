import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AllPostListComponent } from './allPost-list.component';

describe('AllPostListComponent', () => {
  let component: AllPostListComponent;
  let fixture: ComponentFixture<AllPostListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AllPostListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AllPostListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
