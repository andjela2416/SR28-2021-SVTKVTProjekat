import { Component, OnInit } from '@angular/core';
import { Route, Router } from '@angular/router';
import { PostService } from 'src/app/service/post.service';
import { AuthService } from 'src/app/service/auth.service';
import { FormBuilder, FormGroup,FormControl } from '@angular/forms';
import { Validators } from '@angular/forms'

@Component({
  selector: 'app-allPost-list-content',
  templateUrl: './allPost-list-content.component.html',
  styleUrls: ['./allPost-list-content.component.css']
})
export class AllPostListContentComponent implements OnInit {
  postList: any[]
  postReactionCounts: Map<number, any>;

  constructor(
    private postService:PostService,
    private router:Router,
    private authService:AuthService
  ) { }

  ngOnInit() {
	if (this.authService.tokenIsPresent()) {
		this.getPosts()
	 }
	this.postReactionCounts = new Map<number, any>(); 

	 
  }
	

  getPosts(){
    this.postService.getAllRndm().subscribe((posts) => {
    this.postList= posts    
    })
  }
  navigateTo(){
    this.router.navigate(['posts/create'])
  }
	goBack() {
    window.history.back();
}

}