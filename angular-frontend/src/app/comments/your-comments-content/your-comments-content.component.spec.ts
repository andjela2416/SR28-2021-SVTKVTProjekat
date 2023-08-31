import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { YourCommentsContentComponent } from './your-comments-content.component';

describe('YourCommentsContentComponent', () => {
  let component: YourCommentsContentComponent;
  let fixture: ComponentFixture<YourCommentsContentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [YourCommentsContentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(YourCommentsContentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
