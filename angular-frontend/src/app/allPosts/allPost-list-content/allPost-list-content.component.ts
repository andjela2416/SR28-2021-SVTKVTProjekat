import { Component, OnInit } from '@angular/core';
import { Route, Router } from '@angular/router';
import { PostService } from 'src/app/service/post.service';
import { UserService } from 'src/app/service/user.service';
import { AuthService } from 'src/app/service/auth.service';
import { FormBuilder, FormGroup,FormControl } from '@angular/forms';
import { Validators } from '@angular/forms'
import { ChangeDetectorRef } from '@angular/core';
import { of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';
import { tap } from 'rxjs/operators';



@Component({
  selector: 'app-allPost-list-content',
  templateUrl: './allPost-list-content.component.html',
  styleUrls: ['./allPost-list-content.component.css']
})
export class AllPostListContentComponent implements OnInit {
  postList: any[]
  postReactionCounts: Map<number, any>;
  currentUser:any;

  constructor(
    private postService:PostService,
    private router:Router,
    private authService:AuthService,
    private userService:UserService,
    private cdr: ChangeDetectorRef,
  ) { }

  ngOnInit() {
	if (this.authService.tokenIsPresent()) {
		this.getPosts()
	 }
	this.postReactionCounts = new Map<number, any>(); 

	 
  }
	

  getPosts() {
	  this.postService.getAllRndm().pipe(
	    mergeMap((postsRndm) => {
	      this.postList = postsRndm;
	      console.log(postsRndm);
	
	      if (this.authService.tokenIsPresent()) {
	        return this.userService.getMyInfo().pipe(
	          mergeMap((user) => {
	            this.currentUser = user;
	            console.log(user);
	
	            if (user.role === 'ADMIN') {
	              return this.postService.getAll().pipe(
	                tap((postsAdmin) => {
	                  this.postList = postsAdmin;
	                  this.cdr.detectChanges();
	                })
	              );
	            } else {
	              return of(null); 	            }
	          })
	        );
	      } else {
	        return of(null); 
	      }
	    })
	  ).subscribe(() => {
	  });
	}

  
  
  navigateTo(){
    this.router.navigate(['posts/create'])
  }
	goBack() {
    window.history.back();
}

}