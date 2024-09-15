import { Component, Input, OnInit, ChangeDetectionStrategy ,NgZone} from '@angular/core';
import { GroupService } from 'src/app/service/group.service';
import { AuthService } from 'src/app/service/auth.service';
import { UserService } from 'src/app/service/user.service';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-group-requests',
  templateUrl: './group-requests.component.html',
  styleUrls: ['./group-requests.component.css'],
	
})
export class GroupRequestsComponent implements OnInit {
  groupRequests: any[];
  currentUser:any;
  constructor(private groupService: GroupService,
  private authService:AuthService,
  private userService:UserService,
  private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit() {
		
	if (this.authService.tokenIsPresent()) {
    this.userService.getMyInfo().subscribe(user => {
		this.currentUser=user.id;
		this.getGroupRequests();
    });
    
  }
  }
	
  formatTimestamp(timestamp: number[]): string {
	    const date = new Date(timestamp[0], timestamp[1] - 1, timestamp[2], timestamp[3], timestamp[4]);
	    const formattedDate = `${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()} ${date.getHours()}:${date.getMinutes()}`;
	    return formattedDate;
  }


  getGroupRequests() {
    this.groupService.getGroupsRequests().subscribe((requests) => {
	   console.log(requests);
       this.groupRequests = requests.filter((request) => (request.group.groupAdmin && request.group.groupAdmin.id === this.currentUser) || request.group.addedGroupAdmins.some(admin => admin.id === this.currentUser));
       this.cdr.detectChanges();
    });
  }

  approveRequest(requestId: number) {
    this.groupService.prihvati(requestId).subscribe((response) => {
      console.log(response);
      // Osvežite listu zahteva nakon odobravanja
      this.getGroupRequests();
    });
  }

  rejectRequest(requestId: number) {
    this.groupService.deleteGroupRequest(requestId).subscribe((response) => {
      console.log(response);
      // Osvežite listu zahteva nakon odbijanja
      this.getGroupRequests();
    });
  }
}
