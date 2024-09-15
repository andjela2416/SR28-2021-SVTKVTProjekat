import { Component, Input, OnInit,OnChanges, SimpleChanges, ChangeDetectionStrategy ,NgZone} from '@angular/core';
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
import { HttpClient, HttpEventType } from '@angular/common/http';
import { forkJoin } from 'rxjs';


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
  providers: [DatePipe],
  selector: 'app-allPost-list',
  templateUrl: './allPost-list.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  styleUrls: ['./allPost-list.component.css']
})
export class AllPostListComponent implements OnInit,OnChanges {
  @Input() posts: any[];
  @Input() grupa: any;
  @Input() profile: any;
  @Input() your: any; 
  editing = false;
  activeDropdownId: { id: number, type: string } | null = null;
  editingPostDiv = false;
  form: FormGroup
   editPostForm: FormGroup
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
  forma2 = new FormGroup({
    post: new FormControl('', Validators.required),
    pathSlike: new FormControl(''),
    group: new FormControl('')
  });
  submitted2=false;
  submitted3=false;
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
	currentUser:any;
	showDropdown = false;
	createGroup=false;
	form6 = new FormGroup({
    name: new FormControl('', Validators.required),
    description: new FormControl('')
  });
	postDropdownStatus: { [postId: number]: { open: boolean, type: string } } = {};
	commentDropdownStatus: { [commentId: number]: { open: boolean, type: string } } = {};
	isReportMenuOpen: boolean = false;
	isReportMenuOpen2: boolean = false;
selectedReportReason: string = "";
selectedReportReason2: string = "";
reportReasons: string[] = [
  "BREAKES_RULES",
  "HARASSMENT",
  "HATE",
  "SHARING_PERSONAL_INFORMATION",
  "IMPERSONATION",
  "COPYRIGHT_VIOLATION",
  "TRADEMARK_VIOLATION",
  "SPAM",
  "SELF_HARM_OR_SUICIDE",
  "OTHER"
];
submittedEdit = false;
retrievedImageNe: any;
base64Data: any;
  retrieveResonse: any;
  message: string;
  imageName: any;
userImageCache: Map<string, string> = new Map<string, string>();
imagePathss: string[] = [];
imagePathss2: string[] = [];
imagePathss3: string[] = [];
selectedFiles: File[] = [];
selectedFiles2: File[] = [];
selectedFiles3: File[] = [];
private postImageCache = new Map<number, string[]>();

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
    private httpClient: HttpClient
     
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
	
	console.log(this.posts);
	
	if (this.authService.tokenIsPresent()) {
    this.userService.getMyInfo().subscribe(user => {
		this.currentUser=user.id;
    });
  }
	if(this.posts){
	for(const p of this.posts){
			if(p.postedBy.profilePhotoUpload){		
				console.log("ppozv");
				this.retrievedImage2(p.postedBy);
			}
			for(const c of p.comments){
				if(c.userId.profilePhotoUpload){		
				console.log("ppozv");
				this.retrievedImage2(c.userId);
			}
				for(const p of c.repliesComment){
					if(p.userId.profilePhotoUpload){		
					console.log("ppozv");
					this.retrievedImage2(p.userId);
				}}
			}
			
		}
	}

   this.postService.getAllRndm().subscribe((posts) => {
    for (const post of posts) {
      for (const image of post.images) {
        console.log(image);
      }
    }
    console.log(this.posts);
    for (const post of posts) {
	console.log(post.comments.length);
        for (const comment of post.comments) {
	
            this.replyFormControls[this.replyFormControlName(comment.id)] = new FormControl('');
        }
    }
  });

  }
  
    ngOnChanges(changes: SimpleChanges) {
    // ngOnChanges() se poziva kada se input podaci promene
    // Ovde možete pristupiti input podacima i raditi sa njima
    if (changes.posts) {
      console.log('Input "posts" promenjen:', this.posts);
      if(this.posts){
	    for (const p of this.posts) {
	      if (p.images && p.images.length > 0) {
			const imagesPaths=[];
			for(const i of p.images){
				if (i.picByte){
					this.loadPostImages(p.images,p);
					break;
				}
				imagesPaths.push(i.imagePath);
			}
			this.postImageCache.set(p.id,imagesPaths);
	        	this.cdr.detectChanges();
	      }
	    }
    }
    }
  }
  
  slike(posts){
	console.log(posts);
		   for (const p of posts) {
	      if (p.images && p.images.length > 0) {
			console.log(p.images);
			const imagesPaths=[];
			for(const i of p.images){
				console.log(i);
				if (i.picByte){
					console.log(i);
					this.loadPostImages(p.images,p);
					this.cdr.detectChanges();
					break;
				}
				imagesPaths.push(i.imagePath);
			}
			console.log(imagesPaths);
			this.postImageCache.set(p.id,imagesPaths);
	        	this.cdr.detectChanges();
	      }
	    }
	     console.log(this.postImageCache);
}
 	
	toggleCommentDropdown(data: { id: number, type: string }) {
  console.log(this.activeDropdownId);
  const commentId = data.id; 
  const commentType = data.type; 

  if (this.activeDropdownId !== null && (this.activeDropdownId.id !== commentId || this.activeDropdownId.type !== commentType)) {
    console.log("u");
    if (this.activeDropdownId.type === "comment") {
      this.commentDropdownStatus[this.activeDropdownId.id].open = false;
      console.log("Zatvori prethodni padajući meni za komentar");
    } else if (this.activeDropdownId.type === "post") {
      this.postDropdownStatus[this.activeDropdownId.id].open = false;
      console.log("Zatvori prethodni padajući meni za post");
    }
  }
  
  if (!this.commentDropdownStatus[commentId]) {
    this.commentDropdownStatus[commentId] = { open: true, type: commentType  };
    console.log("Otvori novi padajući meni za komentar");
  } else {
    this.commentDropdownStatus[commentId].open = !this.commentDropdownStatus[commentId].open;
    console.log("Preklopi stanje padajućeg menija za komentar");
  }
  
  this.activeDropdownId = { id: commentId, type: commentType };
  this.cdr.detectChanges();
}


togglePostDropdown(data: { id: number, type: string }) {
  console.log(this.activeDropdownId);
  const postId = data.id; 
  const postType = data.type; 

  if (this.activeDropdownId !== null && (this.activeDropdownId.id !== postId || this.activeDropdownId.type !== postType)){
    if (this.activeDropdownId.type === "comment") {
      this.commentDropdownStatus[this.activeDropdownId.id].open = false;
      console.log("Zatvori prethodni padajući meni za komentar");
    } else if (this.activeDropdownId.type === "post") {
      this.postDropdownStatus[this.activeDropdownId.id].open = false;
      console.log("Zatvori prethodni padajući meni za post");
    }
  }
  
  if (!this.postDropdownStatus[postId]) {
    this.postDropdownStatus[postId] = { open: true, type: postType  };
    console.log("Otvori novi padajući meni za post");
  } else {
    this.postDropdownStatus[postId].open = !this.postDropdownStatus[postId].open;
    console.log("Preklopi stanje padajućeg menija za post");
  }
  
  this.activeDropdownId = { id: postId, type: postType };
  this.cdr.detectChanges();
}

	
	closeCommentDropdown(commentId: number) {
	  this.commentDropdownStatus[commentId].open = false;
	}
	
	closePostDropdown(postId: number) {
	  this.postDropdownStatus[postId].open = false;
	  this.imagePathss3=[];
	  this.cdr.detectChanges();
	}
	/*  getImage2() {
	console.log(this.user.profilePhotoUpload);
    this.httpClient.get('http://localhost:8080/api/users/get/' + this.user.profilePhotoUpload)
      .subscribe(
        res => {
          this.retrieveResonse = res;
          this.base64Data = this.retrieveResonse.picByte;
          this.retrievedImage2 = 'data:image/jpeg;base64,' + this.base64Data;
        }
      );
  }*/
  retrievedImage(user: any) {
	console.log(user.profilePhotoUpload);
  //Make a call to Spring Boot to get the Image Bytes.
  this.httpClient.get('http://localhost:8080/api/users/get/' + user.profilePhotoUpload)
    .subscribe(
      res => {
        this.retrieveResonse = res;
        this.base64Data = this.retrieveResonse.picByte;
        console.log('data:image/jpeg;base64,' + this.base64Data);
        return 'data:image/jpeg;base64,' + this.base64Data;
      }
    );
}

removeImage(index: number) {
  this.imagePathss.splice(index, 1); 
  this.selectedFiles.splice(index, 1);
   this.cdr.detectChanges();       
}

removeImage2(index: number) {
  this.imagePathss2.splice(index, 1); 
  this.selectedFiles2.splice(index, 1);
   this.cdr.detectChanges();       
}

removeImage3(index: number) {
  this.imagePathss3.splice(index, 1); 
  this.selectedFiles3.splice(index, 1);
   this.cdr.detectChanges();       
}

handleFileInput(event: any) {
  const files = event.target.files;
console.log("handlefileinput1");

  for (let i = 0; i < files.length; i++) {
    const file = files[i];
     this.selectedFiles.push(file);
    const reader = new FileReader();

    reader.onload = (e: any) => {
      const imagePath = e.target.result ;
      this.imagePathss.push(imagePath);  
      this.cdr.detectChanges();       
    };
	
    reader.readAsDataURL(file);
  }
  console.log(this.imagePathss);
}

handleFileInput2(event: any) {
  const files = event.target.files;
  

  for (let i = 0; i < files.length; i++) {
    const file = files[i];
     this.selectedFiles2.push(file);
    const reader = new FileReader();

    reader.onload = (e: any) => {
      const imagePath = e.target.result ;
      this.imagePathss2.push(imagePath);  
      this.cdr.detectChanges();       
    };
	
    reader.readAsDataURL(file);
  }
  console.log(this.imagePathss2);
}
handleFileInput3(event: any) {
	console.log("handlefileinput3");
  const files = event.target.files;


  for (let i = 0; i < files.length; i++) {
    const file = files[i];
     this.selectedFiles3.push(file);
    const reader = new FileReader();

    reader.onload = (e: any) => {
      const imagePath = e.target.result ;
      this.imagePathss3.push(imagePath);  
      this.cdr.detectChanges();       
    };
	
    reader.readAsDataURL(file);
  }
  console.log(this.imagePathss3);
}

retrievedImage2(user: any) {
  if (this.userImageCache.has(user.id)) {
    return this.userImageCache.get(user.id);
  } else {
    this.httpClient.get('http://localhost:8080/api/users/get/' + user.profilePhotoUpload.imagePath+"/"+user.profilePhotoUpload.id)
      .subscribe(
        res => {
          this.retrieveResonse = res;
          this.base64Data = this.retrieveResonse.picByte;
          const imageUrl = 'data:image/jpeg;base64,' + this.base64Data;
          this.userImageCache.set(user.id, imageUrl);
          this.cdr.detectChanges();
          return imageUrl;
        }
      );
  }
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
    this.createGroup=false;
  }
  
	  toggleReportMenu(): void {
	  this.isReportMenuOpen = !this.isReportMenuOpen;
	}
	
	toggleReportMenu2(): void {
	  this.isReportMenuOpen2 = !this.isReportMenuOpen2;
	}
	
	
	selectReportReason(post:any,reason: string): void {
	  this.selectedReportReason = reason;
	  this.isReportMenuOpen = false; 
	  this.togglePostDropdown({ id: post.id, type: 'post' });
	  this.reportuj(reason,post,null); 
	}
	
	selectReportReason2(comment:any,reason: string): void {
	  this.selectedReportReason2 = reason;
	  this.isReportMenuOpen2 = false; 
	  this.toggleCommentDropdown({ id: comment.id, type: 'comment' });
	  this.reportuj(reason,null,comment); 
	}

reportuj(selectedReason: string,post?,comment?) {
  if (!selectedReason) {
    alert("Morate selektovati razlog pre nego sto posaljete prijavu.");
    return; 
  }
  if(post!=null){
	const novaReakcija = {
    id: post.id,
    reason: selectedReason,
  };

  this.userService.report2(novaReakcija).subscribe(res => {
	alert("Uspesno ste prijavili post.");
  });	
}
  if(comment!=null){
	const novaReakcija = {
    id: comment.id,
    reason: selectedReason,
  };

  this.userService.report3(novaReakcija).subscribe(res => {
	alert("Uspesno ste prijavili komentar.");
  });	
}
}
 
  
      napraviGrupu() {
		 if (!this.form6.value.name){
			 alert('Ime grupe je obavezno polje');
		}else{console.log(this.form6.value);
   // this.submitted = true;
   // console.warn('Your order has been submitted', this.forma.value);
    this.groupService.create(this.form6.value)
    this.createGroup=false;
	this.form6.reset();}
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
    
       editPost(post:any) {
		this.selectedPostForEditing = post;
        this.editingPostDiv = true;
        this.editPostForm = this.formBuilder.group({
	      id: post.id,
	      content: ['', Validators.compose([Validators.required])]
	    });
	    this.editPostForm.get("id").setValue(post.id);
	    this.editPostForm.get("content").setValue(post.content)  
	    this.cdr.detectChanges(); 
	    this.send();
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

    cancelEdit3() {
	this.editingPostDiv=false;
    this.selectedPostForEditing = null; 
    this.editPostForm.reset(); 
}

	confirmDeleteComment(post:any,comment:any) {
    const isConfirmed = confirm("Jeste li sigurni da želite obrisati ovaj komentar? Id:"+comment.id);
    if (isConfirmed) {
        this.deleteComment(post,comment);
    }
}
	confirmDeletePost(post:any) {
    const isConfirmed = confirm("Jeste li sigurni da želite obrisati ovaj post?");
    if (isConfirmed) {
        this.deletePost(post);
    }
}
	deletePost(post:any) {
		      this.postService.delete(post.id)
		        .subscribe(res => {
				console.log(post.id+" "+res.deleted+" post"+post);
				this.updatePost(post.id);
		        }, err => {
		        
		        });
    }

	deleteComment(post:any,comment: any) {
		      this.comService.delete(comment.id)
		        .subscribe(res => {
				console.log(post.id+" "+res.deleted+" post"+post);
				this.update(post.id);
		        }, err => {
		        
		        });
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
    updatePost(n:any){
	  this.postService.getAllFromUser().subscribe((posts) => {
      this.posts = posts; 
        this.cdr.detectChanges();
    });
}
	confirmDeleteComment2(post:any,comment:any,reply:any) {
    const isConfirmed = confirm("Jeste li sigurni da želite obrisati ovaj komentar? Id:"+reply.id);
    if (isConfirmed) {
        this.deleteComment2(post,comment,reply);
    }
}


	deleteComment2(post:any,comment: any,reply:any) {
		      this.comService.delete(reply.id)
		        .subscribe(res => {
				console.log(post.id+" "+res.deleted+" post"+post);
				this.update2(comment.id);
		        }, err => {
		        
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

	  addReply(comment: any,post:any) {
	   const commentContent = this.replyFormControl.value;
	
		if (this.authService.tokenIsPresent()) {
			 if (!this.replyFormControl.value){
				 alert('Tekst je obavezno polje ');
			}else{
	
	  	const body = {
	        text: commentContent,
	        post:post
	      };
	     this.comService.replyToCom(comment,body)
            .subscribe(res => {
		 			this.comService.getCommentReplies(comment).subscribe(updatedReplies => {
		    for (const post of this.posts) {
		      const commentToUpdate = post.comments.find(c => c.id === comment);
		      if (commentToUpdate) {
		        commentToUpdate.repliesComment = updatedReplies;
		        break;
		      }
		    }
		    this.cdr.detectChanges(); 
		  });
        }, err => {
          console.log(err);
        });
    }
	} else {
    alert('Morate se prvo ulogovati');
  	}
  	 this.replyFormControl.reset();
}
addReply2(comment: any,post:any) {
	
   const commentContent = this.replyFormControl2.value;

	if (this.authService.tokenIsPresent()) {
		 if (!this.replyFormControl2.value){
			 alert('Tekst je obavezno polje ');
		}else{

  	const body = {
        text: commentContent,
        post:post
      };
     this.comService.replyToCom(comment,body)
        .subscribe(res => {
				this.comService.getCommentReplies(comment).subscribe(updatedReplies => {
		    for (const post of this.posts) {
				for (const com of post.comments){
						 const commentToUpdate = com.repliesComment.find(c => c.id === comment);
			      if (commentToUpdate) {
				  	console.log(updatedReplies);
			        commentToUpdate.repliesComment = updatedReplies;
			        console.log(commentToUpdate);
			        break;
			      }
				} 
		    }
		    this.cdr.detectChanges(); 
		  });
        }, err => {
          console.log(err);
        });
    }
	} else {
    alert('Morate se prvo ulogovati');
  	}
  	 this.replyFormControl2.reset();
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
addComment(postId: number) {
   const commentContent = this.commentFormControl.value;

	if (this.authService.tokenIsPresent()) {
		 if (!this.commentFormControl.value){
			 alert('Tekst je obavezno polje ');
		}else{

  	const body = {
        postId: postId,
        content: commentContent
      };
      console.warn(postId);
     this.comService.add(body)
        .subscribe(res => {
		   this.comService.getPostComments(postId).subscribe(updatedComments => {
                        const postIndex = this.posts.findIndex(post => post.id === postId);
                        if (postIndex !== -1) {
					        this.posts[postIndex].comments = updatedComments;
                            this.cdr.detectChanges();
                        }
                    });
        }, err => {
          console.log(err);
        });
    }
	} else {
    alert('Morate se prvo ulogovati');
  	}
  	 this.commentFormControl.reset();
}

send(){
	    const editFormElement = document.getElementById('edit-form');
	if (editFormElement) {
	    editFormElement.scrollIntoView({ behavior: 'smooth', block: 'start' });
	}
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
  onSubmitPostavi() {
	this.imagePathss = [];
	 this.cdr.detectChanges();
	if (this.authService.tokenIsPresent()) {
		 if (!this.forma.value.post){
			 alert('Tekst je obavezno polje objave');
		}else{console.log(this.forma.value.pathSlike);
    this.submitted2 = true;
    console.warn('Your order has been submitted', this.forma.value);
         this.postService.create(this.forma.value).subscribe(() => {
    this.postService.getAllRndm().subscribe((posts) => {
      this.posts = posts; 
      this.editing=false;
        this.cdr.detectChanges();
    });
  })
	this.forma.reset();}}
	 else {
    alert('Morate se prvo ulogovati');
    // Dodajte logiku koju želite da primenite ako korisnik nije ulogovan
  }

	
}


	loadPostImages(images,post: any) {
	/*  if ( this.postImageCache.has(post.id)) {
		console.log(this.postImageCache.get(post.id));
	    return this.postImageCache.get(post.id);
	  } else {*/
		console.log(post,images);
	    const imageUrls = images.map((image: any) => {
		console.log("3p");
			return 'http://localhost:8080/api/users/get/' + image.imagePath+"/"+image.id;

	    });
		console.log(imageUrls);
	    const observables = imageUrls.map(url => this.httpClient.get(url));
		console.log(observables);
	    forkJoin(observables).subscribe(
	      (responses: any[]) => {
	        const imageUrls = responses.map((res: any) => {
				if(res.picByte){
					 const base64Data = res.picByte;
	          	return 'data:image/jpeg;base64,' + base64Data;
				}else if (!res.picByte){
					return res.imagePath;
				}
	         
	        });
	
	        this.postImageCache.set(post.id, imageUrls);
	        this.cdr.detectChanges();
	      }
	    );
	 // }
	  console.log(this.postImageCache);
	}

	saveChanges() {
	  if (!this.forma.value.post) {
	    alert('Tekst je obavezno polje objave');
	  } else {
		console.log(this.imagePathss);
	    if (this.imagePathss && this.imagePathss.length > 0) {
	      console.log(this.imagePathss);
	      this.postService.create(this.forma.value, this.selectedFiles).subscribe((data) => {
	        this.postService.getAllRndm().subscribe((posts) => {
	          this.posts = posts;
	          this.cdr.detectChanges();
	          this.slike(posts);	   
	          this.cdr.detectChanges();       
	          console.log(this.posts);
	        });
	        console.log('Changes saved:', data);
	      });
	    } else {
	      this.postService.create(this.forma.value).subscribe((data) => {
	        this.postService.getAllRndm().subscribe((posts) => {
	          this.posts = posts;
	          this.cdr.detectChanges();
	          console.log('Changes saved:', data);
	        });
	      });
	    }
	    this.imagePathss = [];
	    this.selectedFiles=[];
	    this.forma.reset();
	    this.cdr.detectChanges();
	    alert("You have successfully uploaded a post")
	  }
	}

saveChanges2() {
	this.forma2.get('group').setValue(this.grupa);
	console.log(this.grupa);
	  if (!this.forma2.value.post) {
	    alert('Tekst je obavezno polje objave');
	  } else {
		console.log(this.imagePathss2);
	    if (this.imagePathss2 && this.imagePathss2.length > 0) {
	      console.log(this.imagePathss2);
	      this.postService.createInGroup(this.forma2.value, this.selectedFiles2).subscribe((data) => {
	        this.groupService.getGroupPosts(this.grupa.id).subscribe((posts) => {
			  console.log(posts);
		      this.posts = posts; 
	          this.cdr.detectChanges();
	          this.slike(posts);	   
	          this.cdr.detectChanges();       
	          console.log(this.posts);
	        });
	        console.log('Changes saved:', data);
	      });
	    } else {
	      this.postService.createInGroup(this.forma2.value).subscribe((data) => {
	        this.groupService.getGroupPosts(this.grupa.id).subscribe((posts) => {
	          this.posts = posts;
	          this.cdr.detectChanges();
	          console.log('Changes saved:', data);
	        });
	      });
	    }
	    this.imagePathss2 = [];
	    this.selectedFiles2=[];
	    this.forma2.reset();
	    this.cdr.detectChanges();
		alert("You have successfully uploaded a post")
	  }
	}
	saveChanges3() {
	  if (!this.editPostForm.value.content) {
	    alert('Tekst je obavezno polje objave');
	  } else {
		console.log(this.imagePathss3);
	    if (this.imagePathss3 && this.imagePathss3.length > 0) {
	      console.log(this.imagePathss3);
	      this.postService.edit(this.editPostForm.value, this.selectedFiles3).subscribe((data) => {
		    this.postService.getAllFromUser().subscribe((posts) => {
		      this.posts = posts; 
		      this.cdr.detectChanges();
		      this.slike(posts);
		      this.editingPostDiv=false;
		        this.cdr.detectChanges();
		    });
	        console.log('Changes saved:', data);
	      });
	    } else {
	      this.postService.edit(this.editPostForm.value).subscribe((data) => {
	        this.postService.getAllFromUser().subscribe((posts) => {
	          this.posts = posts;
	          this.cdr.detectChanges();
	          console.log('Changes saved:', data);
	        });
	      });
	    }
	    this.imagePathss3 = [];
	    this.selectedFiles3=[];
	    this.editPostForm.reset();
	    this.cdr.detectChanges();
	    console.log(this.imagePathss3)
		alert("You have successfully uploaded a post")
	  }
	}

   onSubmitEdit() {
   if (this.editPostForm.valid && this.editingPostDiv) {	  
	if (this.authService.tokenIsPresent()) {
		 if (!this.editPostForm.value.content){
			 alert('Tekst je obavezno polje objave');
		}else{
    this.submittedEdit = true;
    console.warn('Your order has been submitted', this.editPostForm.value);
    
     this.postService.edit(this.editPostForm.value).subscribe(() => {
    this.postService.getAllFromUser().subscribe((posts) => {
      this.posts = posts; 
      this.editingPostDiv=false;
        this.cdr.detectChanges();
    });
  })

	this.editPostForm.patchValue({
  	content: '',
	});;}
	
	}
	 else {
    alert('Morate se prvo ulogovati');
    // Dodajte logiku koju želite da primenite ako korisnik nije ulogovan
  }}
  }

  onSubmitPostavi2() {
	this.forma2.get('group').setValue(this.grupa);
	console.log("aa"+this.grupa)
	if (this.authService.tokenIsPresent()) {
		 if (!this.forma2.value.post){
			 alert('Tekst je obavezno polje objave');
		}else{console.log(this.forma2.value.pathSlike);
    this.submitted3 = true;
    console.log('Your order has been submitted', this.forma2.value);
         this.postService.createInGroup(this.forma2.value).subscribe(() => {
    this.groupService.getGroupPosts(this.grupa.id).subscribe((posts) => {
	console.log(posts);
		for (const post of posts)
		{
			console.log(post);
		}
      this.posts = posts; 
      this.editing=false;
        this.cdr.detectChanges();
    });
  })
	this.forma2.reset();}}
	 else {
    alert('Morate se prvo ulogovati');
    // Dodajte logiku koju želite da primenite ako korisnik nije ulogovan
  }

	
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
