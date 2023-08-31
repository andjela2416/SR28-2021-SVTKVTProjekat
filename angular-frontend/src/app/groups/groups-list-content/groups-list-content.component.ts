import { Component, OnInit } from '@angular/core';
import { Route, Router } from '@angular/router';
import { GroupService } from 'src/app/service/group.service';
import { AuthService } from 'src/app/service/auth.service';
import { FormBuilder, FormGroup,FormControl } from '@angular/forms';
import { Validators } from '@angular/forms'

@Component({
  selector: 'app-groups-list-content',
  templateUrl: './groups-list-content.component.html',
  styleUrls: ['./groups-list-content.component.css']
})
export class GroupsListContentComponent implements OnInit {
  groupList: any[]

  constructor(
    private groupService:GroupService,
    private router:Router,
    private authService:AuthService
  ) { }

  ngOnInit() {
	if (this.authService.tokenIsPresent()) {
		this.getGroups()
	 }

	 
  }
	

  getGroups(){
    this.groupService.getAllFromUser().subscribe((groups) => {
    this.groupList= groups    
    })
  }
	goBack() {
    window.history.back();
}

}