import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GroupsListContentComponent } from './groups-list-content.component';

describe('GroupsListContentComponent', () => {
  let component: GroupsListContentComponent;
  let fixture: ComponentFixture<GroupsListContentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GroupsListContentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GroupsListContentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
