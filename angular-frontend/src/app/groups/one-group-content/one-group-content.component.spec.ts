import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OneGroupContentComponent } from './one-group-content.component';

describe('OneGroupContentComponent', () => {
  let component: OneGroupContentComponent;
  let fixture: ComponentFixture<OneGroupContentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OneGroupContentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OneGroupContentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
