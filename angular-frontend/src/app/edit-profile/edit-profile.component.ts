import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';
import { UserService } from 'src/app/service/user.service';
import { ChangeDetectorRef } from '@angular/core';
import * as bcrypt from 'bcryptjs';

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.css'],
})
export class EditProfileComponent implements OnInit {
  user: any; 
reports:any[];
reports2:any[];
banns:any[];
changePass=false;
formP: FormGroup;
  constructor(private userService: UserService,private cdr: ChangeDetectorRef,) {}

  ngOnInit(): void {
    this.userService.getMyInfo().subscribe((data) => {
      this.user = data;
      console.log(data.role); 
      this.formP.patchValue({
      username: user.username 
    });
      if(data.role=="ADMIN"){
      this.userService.getAllReports().subscribe((reports) => {
      this.reports = reports; 
	console.log(reports);    
    });
    this.userService.getAllBanns().subscribe((banns) => {
      this.banns = banns; 
	console.log(banns);    
    });
    }
    if(data.role=="GROUPADMIN"){
      this.userService.getAllReports2().subscribe((reports2) => {
      this.reports2 = reports2; 
	console.log(reports2);    
    });}
      this.cdr.detectChanges();
    });
    	 this.formP = this.formBuilder.group({
      
      current: ['', Validators.compose([Validators.required])],
      password: ['', Validators.compose([Validators.required])],
      confirm: ['', Validators.compose([Validators.required])]
    }
    );
  }
  
  changePassword(){
	this.changePass=true;
}
  
  suspend(report?,user?) {
  // Nakon što se korisnik suspenduje, možete ažurirati tabelu ili obavestiti korisnika o uspehu ili neuspehu operacije.
	this.userService.suspend(report).subscribe((rep)=>{
		if(this.user.role=="ADMIN"){
			      this.userService.getAllReports().subscribe((reports) => {
			      this.reports = reports; 
				console.log(reports);  
				 this.cdr.detectChanges();  
			    });
			       this.userService.getAllBanns().subscribe((banns) => {
			      this.banns = banns; 
				console.log(banns);    
				this.cdr.detectChanges();
			    });	    
		}else if(this.user.role=="GROUPADMIN"){
			     this.userService.getAllReports2().subscribe((reports2) => {
			      this.reports2 = reports2; 
				console.log(reports2);   
				 this.cdr.detectChanges(); 
      });
    }
  });
}

  unblock(userId) {
	this.userService.unblock(userId).subscribe((rep)=>{
			       this.userService.getAllBanns().subscribe((banns) => {
			      this.banns = banns; 
				console.log(banns);    
				this.cdr.detectChanges();
			    });	    

	  });
	}

onSubmitPass() {
  this.userService.getMyInfo().subscribe(user => {
    const currentPassword = this.formP.value.current;
    const isPasswordValid = bcrypt.compareSync(currentPassword, user.password);

    if (isPasswordValid) {
      this.authService.changePassword(this.formP.value).subscribe(data => {
        console.log(data);
       this.authService.logout();
		this.router.navigate(['/login']);        
      },
      error => {
        console.log('Sign up error');
        this.notification = { msgType: 'error', msgBody: error['error'].message };
      });
    } else {
      alert("Wrong old password");
    }
  });
}

  saveChanges() {
    this.userService.editUser(this.user).subscribe((data) => {
      this.user=data;
      this.cdr.detectChanges();
      console.log('Changes saved:', data);
    });
  }
}
