import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import {UserService} from '../service/user.service';
import {PostService} from '../service/post.service';
import {ConfigService} from '../service/config.service';
import { FormBuilder, FormGroup,FormControl } from '@angular/forms';
import { AuthService } from '../service/auth.service';
import { ComService } from '../service/com.service';
import { Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { takeUntil } from 'rxjs/operators';
import { Subject } from 'rxjs/Subject';
import { tap } from 'rxjs/operators';

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

  clubResponse = {};
  whoamIResponse = {};
  allUserResponse = {};
  allPostResponse = {};
  UserCommsResponse={};
  CommentsResponse = {};
  allPostFromUserResponse= {};
  OnePostResponse= {};
    OnePostResponse2= {};
   OneComResponse= {};
      OneComResponse2= {};
  selectedPostId: number; 
    selectedPostId2: number; 
  selectedComId: number; 
  submitted = false;
  editing=false;
  commenting=false;
  replying=false;
  reply=false;
  edit=false;
  editCom=false;
  selectedComIdEdit:number;
    form: FormGroup
    returnUrl: string;
    form3:FormGroup;
    form4:FormGroup;
    form5:FormGroup;
    notification: DisplayMessage;
    private ngUnsubscribe: Subject<void> = new Subject<void>();
  constructor(
    private config: ConfigService,
    private userService: UserService,
    private postService : PostService,
    private router: Router,
    private authService: AuthService,
    private comService:ComService,
     private route: ActivatedRoute,
         private formBuilder: FormBuilder,
  ) {
  }
forma = new FormGroup({
    post: new FormControl('', Validators.required),
    pathSlike: new FormControl('')
  });


  ngOnInit() {
			 this.form5 = this.formBuilder.group({
					id:this.selectedComIdEdit,
      text: ['', Validators.compose([Validators.required])]
    });	
  }
  showPostList() {
	
    this.router.navigate(['/post-list']);
  }

  onSubmit() {
	if (this.authService.tokenIsPresent()) {
		 if (!this.forma.value.post){
			 alert('Tekst je obavezno polje objave');
		}
	console.log(this.forma.value.pathSlike);
    this.submitted = true;
    console.warn('Your order has been submitted', this.forma.value);
    this.postService.create(this.forma.value)
	this.forma.reset();}
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
    onSubmit4() {
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
	}
	this.comService.getAllFromUser().subscribe(comments => {
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
			});
  }
  
   addComment(postId?) {
	 this.commenting = true;
	 
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
  
     replyToCom(comId?) {
	 this.replying = true;
	 
	 this.form4 = this.formBuilder.group({
      post: this.postService.getOnePost2(this.selectedPostId),
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
  
    this.form4.get("post").setValue(this.selectedPostId);
 
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

      this.userService.getMyInfo()
        .subscribe(res => {
          this.forgeResonseObj(this.whoamIResponse, res, path);
        }, err => {
          this.forgeResonseObj(this.whoamIResponse, err, path);
        });
    }else if (this.config.posts_url.endsWith(path)) {
      this.postService.getAll()
        .subscribe(res => {
          this.forgeResonseObj(this.allPostResponse, res, path);
        }, err => {
          this.forgeResonseObj(this.allPostResponse, err, path);
        }); 
        }else if (this.config.usersComms_url.endsWith(path)) {
		this.editCom=true;
      	this.comService.getAllFromUser()
        .subscribe(res => {
          this.forgeResonseObj(this.UserCommsResponse, res, path);
        }, err => {
          this.forgeResonseObj(this.UserCommsResponse, err, path);
        }); 
        }else if (this.config.commentsForPost_url.endsWith(path)) {
	console.log(path);
      this.comService.getPostComments(this.selectedPostId)
        .subscribe(res => {
          this.forgeResonseObj(this.CommentsResponse, res, path);
        }, err => {
          this.forgeResonseObj(this.CommentsResponse, err, path);
        });
        }else if (this.config.postsFromUser_url.endsWith(path)) {
      this.postService.getAllFromUser()
        .subscribe(res => {
          this.forgeResonseObj(this.allPostFromUserResponse, res, path);
        }, err => {
          this.forgeResonseObj(this.allPostFromUserResponse, err, path);
        });
        }else if (this.config.getOneCom_url.endsWith(path)) {
		if (!this.selectedComId) {
  			   alert('Please enter a valid Comm ID.');
 			   return;
 			 }
 		this.reply=true;
 		this.comService.getPostComments(this.selectedPostId).subscribe(comments => {
  		const isIdFound = comments.some(com => com.id === this.selectedComId);
  		if (isIdFound) {
    		console.log('ID postoji.');
    			 this.edit=true;
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
    	else if (this.config.getOneCom_url+("/edit").endsWith(path)) {
		if (!this.selectedComIdEdit) {
  			   alert('Please enter a valid Comm ID.');
 			   return;
 			 }
 		this.comService.getAllFromUser().subscribe(comments => {
  		const isIdFound = comments.some(com => com.id === this.selectedComIdEdit);
  		if (isIdFound) {
    		console.log('ID postoji.');
    			 this.edit=true;
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
        else if (this.config.getOnePost_url.endsWith(path)) {
	alert("a");
	 	if (!this.selectedPostId) {
  			   alert('Please enter a valid Post ID.');
 			   return;
 			 }
 		this.postService.getAllFromUser().subscribe(posts => {
  		const isIdFound = posts.some(post => post.id === this.selectedPostId);
  		if (isIdFound) {
    		console.log('ID postoji.');
    			 this.edit=true;
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

    } else if (this.config.getOnePost_url+"/one".endsWith(path)) {
	alert("a");
	console.log(this.selectedPostId2);
	 	if (!this.selectedPostId2) {
  			   alert('Please enter a valid Post ID.');
 			   return;
 			 }
 		this.postService.getAll().subscribe(posts => {
  		const isIdFound = posts.some(post => post.id === this.selectedPostId2);
  		if (isIdFound) {
    		console.log('ID postoji.');
    			 this.edit=true;
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

    } else if (this.config.users_url.endsWith(path)){
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
