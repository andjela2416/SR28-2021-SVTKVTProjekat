import { Component, OnInit } from '@angular/core';
import { Route, Router } from '@angular/router';
import { PostService } from 'src/app/service/post.service';
import { AuthService } from 'src/app/service/auth.service';

@Component({
  selector: 'app-your-comments-content',
  templateUrl: './your-comments-content.component.html',
  styleUrls: ['./your-comments-content.component.css']
})
export class YourCommentsContentComponent implements OnInit {
  postList: any[]

  constructor(
    private postService:PostService,
    private router:Router,
    private authService:AuthService
  ) { }

  ngOnInit() {
	if (this.authService.tokenIsPresent()) {
		this.getPosts()
	 }
  }

  getPosts(){
    this.postService.getAllUserComments().subscribe((posts) => (this.postList= posts))
  }
  navigateTo(){
    this.router.navigate(['posts/create'])
  }
	goBack() {
    window.history.back();
}
}
