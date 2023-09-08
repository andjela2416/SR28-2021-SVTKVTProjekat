import { Component, OnInit } from '@angular/core';
import { Route, Router } from '@angular/router';
import { GroupService } from 'src/app/service/group.service';
import { UserService } from 'src/app/service/user.service';
import { AuthService } from 'src/app/service/auth.service';
import { FormBuilder, FormGroup,FormControl } from '@angular/forms';
import { Validators } from '@angular/forms'

@Component({
  selector: 'app-groups-list-content',
  templateUrl: './groups-list-content.component.html',
  styleUrls: ['./groups-list-content.component.css']
})
export class GroupsListContentComponent implements OnInit {
  groupList: any[];
  currentUser:any;

  constructor(
    private groupService:GroupService,
    private router:Router,
    private authService:AuthService,
    private userService:UserService,
  ) { }

  ngOnInit() {
	if (this.authService.tokenIsPresent()) {
		this.getGroups()
	    this.userService.getMyInfo().subscribe(user => {
			this.currentUser=user.id;
	    });
	    
  
	 }

	 
  }
	

  getGroups(){
    this.groupService.getAllForUser2().subscribe((groups) => {
    this.groupList= groups    
    })
  }
	goBack() {
    window.history.back();
}

}