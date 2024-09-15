import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';
import { UserService } from 'src/app/service/user.service';
import { ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup,FormControl } from '@angular/forms';
import { AuthService } from '../service/auth.service';
import { GroupService } from '../service/group.service';
import { Validators } from '@angular/forms';
import * as bcrypt from 'bcryptjs';
import { HttpClient, HttpEventType } from '@angular/common/http';
import { HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.css'],
})
export class EditProfileComponent implements OnInit {
  user: any; 
reports:any[];
groups:any[];
reports2:any[];
banns:any[];
banns2:any[];
changePass=false;
formP: FormGroup;
retrievedImage: any;
retrievedImage2: any;
base64Data: any;
  retrieveResonse: any;
  message: string;
  imageName: any;
selectedFile: File | null = null;
  constructor(private userService: UserService,private groupService: GroupService,private cdr: ChangeDetectorRef,private router: Router,
  private authService:AuthService,private formBuilder: FormBuilder,private httpClient: HttpClient) {}

  ngOnInit(): void {
    this.userService.getMyInfo().subscribe((data) => {
      this.user = data;
      console.log(data.role); 
      if(this.user.profilePhotoUpload){
		this.getImage2();
}
      this.formP.patchValue({
      username: data.username 
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
    });
        this.userService.getAllBanns2().subscribe((banns2) => {
      this.banns2 = banns2; 
	console.log(banns2);    
    });
     }
       this.groupService.getAllForUser2().subscribe(groupss => {
        this.groups = groupss;
        console.log(this.groups);
    });
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

	cancelChangePassword(){
	this.changePass=false;
}

	onFileSelected(event: any):void {
    this.selectedFile = event.target.files[0];
	  const file = event.target.files[0];
	 const filePath = this.selectedFile.name;
	 console.log(file+" name: "+filePath);
	  const reader = new FileReader();
	  reader.onload = (e: any) => {
		if(this.user.profilePhoto){
			console.log("h");
			this.user.profilePhoto = e.target.result;
		}else{
			this.retrievedImage2 = e.target.result;
		}
	  };
	  reader.readAsDataURL(file); 
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
	
	  unblock2(userId,groupId) {
	this.userService.unblock2(userId,groupId).subscribe((rep)=>{
			       this.userService.getAllBanns2().subscribe((banns2) => {
			      this.banns2 = banns2; 
				console.log(banns2);    
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
      });
    } else {
      alert("Wrong old password");
    }
  });
}
/* getImage() {
	console.log(this.imageName);
    //Make a call to Sprinf Boot to get the Image Bytes.
    this.httpClient.get('http://localhost:8080/api/users/get/' + this.imageName)
      .subscribe(
        res => {
          this.retrieveResonse = res;
          this.base64Data = this.retrieveResonse.picByte;
          this.retrievedImage = 'data:image/jpeg;base64,' + this.base64Data;
        }
      );
  }*/
  getImage2() {
	console.log(this.user.profilePhotoUpload);
    //Make a call to Sprinf Boot to get the Image Bytes.
    this.httpClient.get('http://localhost:8080/api/users/get/' + this.user.profilePhotoUpload.imagePath +"/"+this.user.profilePhotoUpload.id)
      .subscribe(
        res => {
          this.retrieveResonse = res;
          this.base64Data = this.retrieveResonse.picByte;
          this.retrievedImage2 = 'data:image/jpeg;base64,' + this.base64Data;
        }
      );
  }
   onUpload() {
    console.log(this.selectedFile);
       const headers3 = new HttpHeaders({
    'Access-Control-Allow-Methods': 'GET, POST, PUT, PATCH, DELETE',
    'Access-Control-Allow-Origin': 'http://localhost:4200'
  });
    
    //FormData API provides methods and properties to allow us easily prepare form data to be sent with POST HTTP requests.
    const uploadImageData = new FormData();
    uploadImageData.append('file', this.selectedFile, this.selectedFile.name);
  
    //Make a call to the Spring Boot Application to save the image
    this.httpClient.post('http://localhost:8080/api/users/upload', uploadImageData, { headers: headers3,observe: 'response' })
      .subscribe((response) => {
	console.log(response.headers)
        if (response.status === 200) {
	console.log(response.headers)
          this.message = 'Image uploaded successfully';
        } else {
          this.message = 'Image not uploaded successfully';
          console.log(response.headers)
        }
      }
      
      );


  }

  saveChanges() {
	if (this.selectedFile) {
		console.log(this.selectedFile)
      this.userService.editUser(this.user,this.selectedFile).subscribe((data) => {
      this.user=data;
      this.cdr.detectChanges();
	  this.userService.getMyInfo().subscribe((data) => {
      this.user=data;
      if(data.profilePhotoUpload){
		this.getImage2();
		}
      this.cdr.detectChanges();     
      console.log('Changes saved:', data);
    });
      
      console.log('Changes saved:', data);
    });
    }else{this.userService.editUser(this.user).subscribe((data) => {
      this.user=data;
      this.cdr.detectChanges();
      console.log('Changes saved:', data);
    });}
}
}