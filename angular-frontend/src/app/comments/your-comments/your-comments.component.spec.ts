import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { YourCommentsComponent } from './your-comments.component';

describe('YourCommentsComponent', () => {
  let component: YourCommentsComponent;
  let fixture: ComponentFixture<YourCommentsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ YourCommentsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(YourCommentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
