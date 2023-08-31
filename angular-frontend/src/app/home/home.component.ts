import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import {UserService} from '../service/user.service';
import {PostService} from '../service/post.service';
import {GroupService} from '../service/group.service';
import {ConfigService} from '../service/config.service';
import { FormBuilder, FormGroup,FormControl } from '@angular/forms';
import { AuthService } from '../service/auth.service';
import { ApiService } from '../service/api.service';
import { ComService } from '../service/com.service';
import { Validators } from '@angular/forms'
import { CardComponent} from '../card/card.component';
import { ActivatedRoute } from '@angular/router';
import { takeUntil } from 'rxjs/operators';
import { Subject } from 'rxjs/Subject';
import { tap } from 'rxjs/operators';
import * as bcrypt from 'bcryptjs';


interface DisplayMessage {
  msgType: string;
  msgBody: string;
}
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
comments: any[]
  clubResponse = {};
  GroupPostsResponse={};
  YourGroupPostsResponse={};
  admin=false;
  open=false;
  showCard=true;
  whoamIResponse = {};
  allUserResponse = {};
  allPostResponse = {};
  allPostRndmResponse = {};
  allGroupResponse = {};
  OneGroupNResponse={};
  OneGroupUResponse={};
    allGroupForUserResponse = {};
    allGroupForUser2Response = {};
  UserCommsResponse={};
    UserGroupsResponse={};
  CommentsResponse = {};
  allPostFromUserResponse= {};
  OnePostResponse= {};
    OneGroupResponse= {};
    OnePostResponse2= {};
   OneComResponse= {};
   ReportsResponse={};
   GroupRequestsResponse={};
      OneComResponse2= {};
      OneGroupResponse2= {};
  selectedPostId: number;
  selectedRequestId:number;
  selectedGroupId: number; 
    selectedSort: number; 
    selectedPostId2: number; 
  selectedComId: number; 
   selectedUserId: number; 
  submitted = false;
  ReportResponse={};
    formP: FormGroup;
   createGroup = false;
  editing=false;
  commenting=false;
  replying=false;
  reply=false;
  edit=false;
  editCom=false;
  editGroup=false;
  comment=false;
  suspendGroup=false;
  selectedComIdEdit:number;
  selectedGroupIdEdit:number;
   selectedGroupId3:number;
    form: FormGroup
    returnUrl: string;
    form3:FormGroup;
    form4:FormGroup;
    form5:FormGroup;
    form7 = FormGroup;
   // form8 = FormGroup;
    sort=false;
     sort2=false;
     submitted2=false;
    notification: DisplayMessage;
    private ngUnsubscribe: Subject<void> = new Subject<void>();
  constructor(
    private config: ConfigService,
    private userService: UserService,
    private postService : PostService,
    private cardComp:CardComponent,
    private router: Router,
    private groupService : GroupService,
    private authService: AuthService,
    private apiService: ApiService,
    private comService:ComService,
     private route: ActivatedRoute,
         private formBuilder: FormBuilder,
  ) {
  }
forma = new FormGroup({
    post: new FormControl('', Validators.required),
    pathSlike: new FormControl('')
  });
  formg = new FormGroup({
    content: new FormControl('', Validators.required),
    pathSlike: new FormControl('')  
  });
form6 = new FormGroup({
    name: new FormControl('', Validators.required),
    description: new FormControl('')
  });
  formaa = new FormGroup({
    suspendedReason: new FormControl('', Validators.required)
  });
    form8 = new FormGroup({
	id:new FormControl(this.selectedGroupIdEdit),
	name:new FormControl(''),
    description: new FormControl('')
  });
  


ngOnInit() {
	 this.formP = this.formBuilder.group({
      
      current: ['', Validators.compose([Validators.required])],
      password: ['', Validators.compose([Validators.required])],
      confirm: ['', Validators.compose([Validators.required])]
    }
    );

  this.form5 = this.formBuilder.group({
    id: this.selectedComIdEdit,
    text: ['', Validators.compose([Validators.required])]
  });
  
 // this.userService.userIsPresent();
  console.log("Token is present "+this.authService.tokenIsPresent());

  if (this.authService.tokenIsPresent()) {
    this.userService.getMyInfo().subscribe(user => {
      if (user.role == "ADMIN") {
        this.admin = true;
        this.formP.patchValue({
      username: user.username // Postavljanje vrednosti korisničkog imena u formu
    });
      }
    });
  }
}


onSubmit9() {
	console.log("Token is present "+this.authService.tokenIsPresent());
	if(this.authService.tokenIsPresent()){
  this.userService.getMyInfo().subscribe(user => {
    const currentPassword = this.formP.value.current;
    const isPasswordValid = bcrypt.compareSync(currentPassword, user.password);

    if (isPasswordValid) {
      this.notification = undefined;
      this.submitted2 = true;
      this.authService.changePassword(this.formP.value).subscribe(data => {
        console.log(data);
       this.authService.logout();
this.router.navigate(['/login']);
        
      },
      error => {
        this.submitted = false;
        console.log('Sign up error');
        this.notification = { msgType: 'error', msgBody: error['error'].message };
      });
    } else {
      alert("Netacna stara lozinka");
    }
  });}
  else if (!this.authService.tokenIsPresent()){
	
	alert("Morate se prvo ulogovati")}
}


  showPostList() {
	
    this.router.navigate(['/post-list']);
  }
    praviGrupu() {
	if (this.authService.tokenIsPresent()) {
    this.createGroup=true;}else{alert("Morate se prvo ulogovati")}
  }
      suspendGrupu() {
	
	if (this.authService.tokenIsPresent()) {
		if (!this.selectedGroupId) {
  			   alert('Please enter a valid Group ID.');
 			   return;
 			 }else{	this.groupService.getAll().subscribe(posts => {
  		const isIdFound = posts.some(post => post.id === this.selectedGroupId);
  		if (isIdFound) {
  	 this.suspendGroup=true;
  	 
  		}else {
    		alert('Pogresan id');
  				}	
			}, error => {
  			console.error(error);
			});}
 	
    }else{alert("Morate se prvo ulogovati")}
  }

  onSubmit() {
	if (this.authService.tokenIsPresent()) {
		 if (!this.forma.value.post){
			 alert('Tekst je obavezno polje objave');
		}else{console.log(this.forma.value.pathSlike);
    this.submitted = true;
    console.warn('Your order has been submitted', this.forma.value);
    this.postService.create(this.forma.value)
	this.forma.reset();}}
	 else {
    alert('Morate se prvo ulogovati');
    // Dodajte logiku koju želite da primenite ako korisnik nije ulogovan
  }
  
  
  }
  onSubmitg() {
	if (this.authService.tokenIsPresent()) {
		if(!this.selectedGroupId3){alert("Mora id")}
		 if (!this.formg.value.post){
			 alert('Tekst je obavezno polje objave');
		}else{console.log(this.formg.value.pathSlike);
   this.groupService.getOneGroup(this.selectedGroupId3).subscribe(
        group => {
          // Kreiraj objekat za slanje
          const body = {
            content: this.formg.value.post,
            pathSlike: this.formg.value.pathSlike,
            group: group
          };
		console.log(body);
          // Pošalji objavu
          this.postService.create4(this.formg.value,group)
        },
        err => {
          console.log('Greška prilikom učitavanja grupe:', err);
          // Ovdje možete dodati odgovarajuću logiku u slučaju greške prilikom učitavanja grupe
        }
      );
	this.formg.reset();}}
	 else {
    alert('Morate se prvo ulogovati');
    // Dodajte logiku koju želite da primenite ako korisnik nije ulogovan
  }
  
  
  }
    prihvati() {
	if (this.authService.tokenIsPresent()) {
		 if (!this.selectedRequestId){
			 alert('Unesite id');
		}else{


    this.groupService.prihvati(this.selectedRequestId) 
    .subscribe(res => {
          this.forgeResonseObj(this.GroupRequestsResponse, res, '/api/groups/approve');
        }, err => {
          this.forgeResonseObj(this.GroupRequestsResponse, err, '/api/groups/approve');
        });
	this.forma.reset();}}
	 else {
    alert('Morate se prvo ulogovati');
    // Dodajte logiku koju želite da primenite ako korisnik nije ulogovan
  }
  
  }
      blokiraj() {
	if (this.authService.tokenIsPresent()) {
		 if (!this.selectedUserId){
			 alert('Unesite id');
		}else{


    this.userService.blokiraj(this.selectedUserId) 
    .subscribe(res => {
          this.forgeResonseObj(this.allUserResponse, res, '/api/groups/approve');
        }, err => {
          this.forgeResonseObj(this.allUserResponse, err, '/api/groups/approve');
        });
	this.forma.reset();}}
	 else {
    alert('Morate se prvo ulogovati');
    // Dodajte logiku koju želite da primenite ako korisnik nije ulogovan
  }
  
  }
    napraviGrupu() {
	if (this.authService.tokenIsPresent()) {
		 if (!this.form6.value.name){
			 alert('Ime je obavezno polje objave');
		}else{console.log(this.form6.value);
   // this.submitted = true;
   // console.warn('Your order has been submitted', this.forma.value);
    this.groupService.create(this.form6.value)
	this.form6.reset();}}
	 else {
    alert('Morate se prvo ulogovati');
    // Dodajte logiku koju želite da primenite ako korisnik nije ulogovan
  }
  }
      suspendujGrupu() {
	if (this.authService.tokenIsPresent()) {
		 if (!this.formaa.value.suspendedReason){
			 alert('Razlog je obavezno polje');
		}else{console.log(this.formaa.value);
   // this.submitted = true;
   // console.warn('Your order has been submitted', this.forma.value);
    this.groupService.suspend(this.formaa.value,this.selectedGroupId)
 .subscribe((groups) => {this.groupService.getAll()}
 
 );
	this.formaa.reset();}}
	 else {
    alert('Morate se prvo ulogovati');
    // Dodajte logiku koju želite da primenite ako korisnik nije ulogovan
  }
  }
  

  onSubmit2() {
	if (this.authService.tokenIsPresent()) {
		 if (!this.form.value.content){
			 alert('Tekst je obavezno polje objave');
		}else{
    this.submitted = true;
    console.warn('Your order has been submitted', this.form.value);
    console.log(this.form.value+"dsehduhe");
     this.postService.edit(this.form.value)
        .subscribe(res => {
		console.log(res);
          this.forgeResonseObj(this.OnePostResponse, res, "api/posts/edit");
        }, err => {
          this.forgeResonseObj(this.OnePostResponse, err, "api/posts/edit");
        });

	this.form.patchValue({
  content: '',
  images: ''
});;}
	this.editing=false;
}
	 else {
    alert('Morate se prvo ulogovati');
    // Dodajte logiku koju želite da primenite ako korisnik nije ulogovan
  }
  }
    kreirajReakciju(tipReakcije: string, postId: number, commentId: number) {
	if (!this.selectedPostId2){
			 alert('Unesi id posta ');
		}
  const novaReakcija = {
    type: tipReakcije,
    post: postId,
    comment: commentId
  };
 this.postService.react(novaReakcija)
    .subscribe(res => {
      this.postService.getOnePost(this.selectedPostId2).subscribe(updatedPost => {
        this.forgeResonseObj(this.OnePostResponse2, updatedPost, '/api/reactions/create');
        console.log(updatedPost);
      });
    }, err => {
      this.forgeResonseObj(this.OnePostResponse2, err, '/api/reactions/create');
    });
 /*   this.postService.react(novaReakcija)//'http://localhost:8080/api/reactions/create', novaReakcija)
      .subscribe(res => {
          this.forgeResonseObj(this.OnePostResponse2, res, '/api/reactions/create');
        }, err => {
          this.forgeResonseObj(this.OnePostResponse2, err, '/api/reactions/create');
        });
*/
      
  }

	reportuj2(selectedPostId2: any, selectedReason: string) {
  if (!this.selectedPostId2) {
    alert('Unesi id posta');
    return;
  }

else{  this.postService.getAll().subscribe(posts => {
    const selectedPost = posts.find(post => post.id === selectedPostId2);
    if (selectedPost) {
      const novaReakcija = {
        id: selectedPost,
        reason: selectedReason,
      };

      this.userService.report2(novaReakcija).subscribe(res => {
        this.postService.getOnePost(this.selectedPostId2).subscribe(updatedPost => {
          this.forgeResonseObj(this.OnePostResponse2, updatedPost, '/api/reactions/create');
          console.log(updatedPost);
        });
      }, err => {
        this.forgeResonseObj(this.OnePostResponse2, err, '/api/reactions/create');
      });
    }
  });}
}

    posaljiZahtevZaGrupu(tip: string) {
	
		if (!this.selectedGroupId) {
  			   alert('Please enter a valid Group ID.1');
 			   return;
 			 }
		else{this.groupService.getAllForUser().subscribe(posts => {
  		const isIdFound = posts.some(post => post.id === this.selectedGroupId);
  		if (isIdFound) {
    		console.log('ID postoji.');
      this.groupService.posZahtev(this.selectedGroupId)
        .subscribe(res => {
          this.forgeResonseObj(this.OneGroupNResponse, res);
        }, err => {
          this.forgeResonseObj(this.OneGroupNResponse, err);
        });
  		}else {
    		alert('Pogresan id');
  				}	
			}, error => {
  			console.error(error);
			});} 
     
  }
  sortComments(tip: string) {
	if(this.authService.tokenIsPresent()){
	if(this.selectedPostId2){
	 const body = {
    sort:tip,
    post:this.selectedPostId2
  };
	console.log("sortiranje "+tip);
    this.comService.sort(body)
      .subscribe(res => {
          this.forgeResonseObj(this.CommentsResponse, res, '/api/comments/sort');
        }, err => {
          this.forgeResonseObj(this.CommentsResponse, err, '/api/comments/sort');
        });
      }else{alert("Morate upisati id posta ")}}
     else(alert("Morate se ulogovati"))
  }
    sortComments2(tip: string) {
	if(this.authService.tokenIsPresent()){
	 const body = {
    sort:tip
  };
	console.log("sortiranje "+tip);
    this.comService.sort2(body)
      .subscribe(res => {
          this.forgeResonseObj(this.allPostRndmResponse, res, '/api/posts/sorted');
        }, err => {
          this.forgeResonseObj(this.allPostRndmResponse, err, '/api/posts/sorted');
        });
     }
     else(alert("Morate se ulogovati"))
  }
    kreirajReakciju2(tipReakcije: string, postId: number, commentId: number) {
	if (!this.selectedComId){
			 alert('Unesi id komentara ');
		}else{
  const novaReakcija = {
    type: tipReakcije,
    post: postId,
    comment: commentId
  };
 this.postService.react(novaReakcija)
    .subscribe(res => {
      this.comService.getOneCom(this.selectedComId).subscribe(updatedPost => {
        this.forgeResonseObj(this.OneComResponse, updatedPost, '/api/reactions/create');
        console.log(updatedPost);
      });
    }, err => {
      this.forgeResonseObj(this.OneComResponse, err, '/api/reactions/create');
    });
 /*   this.postService.react(novaReakcija)//'http://localhost:8080/api/reactions/create', novaReakcija)
      .subscribe(res => {
          this.forgeResonseObj(this.OnePostResponse2, res, '/api/reactions/create');
        }, err => {
          this.forgeResonseObj(this.OnePostResponse2, err, '/api/reactions/create');
        });
*/
      }
  }
  /*    reportuj3(Id: number) {
	if (!this.selectedComId){
			 alert('Unesi id komentara ');
		}else{
  const novaReakcija = {
    id:Id
  };
 this.userService.report3(novaReakcija)
    .subscribe(res => {
      this.comService.getOneCom(this.selectedComId).subscribe(updatedPost => {
        this.forgeResonseObj(this.OneComResponse, updatedPost, '/api/reactions/create');
        console.log(updatedPost);
      });
    }, err => {
      this.forgeResonseObj(this.OneComResponse, err, '/api/reactions/create');
    });
 /*   this.postService.react(novaReakcija)//'http://localhost:8080/api/reactions/create', novaReakcija)
      .subscribe(res => {
          this.forgeResonseObj(this.OnePostResponse2, res, '/api/reactions/create');
        }, err => {
          this.forgeResonseObj(this.OnePostResponse2, err, '/api/reactions/create');
        });
*/
     /* }
  }*/	reportuj3(selectedComId: any, selectedReason: string) {
  if (!this.selectedComId) {
    alert('Unesi id posta');
    return;
  }

else{  this.comService.getAll().subscribe(posts => {
    const selectedPost = posts.find(post => post.id === selectedComId);
    if (selectedPost) {
      const novaReakcija = {
        id: selectedPost,
        reason: selectedReason,
      };

      this.userService.report3(novaReakcija).subscribe(res => {
        this.comService.getOneCom(this.selectedComId).subscribe(updatedPost => {
          this.forgeResonseObj(this.OneComResponse, updatedPost, '/api/reactions/create');
          console.log(updatedPost);
        });
      }, err => {
        this.forgeResonseObj(this.OneComResponse, err, '/api/reactions/create');
      });
    }
  });}
}
    onSubmit3() {
	if (this.authService.tokenIsPresent()) {
		 if (!this.form3.value.text){
			 alert('Tekst je obavezno polje ');
		}else{
    console.warn('Your order has been submitted', this.form3.value);
    console.log(this.form3.value+"ds");
     this.comService.add(this.form3.value)
        .subscribe(res => {
		console.log(res);
          this.forgeResonseObj(this.CommentsResponse, res, "api/comments/create");
        }, err => {
          this.forgeResonseObj(this.CommentsResponse, err, "api/comments/create");
        });

	this.form3.patchValue({
  text: ''
});;}
	this.commenting=false;
}
	 else {
    alert('Morate se prvo ulogovati');
    // Dodajte logiku koju želite da primenite ako korisnik nije ulogovan
  }
  }
     reaguj(tipReakcije: string, postId: number, commentId: number) {
	if (this.authService.tokenIsPresent()) {
 const novaReakcija = {
    type: tipReakcije,
    post: postId,
    comment: commentId
  };


     this.postService.react(novaReakcija)
        .subscribe(res => {
		console.log(res);
          this.forgeResonseObj(this.OnePostResponse2, res, '/api/reactions/create');
        }, err => {
          this.forgeResonseObj(this.OnePostResponse2, err, '/api/reactions/create');
        });

}
	 else {
    alert('Morate se prvo ulogovati');
    // Dodajte logiku koju želite da primenite ako korisnik nije ulogovan
  }
  }
  
    onSubmit4() {
	if (!this.selectedPostId2) {
  			   alert('Please enter a valid Comm ID.');
 			   return;
 			 }
	if (this.authService.tokenIsPresent()) {
		 if (!this.form4.value.text){
			 alert('Tekst je obavezno polje');
		}else{
    console.warn('Your order has been submitted', this.form4.value);
    console.log(this.form4.value+"ds");
     this.comService.replyToCom(this.selectedComId,this.form4.value)
        .subscribe(res => {
		console.log(res);
          this.forgeResonseObj(this.OneComResponse, res, "api/comments/{commentId}/reply");
        }, err => {
          this.forgeResonseObj(this.OneComResponse, err, "api/comments/{commentId}/reply");
        });

	this.form4.patchValue({
  text: ''
});;}
	this.replying=false;
}
	 else {
    alert('Morate se prvo ulogovati');
    // Dodajte logiku koju želite da primenite ako korisnik nije ulogovan
  }
  }
      onSubmit5() {
	if(!this.selectedComIdEdit){
		alert("Morate uneti id komentara koji azurirate")
	}else{this.comService.getAllFromUser().subscribe(comments => {
  		const isIdFound = comments.some(com => com.id === this.selectedComIdEdit);
  		if (isIdFound) {
    		console.log('ID postoji.');
      	if (this.authService.tokenIsPresent()) {
		 if (!this.form5.value.text){
			 alert('Tekst je obavezno polje');
		}else{
	    this.form5.get("id").setValue(this.selectedComIdEdit);
    console.warn('Your order has been submitted', this.form5.value);
    console.log(this.form5.value+"ds");
     this.comService.editCom(this.form5.value)
        .subscribe(res => {
		console.log(res);
          this.forgeResonseObj(this.OneComResponse2, res, "api/comments/edit");
        }, err => {
          this.forgeResonseObj(this.OneComResponse2, err, "api/comments/edit");
        });

	this.form5.patchValue({
  text: ''
});;}
	}
        }else {
    		alert('Pogresan id');
  				}	
			}, error => {
  			console.error(error);
			});}
	
  }
   onSubmit8() {
	if(!this.selectedGroupIdEdit){
		alert("Morate uneti id grupe koji azurirate")
	}else{this.groupService.getAllFromUser().subscribe(comments => {
  		const isIdFound = comments.some(com => com.id === this.selectedGroupIdEdit);
  		if (isIdFound) {
    		console.log('ID postoji.');
      	if (this.authService.tokenIsPresent()) {
		 if (!this.form8.value){
			 alert('Tekst je obavezno polje');
		}else{
	    this.form8.get("id").setValue(this.selectedGroupIdEdit);
    console.warn('Your order has been submitted', this.form8.value);
    console.log(this.form8.value+"ds");
     this.groupService.editGroup(this.form8.value)
        .subscribe(res => {
		console.log(res);
          this.forgeResonseObj(this.OneGroupResponse2, res, "api/groups/edit");
        }, err => {
          this.forgeResonseObj(this.OneGroupResponse2, err, "api/groups/edit");
        });

	this.form8.patchValue({
  opis: '',
  ime:''
});;}
	}
        }else {
    		alert('Pogresan id');
  				}	
			}, error => {
  			console.error(error);
			});}
	
  }
  
  
   addComment(postId?) {
	 this.commenting = true;
	 //console.log(postId,this.selectedPostId);
	 this.form3 = this.formBuilder.group({
      post: this.postService.getOnePost2(postId),
      text: ['', Validators.compose([Validators.required])]
    });
/*      this.route.params
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe((params: DisplayMessage) => {
        this.notification = params;
      });
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
	 this.postService.getOnePost(postId)
    .subscribe(
      post => {
        // Ovde možete pristupiti vrednostima images i content iz post objekta
        const images = post.images;
        const content = post.content;
  		 this.form.get("content").setValue(content)
  		 this.form.get("images").setValue(this.getImagesPathString(images));
        // Možete ih koristiti u daljoj logici
        console.log('Images:', images);
        console.log('Content:', content);
      },
      error => {
        // Ovde možete rukovati greškom ako dođe do problema pri dobijanju posta
        console.error(error);
      }
    );*/
  
    this.form3.get("post").setValue(postId);
 
  }

  kreirajReakciju3(tipReakcije: string, postId: number, commentId: number) {
  const novaReakcija = {
    type: tipReakcije,
    post: postId,
    comment: commentId
  };

    this.apiService.post('http://localhost:8080/api/reactions/create', novaReakcija)
      .subscribe(res => {
          this.forgeResonseObj(this.OneComResponse, res, 'http://localhost:8080/api/reactions/create');
        }, err => {
          this.forgeResonseObj(this.UserCommsResponse, err, 'http://localhost:8080/api/reactions/create');
        });
      
  }
     replyToCom(comId?) {
	if (!this.selectedComId) {
  			   alert('Please enter a valid Comm ID.');
 			   return;
 			 }
	 this.replying=true;
	 this.form4 = this.formBuilder.group({
      post: this.postService.getOnePost2(this.selectedPostId2),
      text: ['', Validators.compose([Validators.required])]
    });
/*      this.route.params
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe((params: DisplayMessage) => {
        this.notification = params;
      });
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
	 this.postService.getOnePost(postId)
    .subscribe(
      post => {
        // Ovde možete pristupiti vrednostima images i content iz post objekta
        const images = post.images;
        const content = post.content;
  		 this.form.get("content").setValue(content)
  		 this.form.get("images").setValue(this.getImagesPathString(images));
        // Možete ih koristiti u daljoj logici
        console.log('Images:', images);
        console.log('Content:', content);
      },
      error => {
        // Ovde možete rukovati greškom ako dođe do problema pri dobijanju posta
        console.error(error);
      }
    );*/
  
    this.form4.get("post").setValue(this.selectedPostId2);
 
  }
  editPost(postId?) {
	 this.editing = true;
	 this.form = this.formBuilder.group({
      id: postId,
      content: ['', Validators.compose([Validators.required])],
      images:['']
    });
      this.route.params
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe((params: DisplayMessage) => {
        this.notification = params;
      });
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
	 this.postService.getOnePost(postId)
    .subscribe(
      post => {
        // Ovde možete pristupiti vrednostima images i content iz post objekta
        const images = post.images;
        const content = post.content;
  		 this.form.get("content").setValue(content)
  		 this.form.get("images").setValue(this.getImagesPathString(images));
        // Možete ih koristiti u daljoj logici
        console.log('Images:', images);
        console.log('Content:', content);
      },
      error => {
        // Ovde možete rukovati greškom ako dođe do problema pri dobijanju posta
        console.error(error);
      }
    );
  
    this.form.get("id").setValue(postId);
 
  }
getImagesSize1(images: any): string[] {
  const imagePaths2: string[] = [];
  if (images) {
    for (const image of images.values()) {
      imagePaths2.push(decodeURIComponent(image.path));
    }
  }
  return imagePaths2;
}
  makeRequest(path) {
	
	if (this.authService.tokenIsPresent()) {
  console.log(path);
  
	
   if (this.config.whoami_url.endsWith(path)) {
	console.log(path);
      this.userService.getMyInfo()
        .subscribe(res => {
          this.forgeResonseObj(this.whoamIResponse, res, path);
        }, err => {
          this.forgeResonseObj(this.whoamIResponse, err, path);
        });
    }else if (this.config.osam.endsWith(path)) {
	console.log(path);
	this.sort2=true;
      this.postService.getAll()
        .subscribe(res => {
          this.forgeResonseObj(this.allPostResponse, res, path);
        }, err => {
          this.forgeResonseObj(this.allPostResponse, err, path);
        }); 
        }
        else if (this.config.devet.endsWith(path)) {
	console.log(path);
	this.sort2=true;
      this.postService.getAllRndm()
        .subscribe(res => {
          this.forgeResonseObj(this.allPostRndmResponse, res, path);
        }, err => {
          this.forgeResonseObj(this.allPostRndmResponse, err, path);
        }); 
        }else if (this.config.dva.endsWith(path)) {
	console.log(path);
      this.groupService.getAll()
        .subscribe(res => {
          this.forgeResonseObj(this.allGroupResponse, res, path);
        }, err => {
          this.forgeResonseObj(this.allGroupResponse, err, path);
        }); 
        }
        else if (this.config.tri.endsWith(path)) {
	console.log(path);
      this.groupService.getAllForUser()
        .subscribe(res => {
          this.forgeResonseObj(this.allGroupForUserResponse, res, path);
        }, err => {
          this.forgeResonseObj(this.allGroupForUserResponse, err, path);
        }); 
        }
         else if (this.config.pet.endsWith(path)) {
	console.log(path);
      this.groupService.getAllForUser2()
        .subscribe(res => {
          this.forgeResonseObj(this.allGroupForUser2Response, res, path);
        }, err => {
          this.forgeResonseObj(this.allGroupForUser2Response, err, path);
        }); 
        }else if (this.config.osamnaest.endsWith(path)) {
	console.log(path);
		this.editCom=true;
		this.form8
      	this.comService.getAllFromUser()
        .subscribe(res => {
          this.forgeResonseObj(this.UserCommsResponse, res, path);
        }, err => {
          this.forgeResonseObj(this.UserCommsResponse, err, path);
        }); 
        }
        else if (this.config.sesnaest.endsWith(path)) {
	console.log(path);
		this.editGroup=true;
      	this.groupService.getAllFromUser()
        .subscribe(res => {
          this.forgeResonseObj(this.UserGroupsResponse, res, path);
        }, err => {
          this.forgeResonseObj(this.UserGroupsResponse, err, path);
        }); 
        }else if (this.config.jedanaest.endsWith(path)) {
	console.log(path);
	if (!this.selectedPostId2) {
  			   alert('Please enter a valid Comm ID.');
 			   return;
 			 }
 			 this.sort=true;
	this.reply=true;
	console.log(path);
      this.comService.getPostComments(this.selectedPostId2)
        .subscribe(res => {
	this.comments=res;
          this.forgeResonseObj(this.CommentsResponse, res, path);
        }, err => {
          this.forgeResonseObj(this.CommentsResponse, err, path);
        });
        }else if (this.config.trinaest.endsWith(path)) {
	console.log(path);
      this.postService.getAllFromUser()
        .subscribe(res => {
          this.forgeResonseObj(this.allPostFromUserResponse, res, path);
        }, err => {
          this.forgeResonseObj(this.allPostFromUserResponse, err, path);
        });
        }else if (this.config.dvanaest==path) {
	console.log(path);
		
		if (!this.selectedComId) {
  			   alert('Please enter a valid Comm ID.');
 			   return;
 			 }
 		this.reply=true;
 		this.comService.getPostComments(this.selectedPostId2).subscribe(comments => {
		
  		const isIdFound = comments.some(com => com.id === this.selectedComId);
  		if (isIdFound) {
    		console.log('ID postoji.');
    			alert("evo ga id"+this.selectedComId);
      	this.comService.getOneCom(this.selectedComId)
        .subscribe(res => {
          this.forgeResonseObj(this.OneComResponse, res, path);
        }, err => {
          this.forgeResonseObj(this.OneComResponse, err, path);
        });
        }else {
    		alert('Pogresan id');
  				}	
			}, error => {
  			console.error(error);
			});

    	} 
    	else if (this.config.devetnaest.endsWith(path)) {
	console.log(path);
	alert("hajha"+this.config.getOneCom2_url+path);
		if (!this.selectedComIdEdit) {
  			   alert('Please enter a valid Comm ID.');
 			   return;
 			 }
 		this.comService.getAllFromUser().subscribe(comments => {
  		const isIdFound = comments.some(com => com.id === this.selectedComIdEdit);
  		if (isIdFound) {
    		console.log('ID postoji.');
      	this.comService.getOneCom(this.selectedComIdEdit)
        .subscribe(res => {
          this.forgeResonseObj(this.OneComResponse2, res, path);
        }, err => {
          this.forgeResonseObj(this.OneComResponse2, err, path);
        });
        }else {
    		alert('Pogresan id');
  				}	
			}, error => {
  			console.error(error);
			});

    	} 
    	   	else if (this.config.nula.endsWith(path)) {
console.log(path);
		if (!this.selectedGroupIdEdit) {
  			   alert('Please enter a valid group ID.3');
 			   return;
 			 }else{	this.groupService.getAllFromUser().subscribe(comments => {
  		const isIdFound = comments.some(com => com.id === this.selectedGroupIdEdit);
  		if (isIdFound) {
    		console.log('ID postoji.');
      	this.groupService.getOneGroup(this.selectedGroupIdEdit)
        .subscribe(res => {
          this.forgeResonseObj(this.OneGroupResponse2, res, path);
        }, err => {
          this.forgeResonseObj(this.OneGroupResponse2, err, path);
        });
        }else {
    		alert('Pogresan id');
  				}	
			}, error => {
  			console.error(error);
			});

    	} }
    	else if (this.config.nula2==path) {
		console.log(path);
		if (!this.selectedGroupIdEdit) {
  			   alert('Please enter a valid group ID.2');
 			   return;
 			 }else{	this.groupService.getAllFromUser().subscribe(comments => {
  		const isIdFound = comments.some(com => com.id === this.selectedGroupIdEdit);
  		if (isIdFound) {
	
    		console.log('ID postoji.');
      	this.groupService.getGroupRequests(this.selectedGroupIdEdit)
        .subscribe(res => {
			this.open=true;
          this.forgeResonseObj(this.GroupRequestsResponse, res, path);
        }, err => {
          this.forgeResonseObj(this.GroupRequestsResponse, err, path);
        });
        }else {
    		alert('Pogresan id');
  				}	
			}, error => {
  			console.error(error);
			});

    	} }
        else if (this.config.cetrnaest.endsWith(path)) {
	console.log(path);
		this.edit=true;
	 	if (!this.selectedPostId) {
  			   alert('Please enter a valid Post ID.1');
 			   return;
 			 }
 		this.postService.getAllFromUser().subscribe(posts => {
  		const isIdFound = posts.some(post => post.id === this.selectedPostId);
  		if (isIdFound) {
    		console.log('ID postoji.');
      this.postService.getOnePost(this.selectedPostId)
        .subscribe(res => {
          this.forgeResonseObj(this.OnePostResponse, res, path);
        }, err => {
          this.forgeResonseObj(this.OnePostResponse, err, path);
        });
  		}else {
    		alert('Pogresan id');
  				}	
			}, error => {
  			console.error(error);
			});

    } 
     else if (this.config.dvadeset==path) {
	console.log(path);
	console.log("pozv"+path);
 		this.userService.getAllReports().subscribe(res => {
	console.log(res);
          this.forgeResonseObj(this.ReportResponse, res, path);
        }, err => {
          this.forgeResonseObj(this.ReportResponse, err, path);
        });
  			
		

    }  else if (this.config.getOneGroup_url.endsWith(path)) {
		console.log(path);
	 	if (!this.selectedGroupId) {
  			   alert('Please enter a valid Group ID.1');
 			   return;
 			 }
 		this.groupService.getAll().subscribe(posts => {
  		const isIdFound = posts.some(post => post.id === this.selectedGroupId);
  		if (isIdFound) {
    		console.log('ID postoji.');
      this.groupService.getOneGroup(this.selectedGroupId)
        .subscribe(res => {
          this.forgeResonseObj(this.OneGroupResponse, res, path);
        }, err => {
          this.forgeResonseObj(this.OneGroupResponse, err, path);
        });
  		}else {
    		alert('Pogresan id');
  				}	
			}, error => {
  			console.error(error);
			});

    } 
     else if (this.config.cetiri.endsWith(path)) {
		console.log(path);
	 	if (!this.selectedGroupId) {
  			   alert('Please enter a valid Group ID.1');
 			   return;
 			 }
else{	this.groupService.getAllForUser().subscribe(posts => {
  		const isIdFound = posts.some(post => post.id === this.selectedGroupId);
  		if (isIdFound) {
    		console.log('ID postoji.');
      this.groupService.getOneGroupN(this.selectedGroupId)
        .subscribe(res => {
          this.forgeResonseObj(this.OneGroupNResponse, res, path);
        }, err => {
          this.forgeResonseObj(this.OneGroupNResponse, err, path);
        });
  		}else {
    		alert('Pogresan id');
  				}	
			}, error => {
  			console.error(error);
			});} 	

    }else if (this.config.sest==path) {
		console.log(path);
	 	if (!this.selectedGroupId3) {
  			   alert('Please enter a valid Group ID.1');
 			   return;
 			 }
 		this.groupService.getAllForUser2().subscribe(posts => {
  		const isIdFound = posts.some(post => post.id === this.selectedGroupId3);
  		if (isIdFound) {
    		console.log('ID postoji.');
      this.groupService.getOneGroupU(this.selectedGroupId3)
        .subscribe(res => {
          this.forgeResonseObj(this.OneGroupUResponse, res, path);
        }, err => {
          this.forgeResonseObj(this.OneGroupUResponse, err, path);
        });
  		}else {
    		alert('Pogresan id');
  				}	
			}, error => {
  			console.error(error);
			});

    }else if (this.config.sedamnaest.endsWith(path)) {
		console.log(path);
	 	if (!this.selectedGroupIdEdit) {
  			   alert('Please enter a valid Group ID.1');
 			   return;
 			 }
 	else{	this.groupService.getAllForUser3().subscribe(posts => {
  		const isIdFound = posts.some(post => post.id === this.selectedGroupIdEdit);
  		if (isIdFound) {
    		console.log('ID postoji.');
      this.groupService.getAllGroupPostsYour(this.selectedGroupIdEdit)
        .subscribe(res => {
          this.forgeResonseObj(this.YourGroupPostsResponse, res, path);
        }, err => {
          this.forgeResonseObj(this.YourGroupPostsResponse, err, path);
        });
  		}else {
    		alert('Pogresan id');
  				}	
			}, error => {
  			console.error(error);
			});}

    }
    else if (this.config.allGroupPosts_url.endsWith(path)) {
		console.log(path);
	 	if (!this.selectedGroupId3) {
  			   alert('Please enter a valid Group ID.1');
 			   return;
 			 }
 	else{	this.groupService.getAllForUser2().subscribe(posts => {
  		const isIdFound = posts.some(post => post.id === this.selectedGroupId3);
  		if (isIdFound) {
    		console.log('ID postoji.');
      this.groupService.getAllGroupPosts(this.selectedGroupId3)
        .subscribe(res => {
          this.forgeResonseObj(this.GroupPostsResponse, res, path);
        }, err => {
          this.forgeResonseObj(this.GroupPostsResponse, err, path);
        });
  		}else {
    		alert('Pogresan id');
  				}	
			}, error => {
  			console.error(error);
			});}

    }
    else if (this.config.deset==path) {
	console.log(path);
	console.log(this.selectedPostId2);
	 	if (!this.selectedPostId2) {
  			   alert('Please enter a valid Post IDDD.');
 			   return;
 			 }
 		this.postService.getAll().subscribe(posts => {
  		const isIdFound = posts.some(post => post.id === this.selectedPostId2);
  		if (isIdFound) {
    		console.log('ID postoji.');
    			 this.comment=true;
      this.postService.getOnePost(this.selectedPostId2)
        .subscribe(res => {
          this.forgeResonseObj(this.OnePostResponse2, res, path);
        }, err => {
          this.forgeResonseObj(this.OnePostResponse2, err, path);
        });
  		}else {
    		alert('Pogresan id');
  				}	
			}, error => {
  			console.error(error);
			});

    } else if (this.config.jedan==path){
	console.log(path);
      this.userService.getAll()
        .subscribe(res => {
          this.forgeResonseObj(this.allUserResponse, res, path);
        }, err => {
          this.forgeResonseObj(this.allUserResponse, err, path);
        });
    }
     }
	 else {
    alert('Morate se prvo ulogovati');
    // Dodajte logiku koju želite da primenite ako korisnik nije ulogovan
  }
  }
getImagesPathString(postImages:any): string {
  return this.getImagesSize1(postImages).join(', '); // Spajamo putanje slika separatorom ', '
}

  
  deleteGroup(groupId: number) {
		 if (!this.selectedGroupIdEdit) {
  			   alert('Please enter a valid Group ID.');
 			   return;
 			 }
 		this.groupService.getAllFromUser().subscribe(posts => {
  		const isIdFound = posts.some(post => post.id === this.selectedGroupIdEdit);
  		if (isIdFound) {
    		console.log('ID postoji.');
      this.groupService.delete(this.selectedGroupIdEdit)
        .subscribe(res => {
          this.forgeResonseObj(this.OneGroupResponse2, res, 'api/groups/delete');
        }, err => {
          this.forgeResonseObj(this.OneGroupResponse2, err, 'api/groups/delete');
        });
  		}else {
    		alert('Pogresan id');
  				}	
			}, error => {
  			console.error(error);
			});
	}
	  deletePost(groupId: number) {
		 if (!this.selectedPostId) {
  			   alert('Please enter a valid post ID.');
 			   return;
 			 }
 		this.postService.getAllFromUser().subscribe(posts => {
  		const isIdFound = posts.some(post => post.id === this.selectedPostId);
  		if (isIdFound) {
    		console.log('ID postoji.');
      this.postService.delete(this.selectedPostId)
        .subscribe(res => {
          this.forgeResonseObj(this.OnePostResponse, res, 'api/groups/delete');
        }, err => {
          this.forgeResonseObj(this.OnePostResponse, err, 'api/groups/delete');
        });
  		}else {
    		alert('Pogresan id');
  				}	
			}, error => {
  			console.error(error);
			});
	}
  forgeResonseObj(obj, res, path?) {
    obj['path'] = path;
    obj['method'] = 'GET';
    
    if (res.ok === false) {
      // err
      obj['status'] = res.status;
      try {
        obj['body'] = JSON.stringify(JSON.parse(res._body), null, 2);
      } catch (err) {
        console.log(res);
        obj['body'] = res.error.message;
      }
    } else {
      // 200
      obj['status'] = 200;
      obj['body'] = JSON.stringify(res, null, 2);
    }
  }

}
