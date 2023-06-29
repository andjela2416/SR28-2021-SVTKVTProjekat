import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import {UserService} from '../service/user.service';
import {PostService} from '../service/post.service';
import {ConfigService} from '../service/config.service';
import {FormControl, FormGroup} from "@angular/forms";
import { AuthService } from '../service/auth.service';
import { Validators } from '@angular/forms';


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
  allPostFromUserResponse= {};
  OnePostResponse= {};
  selectedPostId: number; 
  submitted = false;
  constructor(
    private config: ConfigService,
    private userService: UserService,
    private postService : PostService,
    private router: Router,
    private authService: AuthService
  ) {
  }
forma = new FormGroup({
    post: new FormControl('', Validators.required),
    pathSlike: new FormControl('')
  });

  ngOnInit() {
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
  makeRequest(path) {
	
  console.log(this.selectedPostId);
  
	
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
        }else if (this.config.postsFromUser_url.endsWith(path)) {
      this.postService.getAllFromUser()
        .subscribe(res => {
          this.forgeResonseObj(this.allPostFromUserResponse, res, path);
        }, err => {
          this.forgeResonseObj(this.allPostFromUserResponse, err, path);
        });
        }else if (this.config.getOnePost_url.endsWith(path)) {
	 if (!this.selectedPostId) {
    // Validacija unosa ID-a posta
  			   alert('Please enter a valid Post ID.');
 			   return;
 			 }
      this.postService.getOnePost(this.selectedPostId)
        .subscribe(res => {
          this.forgeResonseObj(this.OnePostResponse, res, path);
        }, err => {
          this.forgeResonseObj(this.OnePostResponse, err, path);
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


  forgeResonseObj(obj, res, path) {
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
