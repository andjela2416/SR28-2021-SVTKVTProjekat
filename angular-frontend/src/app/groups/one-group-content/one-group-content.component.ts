import { Component, OnInit } from '@angular/core';
import { Route, Router } from '@angular/router';
import { GroupService } from 'src/app/service/group.service';
import { AuthService } from 'src/app/service/auth.service';
import { UserService } from 'src/app/service/user.service';
import { FormBuilder, FormGroup,FormControl } from '@angular/forms';
import { Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-one-group-content',
  templateUrl: './one-group-content.component.html',
  styleUrls: ['./one-group-content.component.css']
})
export class OneGroupContentComponent implements OnInit {
  groupId: number;
  group: any;
  isMember: boolean;
	isAdmin: boolean;
  isRequestSent:boolean;
  currentUser:any;

  constructor(
	private route: ActivatedRoute,
    private groupService:GroupService,
    private router:Router,
    private authService:AuthService,
    private userService:UserService,
  ) { }

  ngOnInit() {
	 if (this.authService.tokenIsPresent()) {
		this.userService.getMyInfo().subscribe(user => {
		this.currentUser=user.id;
    });
      this.route.params.subscribe(params => {
        this.groupId = params['id'];
        this.getGroup();
        this.checkIfRequestSent();
      });
    } 
  }
	

  getGroup(){
    this.groupService.getOneGroup(this.groupId).subscribe((group) => {
    this.group= group;
    console.log(group)
    this.isMember = group.members.some((member) => member.id === this.currentUser);
    this.isAdmin = group.groupAdmin.id === this.currentUser;
    console.log(this.isMember,this.isAdmin)
    })
  }
  
    checkIfRequestSent() {
    this.groupService.getGroupRequests(this.groupId).subscribe((requests) => {
      const user = this.userService.currentUser; 
      console.log(requests);
      if (user) {
        this.isRequestSent = requests.some((request) => request.user_id.id === user.id);
   		 console.log(this.isRequestSent);
      }
    });
   
  }

  
	goBack() {
    window.history.back();
}

}