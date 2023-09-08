import { Component, Input, OnInit, ChangeDetectionStrategy ,NgZone} from '@angular/core';
import { GroupService } from 'src/app/service/group.service';
import { AuthService } from 'src/app/service/auth.service';
import { UserService } from 'src/app/service/user.service';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-friend-requests',
  templateUrl: './friend-requests.component.html',
  styleUrls: ['./friend-requests.component.css'],
  
})
export class FriendRequestsComponent implements OnInit {
  friendRequests: any[];
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
		this.getFriendRequests();
    });
    
  }
   
  }
	
  formatTimestamp(timestamp: number[]): string {
	    const date = new Date(timestamp[0], timestamp[1] - 1, timestamp[2], timestamp[3], timestamp[4]);
	    const formattedDate = `${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()} ${date.getHours()}:${date.getMinutes()}`;
	    return formattedDate;
  }


  getFriendRequests() {
    this.userService.getFriendRequests().subscribe((requests) => {
	   console.log(requests);
       this.friendRequests = requests.filter((request) => request.toWho.id === this.currentUser);
       this.cdr.detectChanges();
    });
  }

  approveRequest(requestId: number) {
    this.userService.prihvati(requestId).subscribe((response) => {
      console.log(response);
      this.getFriendRequests();
    });
  }

  rejectRequest(requestId: number) {
    this.userService.deleteFriendRequest(requestId).subscribe((response) => {
      console.log(response);
      // Osvežite listu zahteva nakon odbijanja
      this.getFriendRequests();
    });
  }
}
