import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserService } from 'src/app/service/user.service';
import { ChangeDetectorRef } from '@angular/core';
import { switchMap } from 'rxjs/operators';

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

  constructor(private route: ActivatedRoute,
  private userService: UserService,
  private cdr: ChangeDetectorRef,
  ) { }

ngOnInit(): void {
  this.route.params.pipe(
    switchMap(params => {
      this.userId = +params['userId'];
      this.cdr.detectChanges();
      console.log(this.userId);

      return this.userService.getOne(this.userId).pipe(
        switchMap(user => {
          this.user = user;
          return this.userService.getMyInfo().pipe(
            switchMap(currentUser => {
              this.isFriend = currentUser.friends.some((friend) => friend.id === user.id);
              this.isHim = currentUser.id === user.id;
              return this.userService.getFriendRequest(this.userId);
            })
          );
        })
      );
    })
  ).subscribe(requests => {
    this.checkIfRequestSent(requests);
    this.cdr.detectChanges();
    console.log(this.user);
    console.log(this.isFriend, this.isHim);
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
      this.cdr.detectChanges();
      console.log(this.user);
      console.log(this.isFriend, this.isHim);
    });
  });
}

}
