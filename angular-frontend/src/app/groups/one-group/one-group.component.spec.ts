import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import {OneGroupComponent } from './oneGroup.component';

describe('OneGroupComponent', () => {
  let component: OneGroupComponent;
  let fixture: ComponentFixture<OneGroupComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [OneGroupComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OneGroupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
