import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserService } from 'src/app/service/user.service';
import { GroupService } from 'src/app/service/group.service';
import {PostService } from 'src/app/service/post.service';
import { ChangeDetectorRef } from '@angular/core';
import { switchMap } from 'rxjs/operators';
import { HttpClient, HttpEventType } from '@angular/common/http'

type NewType = boolean;

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class UserProfileComponent implements OnInit {
  userId: number;
  user:any;
  isRequestSent: boolean = false;
  isFriend: boolean;
  isHim: boolean;
  groups:any[]
  isRequestGot: boolean = false;
  isRequestGotId:number;
  friendRequests:any[];
  postList:any[];
  profile=true;
  retrievedImage2: any;
  base64Data: any;
  retrieveResonse: any;
  message: string;
  imageName: any;
  admin:boolean;
  banned=false;

  constructor(private route: ActivatedRoute,
  private userService: UserService,
  private cdr: ChangeDetectorRef,
  private groupService:GroupService,
  private postService:PostService,
  private httpClient: HttpClient
  ) { }

ngOnInit(): void {
  this.route.params.pipe(
    switchMap(params => {
      this.userId = +params['userId'];
      this.cdr.detectChanges();
      console.log(this.userId);
         this.postService.getAllPostUserId(this.userId).subscribe(postss => {
        this.postList = postss;
        console.log(this.postList);
    });
      this.groupService.getAllForUser22(+params['userId']).subscribe(groupss => {
        this.groups = groupss;
        console.log(this.groups);
    });

      return this.userService.getOne(this.userId).pipe(
        switchMap(user => {
          this.user = user;
          
        if(this.user.profilePhotoUpload){
		this.getImage2();
		}
          
          return this.userService.getMyInfo().pipe(
            switchMap(currentUser => {
				if(currentUser.role=='ADMIN'){
					this.admin=true;
				this.userService.getAllBanns().subscribe((banns) => {
						for(const b of banns){
							if(b.towards==this.user){
								this.banned=true;
							}
						}
				    });	
				}
				
              this.isFriend = currentUser.friends.some((friend) => friend.id === user.id);
              this.isHim = currentUser.id === user.id;
              return this.userService.getFriendRequest(this.user.id);
            })
          );
        })
      );
    })
  ).subscribe(requests => {
	console.log(requests);
    this.checkIfRequestSent(requests);
    this.getFriendRequests();
    this.cdr.detectChanges();
    console.log(this.user);
    console.log(this.isFriend, this.isHim);
  });
}

	getImage2() {
	console.log(this.user.profilePhotoUpload);
    //Make a call to Sprinf Boot to get the Image Bytes.
    this.httpClient.get('http://localhost:8080/api/users/get/' + this.user.profilePhotoUpload.imagePath+"/"+this.user.profilePhotoUpload.id)
      .subscribe(
        res => {
          this.retrieveResonse = res;
          this.base64Data = this.retrieveResonse.picByte;
          this.retrievedImage2 = 'data:image/jpeg;base64,' + this.base64Data;
        }
      );
  }

  getFriendRequests() {
    this.userService.getFriendRequests().subscribe((requests) => {
	   console.log(requests);
       this.friendRequests = requests.filter((request) => request.toWho.id === this.userService.currentUser.id);
       
       
       const requestFound = this.friendRequests.find((request) => request.fromWho.id === this.user.id);
    
	    if (requestFound) {
	      console.log('Request ID:', requestFound.id); 
	      this.isRequestGotId=requestFound.id;
	    }
	    
	    this.isRequestGot = !!requestFound; 
	   
       this.cdr.detectChanges();
    });
  }
  
    approveRequest() {
    this.userService.prihvati(this.isRequestGotId).subscribe((response) => {
      console.log(response);
      this.getFriendRequests();
     this.isFriend=true;
     this.cdr.detectChanges();
    });
  }

  rejectRequest() {
    this.userService.deleteFriendRequest(this.isRequestGotId).subscribe((response) => {
      console.log(response);
      this.getFriendRequests();
      this.isRequestGot=false;
      this.cdr.detectChanges();
    });
  }

checkIfRequestSent(requests: any[]) {
  const user = this.userService.currentUser;
  console.log(requests);
  if (user) {
    this.isRequestSent = requests.some((request) => request.fromWho.id === user.id);
    console.log(this.isRequestSent);

  }
}

reportuj(selectedReason: string) {
  if (!selectedReason) {
    alert("Morate selektovati razlog pre nego sto posaljete prijavu.");
    return; 
  }

if(this.user.role!='ADMIN'){
	  const novaReakcija = {
    id: this.user,
    reason: selectedReason,
  };

  this.userService.report(novaReakcija).subscribe(res => {
	alert("Uspesno ste prijavili korisnika.");
  });	
}

}


  formatTimestamp(timestamp: number[]): string {
    const date = new Date(timestamp[0], timestamp[1] - 1, timestamp[2], timestamp[3], timestamp[4]);
    const formattedDate = `${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()} ${date.getHours()}:${date.getMinutes()}`;
    return formattedDate;
}

posaljiZahtevZaPrijateljstvo() {
  if(this.isRequestSent==false){	
  this.userService.posZahtev(this.user.id)
    .subscribe(res => {
	this.isRequestSent = true;
	this.cdr.detectChanges();
    });
}
}
  suspend(report?,user?) {
	if(this.banned==false){
			this.userService.suspendUser(user).subscribe((rep)=>{
			this.banned=true;
			this.cdr.detectChanges();
  });
	}
}
ukloniIzPrijatelja() {
  this.userService.izbaciIzPrijatelja(this.user.id).subscribe(() => {
    this.userService.getOne(this.userId).pipe(
      switchMap(user => {
        this.user = user;
        return this.userService.getMyInfo().pipe(
          switchMap(currentUser => {
            this.isFriend = currentUser.friends.some((friend) => friend.id === user.id);
            this.isHim = currentUser.id === user.id;
            return this.userService.getFriendRequest(this.userId);
          })
        )
      })
    ).subscribe(requests => {
      this.checkIfRequestSent(requests);
      //this.isRequestSent=false;
      this.cdr.detectChanges();
      console.log(this.user);
      console.log(this.isFriend, this.isHim);
    });
  });
}

}
