import { Component, Input, OnInit, ChangeDetectionStrategy ,NgZone} from '@angular/core';
import { FormBuilder, FormGroup, Validators,FormControl } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject } from 'rxjs/Subject';
import { takeUntil } from 'rxjs/operators';
import { PostService } from 'src/app/service/post.service';
import { ComService } from 'src/app/service/com.service';
import { GroupService } from 'src/app/service/group.service';
import { UserService } from 'src/app/service/user.service';
import { AuthService } from 'src/app/service/auth.service';
import { ChangeDetectorRef } from '@angular/core';
import { Location } from '@angular/common';
import { DatePipe } from '@angular/common';
import { Router } from '@angular/router';


interface DisplayMessage {
  msgType: string;
  msgBody: string;
}
export interface Group {
  id: number;
  name: string;
  description: string;
}

@Component({
  providers: [DatePipe],
  selector: 'app-groups-list',
  templateUrl: './groups-list.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  styleUrls: ['./groups-list.component.css']
})
export class GroupsListComponent implements OnInit {
  @Input() groups: any[];
  @Input() posts: any[];
  @Input() currentUser: any; 
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
  forma = new FormGroup({
    post: new FormControl('', Validators.required),
    pathSlike: new FormControl('')
  });
  submitted2=false;
  commentFormControl = new FormControl('');
  replyFormControl = new FormControl('');
  replyFormControl2 = new FormControl('');
  editingComment= false;
  editCommentForm: FormGroup; 
  selectedPostForEditing: any;
   editingComment2= false;
  editCommentForm2: FormGroup; 
  selectedPostForEditing2: any;
	selectedComForEditing: any;
	komKojiMenjamo:any;
	showDropdown = false;
	createGroup=false;
	form6 = new FormGroup({
    name: new FormControl('', Validators.required),
    description: new FormControl('')
  });
	commentDropdownStatus: { [commentId: number]: { open: boolean } } = {};
	chosedGroup = false;
	group: any;
	postList: any[];

	
  constructor(
	private location: Location,
	private cdr: ChangeDetectorRef,
	private ngZone: NgZone,
    private postService: PostService,
    private comService: ComService,
    private groupService: GroupService,
    private userService: UserService,
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private authService: AuthService,
    private router:Router,
     
  ) {	
	this.editCommentForm = this.formBuilder.group({
            text: ['', Validators.required],
            id:['', Validators.required]
        });
        this.editCommentForm2 = this.formBuilder.group({
            text: ['', Validators.required],
            id:['', Validators.required]
        });
  }
  
   replyFormControls: { [commentId: number]: FormControl } = {};

   replyFormControlName(comment: any): string {
	    return `reply_${comment.id}`;
	}
   commentsForPost=false; 
   vidiKom=true; 
   drugaKomp=false;
   

   ngOnInit() {


   this.postService.getAllRndm().subscribe((posts) => {
    for (const post of posts) {
      for (const image of post.images) {
        console.log(image);
      }
    }
    for (const post of posts) {
	console.log(post.comments.length);
        for (const comment of post.comments) {
	
            this.replyFormControls[this.replyFormControlName(comment.id)] = new FormControl('');
        }
    }
  });

  }
    
  handleGroupClick(group) {
  this.getGroupPosts(group.id);
  this.group=group;
  this.chosedGroup=true;
}

  
  getGroupPosts(id:number){
	console.log("pozv");
    this.groupService.getGroupPosts(id).subscribe((posts) => {
    this.postList= posts    
     this.cdr.detectChanges();
    })
  }
 	
	toggleCommentDropdown(commentId: number) {
	  for (const id in this.commentDropdownStatus) {
	    if (id !== commentId.toString()) {
	      this.commentDropdownStatus[id].open = false;
	    }
	  }
	  if (!this.commentDropdownStatus[commentId]) {
	    this.commentDropdownStatus[commentId] = { open: true };
	  } else {
	    this.commentDropdownStatus[commentId].open = !this.commentDropdownStatus[commentId].open;
	  }
	}

	
	closeCommentDropdown(commentId: number) {
	  this.commentDropdownStatus[commentId].open = false;
	}

	sortComments(tip: string,post:any) {
		const body = {
	    sort:tip,
	    post:post.id
	  };
		console.log("sortiranje "+tip);
	    this.comService.sort(body)
	      .subscribe(updatedComments => {
	          const postIndex = this.posts.findIndex(p => p.id === post.id);
                        if (postIndex !== -1) {
					        this.posts[postIndex].comments = updatedComments;
                            this.cdr.detectChanges();
                        }
                    });
	  }
	  
	  praviGrupu() {
		
    this.createGroup=true;
  }
  	  cancel() {
	    for ( const g of this.groups){
	console.log(g);
}
    this.createGroup=false;
  }
  
  
      napraviGrupu() {
		 if (!this.form6.value.name){
			 alert('Ime grupe je obavezno polje');
		}else{console.log(this.form6.value);
   // this.submitted = true;
   // console.warn('Your order has been submitted', this.forma.value);
    this.groupService.create(this.form6.value).subscribe(
	res=>{
	this.groupService.getAllForUser2().subscribe((groupsUpdated) => {
    this.groups= groupsUpdated;
    this.cdr.detectChanges();
    })
	}	
	);
    this.createGroup=false;
	this.form6.reset();}
  }
  
   deleteGroup(groupId: number) {
	
	const isConfirmed = confirm("Da li ste sigurni?");
    if (isConfirmed) {
        this.groupService.delete(groupId).subscribe(res => {
		this.groupService.getAllForUser2().subscribe((groupsUpdated) => {
		console.log(groupsUpdated);
	    this.groups= groupsUpdated;
	    if(this.group){
		if(this.group.id==groupId){
			this.chosedGroup=false;
			this.group=null;
	}
	}
	    this.cdr.detectChanges();
    })
	
	});
    
    }
  }
	  
	sortPosts(tip: string) {
	const body = {
    sort:tip
    };
	console.log("sortiranje "+tip);
    this.comService.sort2(body)
      .subscribe(res => {
          this.posts=res;
          this.cdr.detectChanges();
        }, err => {
          
        });

  }
  
  
   kreirajReakciju(tipReakcije: string, postId: number, commentId: number) {
  const novaReakcija = {
    type: tipReakcije,
    post: postId,
    comment: commentId
  };
  this.postService.react(novaReakcija)
    .subscribe(res => {
      this.postService.getOnePost(postId).subscribe(updatedPost => {
		   this.posts.forEach((post, index) => {
			console.log(index);
		        if (post.id === updatedPost.id) {
			console.log(updatedPost);
		          this.posts[index].likes = updatedPost.likes;
		          this.posts[index].dislikes = updatedPost.dislikes;
		          this.posts[index].hearts = updatedPost.hearts;
		          this.cdr.detectChanges();
		          console.log(this.posts[index]);
		          console.log(updatedPost);
		          console.log(index);
		        }
		      });
		    });
    }, err => {
    }); 
  }
      kreirajReakciju2(tipReakcije: string, postId: number, commentId: number) {

	  const novaReakcija = {
	    type: tipReakcije,
	    post: postId,
	    comment: commentId
	  };
	 	this.postService.react(novaReakcija)
	    .subscribe(res => {
	      this.comService.getOneCom(commentId).subscribe(updatedComment => {
	        console.log(updatedComment);
	       this.posts.forEach(post => {
	        post.comments.forEach((comment, index) => {
	          if (comment.id === updatedComment.id) {
	            post.comments[index] = updatedComment;
	            this.cdr.detectChanges();
	          }
	        });
	      });
	    });
	    }, err => {
	   
	    });
	  }
	  
	  kreirajReakciju3(tipReakcije: string, postId: number, commentId: number) {

	  const novaReakcija = {
	    type: tipReakcije,
	    post: postId,
	    comment: commentId
	  };
	 	this.postService.react(novaReakcija)
	    .subscribe(res => {
	      this.comService.getOneCom(commentId).subscribe(updatedReply => {
	        console.log(updatedReply);
			   this.posts.forEach(post => {
		      post.comments.forEach(comment => {
			comment.repliesComment.forEach((reply, index) => {
		        if (reply.id === updatedReply.id) {

		              comment.repliesComment[index] = updatedReply;

		          this.cdr.detectChanges();
		        }
		      });
		      });
		    });
		  });
	    }, err => {
	   
	    });
	  }

   editComment(post:any,comment: any) {
		this.selectedPostForEditing = post;
        this.editingComment=true;
        this.editCommentForm.patchValue({
            text: comment.text,
            id:comment.id
        });
    }
    
    editGroup(groupId, groupName, groupDesc) {
    this.editing = true;
    this.route.params
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe((params: DisplayMessage) => {
        this.notification = params;
      });
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
    this.form = this.formBuilder.group({
      id: groupId,
      name: ['', Validators.compose([Validators.required, Validators.minLength(3), Validators.maxLength(64)])],
      description: ['', Validators.compose([Validators.required, Validators.minLength(3), Validators.maxLength(64)])],
    });
    this.form.get("name").setValue(groupName)
    this.form.get('description').setValue(groupDesc)
  }

	onSubmit() {
	  const editedGroup = this.form.value;
	  this.editing = false;
	
	  this.groupService.editGroup(editedGroup).subscribe((result) => {
	   
	    const editedGroupIndex = this.groups.findIndex(group => group.id === editedGroup.id);
	
	    if (editedGroupIndex !== -1) { 
	      this.groups[editedGroupIndex] = result;
	      this.group=result;
	      this.cdr.detectChanges();
	    }
	  });
	  
	}

    
   editComment2(post:any,comment:any,reply: any) {
		this.selectedPostForEditing2 = post;
		this.selectedComForEditing = comment;
        this.editingComment2=true;
        this.editCommentForm2.patchValue({
            text: reply.text,
            id:reply.id
        });
    }
     editComment3(post:any,comment: any) {
		console.log("poz");
		this.drugaKomp=true;
		this.selectedPostForEditing = post;
        this.editingComment=true;
        this.komKojiMenjamo=comment;
        this.editCommentForm.patchValue({
            text: comment.text,
            id:comment.id
        });
    }


	 saveEditedComment() {		
        if (this.editCommentForm.valid && this.editingComment) {
            const editedText = this.editCommentForm.get('text').value;

            this.comService.editCom(this.editCommentForm.value)
		        .subscribe(res => {
				console.log(res);
				const n = this.selectedPostForEditing.id;
				console.log(n);
		    	  this.comService.getPostComments(n).subscribe(updatedComments => {
                        const postIndex = this.posts.findIndex(post => post.id === this.selectedPostForEditing.id);
                        console.log(this.selectedPostForEditing.id );
                        if (postIndex !== -1) {
							console.log(updatedComments);
					        this.posts[postIndex].comments = updatedComments;
					        console.log(this.posts[postIndex].comments);
                            this.cdr.detectChanges();
                            this.selectedPostForEditing = null;
                        }
                        if(this.drugaKomp){
							//this.authService.updateComm(this.komKojiMenjamo);
				}
                    });
		        }, err => {
		        
		        });
			
            this.editCommentForm.reset();
            this.editingComment = false;
        }
    }
    
    	 saveEditedComment2() {		
        if (this.editCommentForm2.valid && this.editingComment2) {
            const editedText = this.editCommentForm2.get('text').value;

            this.comService.editCom(this.editCommentForm2.value)
		        .subscribe(res => {
			const n = this.selectedComForEditing.id;
			this.comService.getCommentReplies(n).subscribe(updatedReplies => {
		    for (const post of this.posts) {
		      const commentToUpdate = post.comments.find(c => c.id === n);
		      if (commentToUpdate) {
		        commentToUpdate.repliesComment = updatedReplies;
		        this.selectedPostForEditing2 = null;
				this.selectedComForEditing = null;
		        break;
		     	 }
		    	}
		    	this.cdr.detectChanges(); 
		 		 });
		        }, err => {
		        
		        });
			
            this.editCommentForm2.reset();
            this.editingComment2 = false;
        }
    }
    
    cancelEdit() {
	this.editingComment=false;
    this.selectedPostForEditing = null; 
    this.editCommentForm.reset(); 
}

    cancelEdit2() {
	this.editingComment2=false;
    this.selectedPostForEditing2 = null; 
    this.editCommentForm2.reset(); 
}


    
    update(n:any){
	  this.comService.getPostComments(n).subscribe(updatedComments => {
                        const postIndex = this.posts.findIndex(post => post.id === n);
                        if (postIndex !== -1) {
							console.log(postIndex+" "+n);
							console.log(updatedComments);
					        this.posts[postIndex].comments = updatedComments;
					        console.log(this.posts[postIndex].comments);
                            this.cdr.detectChanges();
                        }
                    });
}


    
    update2(n:any){
			console.log(n);
			this.comService.getCommentReplies(n).subscribe(updatedReplies => {
		    for (const post of this.posts) {
		      const commentToUpdate = post.comments.find(c => c.id === n);
		      console.log(commentToUpdate);
		      if (commentToUpdate) {
				console.log(commentToUpdate);
				console.log(updatedReplies);
		        commentToUpdate.repliesComment = updatedReplies;
		        console.log(commentToUpdate.repliesComment);
		        break;
		     	 }
		    	}
		    	this.cdr.detectChanges(); 
		 		 });

}



toggleComments(postId: number) {
    for (const post of this.posts) {
        if (post.id === postId) {
            post.commentsForPost = !post.commentsForPost;
        }/* else {
            post.commentsForPost = false;
        }*/
    }
}

toggleReplies(comment: any) {
    comment.showReplies = !comment.showReplies;
}


formatTimestamp(timestamp: number[]): string {
    const date = new Date(timestamp[0], timestamp[1] - 1, timestamp[2], timestamp[3], timestamp[4]);
    const formattedDate = `${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()} ${date.getHours()}:${date.getMinutes()}`;
    return formattedDate;
}


 
getPosts() {
  this.postService.getAllRndm().subscribe((posts) => {
    this.posts = posts;
  });
}

  editPost(postId, postContent,postImages) {
    this.editing = true;
    this.route.params
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe((params: DisplayMessage) => {
        this.notification = params;
      });
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
    this.form = this.formBuilder.group({
      id: postId,
      content: ['', Validators.compose([Validators.required])],
      images:['']
    });
    this.form.get("id").setValue(postId);
    this.form.get("content").setValue(postContent)
    
    this.form.get("images").setValue(this.getImagesPathString(postImages));
  }


getImagesPathString(postImages:any): string {
  return this.getImagesSize1(postImages).join(','); // Spajamo putanje slika separatorom ', '
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


}
