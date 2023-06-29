import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Subject } from 'rxjs/Subject';
import { takeUntil } from 'rxjs/operators';
import { PostService } from 'src/app/service/post.service';
import { AuthService } from 'src/app/service/auth.service';

interface DisplayMessage {
  msgType: string;
  msgBody: string;
}


@Component({
  selector: 'app-post-list',
  templateUrl: './post-list.component.html',
  styleUrls: ['./post-list.component.css']
})
export class PostListComponent implements OnInit {
  @Input() posts: any[]
  editing = false;
  form: FormGroup
  private ngUnsubscribe: Subject<void> = new Subject<void>();
  notification: DisplayMessage;
  returnUrl: string;



  constructor(
    private postService: PostService,
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
     private authService: AuthService
  ) {}

   ngOnInit() {
   
  }

  getImagesSize(post: any) {
	//for (const post of this.posts) 
	for (var i in post.images) {
  console.log(post.images[i].path) 
  return post.images[i].path
}
	}
getImagesSize2(post: any): string[] {
  const imagePaths: string[] = [];
  if (post.images) {
    for (const image of post.images.values()) {
      imagePaths.push(image.path);
    }
  }
  return imagePaths;
}
  
  deletePost(postId: number) {
    console.log(postId+"a") 
    this.postService.delete(postId).subscribe((posts) => {this.postService.getAllFromUser()});

 // this.postService.delete(postId).subscribe(() => {
   // this.postService.getAllFromUser().subscribe(posts => {
      // Ažuriraj prikaz svih objava nakon brisanja
    //});
  //});

  }
  editPost(postId, postContent) {
    this.editing = true;
    this.route.params
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe((params: DisplayMessage) => {
        this.notification = params;
      });
    // get return url from route parameters or default to '/'
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
    this.form = this.formBuilder.group({
      id: postId,
      content: ['', Validators.compose([Validators.required, Validators.minLength(3), Validators.maxLength(64)])],
    });
    this.form.get("content").setValue(postContent)
  }

  onSubmit(){
    this.postService.edit(this.form.value).subscribe((result) => {
    });
  }


}
