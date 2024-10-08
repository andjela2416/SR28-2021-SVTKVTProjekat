import { Component, Input } from '@angular/core';
import { FormBuilder, FormGroup, Validators,FormControl } from '@angular/forms';
import { AuthService } from 'src/app/service/auth.service';
import { ComService } from 'src/app/service/com.service';
import { PostService } from 'src/app/service/post.service';
import { UserService } from 'src/app/service/user.service';
import { ChangeDetectionStrategy } from '@angular/core';
import { ChangeDetectorRef } from '@angular/core';
import { AllPostListComponent } from '../allPost-list/allPost-list.component';
import { HttpClient, HttpEventType } from '@angular/common/http';

@Component({
  selector: 'app-comment-list',
  templateUrl: './comment-list.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  styleUrls: ['./comment-list.component.css']
})
export class CommentListComponent {
  @Input() comments: any[]; // Ulazni podaci za komentare
  @Input() parentPost: any;
  @Input() currentUser: any;
   activeDropdownId: { id: number, type: string } | null = null;
  isReportMenuOpen: boolean = false;
  isReportMenuOpen2: boolean = false;
  replyFormControls: { [commentId: number]: FormControl } = {};
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
retrievedImage: any;
base64Data: any;
  retrieveResonse: any;
  message: string;
  imageName: any;
  userImageCache: Map<string, string> = new Map<string, string>();

  constructor(private authService: AuthService, private comService: ComService,
  private cdr: ChangeDetectorRef,private formBuilder: FormBuilder,
  private allPostListComponent: AllPostListComponent,private userService:UserService,
  private postService:PostService,private httpClient: HttpClient
  ) {
	this.editCommentForm3 = this.formBuilder.group({
            text: ['', Validators.required],
            id:['', Validators.required]
        });
        this.editCommentForm4 = this.formBuilder.group({
            text: ['', Validators.required],
            id:['', Validators.required]
        });
  }
    
    
	replyFormControl3 = new FormControl('');
	replyFormControl4 = new FormControl('');

    editingComment3= false;
    editCommentForm3: FormGroup; 
    selectedPostForEditing3: any;
    editingComment4= false;
    editCommentForm4: FormGroup; 
    selectedComForEditing2: any;
   	postDropdownStatus: { [postId: number]: { open: boolean, type: string } } = {};
	commentDropdownStatus: { [commentId: number]: { open: boolean, type: string } } = {};
	ngOnInit() {
	
	    for (const comment of this.comments) {
	      this.replyFormControls[this.replyFormControlName(comment.id)] = new FormControl('');
	      if(comment.userId.profilePhotoUpload){		
				console.log("ppozv");
				this.retrievedImage2(comment.userId);
			}
	    	for(const p of comment.repliesComment){
			if(p.userId.profilePhotoUpload){		
				console.log("ppozv");
				this.retrievedImage2(p.userId);
			}
			
		}
	    }
		this.selectedPostForEditing3=this.parentPost;
	
	}
	
    kreirajReakciju(tipReakcije: string, postId: number, commentId: number) {

	  const novaReakcija = {
	    type: tipReakcije,
	    post: postId,
	    comment: commentId
	  };
	 	this.postService.react(novaReakcija)
	    .subscribe(res => {
	      this.comService.getOneCom(commentId).subscribe(updatedReply => {
 			const commIndex = this.comments.findIndex(comm => comm.id === commentId);
	                        if (commIndex !== -1) {
						        this.comments[commIndex] = updatedReply;
	                            this.cdr.detectChanges();
	                        }
	                    });
	    }, err => {
	   
	    });
	  }
	  
	    retrievedImage1(user: any) {
		  //Make a call to Spring Boot to get the Image Bytes.
		  this.httpClient.get('http://localhost:8080/api/users/get/' + user.profilePhotoUpload.imagePath+"/"+user.profilePhotoUpload.id)
		    .subscribe(
		      res => {
		        this.retrieveResonse = res;
		        this.base64Data = this.retrieveResonse.picByte;
		        return 'data:image/jpeg;base64,' + this.base64Data;
		      }
		    );
		}
	  
	  kreirajReakciju2(tipReakcije: string, postId: number, commentId: number) {

	  const novaReakcija = {
	    type: tipReakcije,
	    post: postId,
	    comment: commentId
	  };
	 	this.postService.react(novaReakcija)
	    .subscribe(res => {
	      this.comService.getOneCom(commentId).subscribe(updatedReply => { 			    
		      this.comments.forEach(comment => {
		      const replyIndexToUpdate = comment.repliesComment.findIndex(reply => reply.id === commentId);
	          if (replyIndexToUpdate !== -1) {
	            comment.repliesComment[replyIndexToUpdate] = updatedReply;
	            this.cdr.detectChanges();
	          }
	        });
	      });
	    }, err => {
	   
	    });
	  }
	  
	  	  toggleReportMenu(): void {
	  this.isReportMenuOpen = !this.isReportMenuOpen;
	}
	
	toggleReportMenu2(): void {
	  this.isReportMenuOpen2 = !this.isReportMenuOpen2;
	}
	retrievedImage2(user: any) {
  if (this.userImageCache.has(user.id)) {
	console.log(user.profilePhotoUpload);
    return this.userImageCache.get(user.id);
  } else {
	console.log(user.profilePhotoUpload);
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
	/*  
	toggleCommentDropdown2(commentId: number) {
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
	*/
	
	closeCommentDropdown(commentId: number) {
	  this.commentDropdownStatus[commentId].open = false;
	}
	
	editComment(comment:any){
		this.allPostListComponent.editComment3(this.parentPost,comment);
		
	}
	update(comment:any){
				this.comService.getOneCom(comment).subscribe(updatedComment => { 
					const commIndex = this.comments.findIndex(comm => comm.id === comment.id);
			      	if (commIndex!==-1) {
				  	console.log(updatedComment);
			        this.comments[commIndex] = updatedComment;
	                this.cdr.detectChanges();
			      }
				
		  });
	}
    
    deleteComment(comment: any) {
	  const confirmDelete = confirm('Da li ste sigurni da zelite da obrisete ovaj komentar? Id:'+comment.id);
	  if (confirmDelete) {
	    this.comService.delete(comment.id)
	      .subscribe(() => {
	        const index = this.comments.findIndex(c => c.id === comment.id);
	        if (index !== -1) {
			  console.log(index+" "+comment.id);
	          this.comments.splice(index, 1);
	          this.cdr.detectChanges();
	        }
	      }, err => {
	        console.log(err);
	      });
	  }
	}
	
	deleteComment2(comment: any) {
	  const confirmDelete = confirm('Da li ste sigurni da zelite da obrisete ovaj komentar? Id: ' + comment.id);
	  if (confirmDelete) {
	    this.comService.delete(comment.id)
	      .subscribe(() => {
	        for (const comm of this.comments) {
	          const index = comm.repliesComment.findIndex(reply => reply.id === comment.id);
	          if (index !== -1) {
				console.log(index+" "+comment.id);
	            comm.repliesComment.splice(index, 1);
	            this.cdr.detectChanges();
	            break; 
	          }
	        }
	      }, err => {
	        console.log(err);
	      });
	  }
	}




	addReply3(comment: any) {
	   const commentContent = this.replyFormControl3.value;
	
		if (this.authService.tokenIsPresent()) {
			 if (!this.replyFormControl3.value){
				 alert('Tekst je obavezno polje ');
			}else{
	
	  	const body = {
	        text: commentContent,
	        post:this.parentPost.id
	      };
	     this.comService.replyToCom(comment,body)
	        .subscribe(res => {
			 this.comService.getCommentReplies(comment).subscribe(updatedComments => {
	                        const commIndex = this.comments.findIndex(comm => comm.id === comment);
	                        if (commIndex !== -1) {
						        this.comments[commIndex].repliesComment = updatedComments;
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
	  	 this.replyFormControl3.reset();
	}
	addReply4(comment: any) {
	     const commentContent = this.replyFormControl4.value;
	
		if (this.authService.tokenIsPresent()) {
			 if (!this.replyFormControl4.value){
				 alert('Tekst je obavezno polje ');
			}else{
	
	  	const body = {
	        text: commentContent,
	        post:this.parentPost.id
	      };
	     this.comService.replyToCom(comment,body)
	        .subscribe(res => {
			 this.comService.getCommentReplies(comment).subscribe(updatedReplies => {
	                            for (const com of this.comments) {
			      const commentToUpdate = com.repliesComment.find(c => c.id === comment);
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
	  	 this.replyFormControl4.reset();
	}
	
	formatTimestamp(timestamp: number[]): string {
	    const date = new Date(timestamp[0], timestamp[1] - 1, timestamp[2], timestamp[3], timestamp[4]);
	    const formattedDate = `${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()} ${date.getHours()}:${date.getMinutes()}`;
	    return formattedDate;
	}

	selectReportReason(comment:any,reason: string): void {
	 // this.selectedReportReason = reason;
	  this.isReportMenuOpen = false; 
	  this.toggleCommentDropdown({ id:comment.id, type: 'comment' });
	  this.reportuj(comment,reason); 
	}
	
	selectReportReason2(comment:any,reason: string): void {
	  //this.selectedReportReason2 = reason;
	  this.isReportMenuOpen2 = false; 
	  this.toggleCommentDropdown({ id: comment.id, type: 'comment' });
	  this.reportuj(comment,reason); 
	}
	
	reportuj(comment,selectedReason: string) {
  if (!selectedReason) {
    alert("Morate selektovati razlog pre nego sto posaljete prijavu.");
    return; 
  }

	const novaReakcija = {
    id: comment.id,
    reason: selectedReason,
  };

  this.userService.report3(novaReakcija).subscribe(res => {
	alert("Uspesno ste prijavili komentar.");
  });	

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

  replyFormControlName(commentId: number): string {
    return `reply_${commentId}`;
  }


  toggleReplies(comment: any) {
    comment.showReplies = !comment.showReplies;
  }
}
