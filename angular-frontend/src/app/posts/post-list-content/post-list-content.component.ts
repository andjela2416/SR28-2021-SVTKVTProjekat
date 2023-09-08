import { Component, OnInit } from '@angular/core';
import { Route, Router } from '@angular/router';
import { PostService } from 'src/app/service/post.service';
import { AuthService } from 'src/app/service/auth.service';

@Component({
  selector: 'app-post-list-content',
  templateUrl: './post-list-content.component.html',
  styleUrls: ['./post-list-content.component.css']
})
export class PostListContentComponent implements OnInit {
  postList: any[];
  your=true;
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
    this.postService.getAllFromUser().subscribe((posts) => (this.postList= posts))
  }
  navigateTo(){
    this.router.navigate(['posts/create'])
  }
	goBack() {
    window.history.back();
}
}
