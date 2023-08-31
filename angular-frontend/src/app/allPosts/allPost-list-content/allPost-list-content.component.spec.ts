import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AllPostListContentComponent } from './allPost-list-content.component';

describe('AllPostListContentComponent', () => {
  let component: AllPostListContentComponent;
  let fixture: ComponentFixture<AllPostListContentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AllPostListContentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AllPostListContentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
