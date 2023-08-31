import { Component, Input, OnInit, ChangeDetectionStrategy } from '@angular/core';
import { FormBuilder, FormGroup, Validators,FormControl } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject } from 'rxjs/Subject';
import { takeUntil } from 'rxjs/operators';
import { PostService } from 'src/app/service/post.service';
import { UserService } from 'src/app/service/user.service';
import { AuthService } from 'src/app/service/auth.service';
import { ChangeDetectorRef } from '@angular/core';

interface DisplayMessage {
  msgType: string;
  msgBody: string;
}
export interface Post {
  id: number;
  content: string;
  imageIds: number[];
}

@Component({
  selector: 'app-your-comments',
  templateUrl: './your-comments.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  styleUrls: ['./your-comments.component.css']
})
export class YourCommentsComponent implements OnInit {
  @Input() posts: any[]
  editing = false;
  form: FormGroup
  private ngUnsubscribe: Subject<void> = new Subject<void>();
  notification: DisplayMessage;
  returnUrl: string;
   OnePostResponse= {};
  selectedImages: File[] = [];
  imagePaths: string[] = [];
  imagePaths2: string[] = [];
  selectedImageIds: number[] = [];
  imageId: number = 1;
  submitted = false;

  constructor(
	private cdr: ChangeDetectorRef,
    private postService: PostService,
    private userService: UserService,
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
     private authService: AuthService
  ) {}

   ngOnInit() {
  this.getPosts();
  this.postService.getAllFromUser().subscribe((posts) => {
    this.posts = posts;

    for (const post of this.posts) {
      for (const image of post.images) {
        console.log(image);
      }
    }
  });
}

 
getPosts() {
  this.postService.getAllFromUser().subscribe((posts) => {
    this.posts = posts;
  });
}

  
  deletePost(postId: number) {
  console.log(postId);
  this.postService.delete(postId).subscribe(() => {
    this.postService.getAllFromUser().subscribe((posts) => {
      this.posts = posts; 
        this.cdr.detectChanges();
    });
  });
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

getImagesSize2(post: any): string[] {
  const imagePaths: string[] = [];
  if (post.images) {
    for (const image of post.images.values()) {
      imagePaths.push(decodeURIComponent(image.path));
    }
  }
  return imagePaths;
}

send(){
	    const editFormElement = document.getElementById('edit-form');
	if (editFormElement) {
	    editFormElement.scrollIntoView({ behavior: 'smooth', block: 'start' });
	}
}

  editPost(postId, postContent,postImages) {
    this.editing = true;
    this.form = this.formBuilder.group({
      id: postId,
      content: ['', Validators.compose([Validators.required])],
      images:['']
    });
    this.form.get("id").setValue(postId);
    this.form.get("content").setValue(postContent)
    console.log(this.getImagesSize1(postImages));
    
    this.form.get("images").setValue(this.getImagesPathString(postImages));
    console.log(this.getImagesPathString(postImages));
    this.send();
  }

    onSubmit() {
	  
	if (this.authService.tokenIsPresent()) {
		 if (!this.form.value.content){
			 alert('Tekst je obavezno polje objave');
		}else{
    this.submitted = true;
    console.warn('Your order has been submitted', this.form.value);
    
     this.postService.edit(this.form.value).subscribe(() => {
    this.postService.getAllFromUser().subscribe((posts) => {
      this.posts = posts; 
      this.editing=false;
        this.cdr.detectChanges();
    });
  })

	this.form.patchValue({
  	content: '',
 	images: ''
	});;}
	
}
	 else {
    alert('Morate se prvo ulogovati');
    // Dodajte logiku koju želite da primenite ako korisnik nije ulogovan
  }
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

getImagesPathString(postImages:any): string {
  return this.getImagesSize1(postImages).join(','); // Spajamo putanje slika separatorom ', '
}

}
