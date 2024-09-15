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
  selector: 'app-one-group',
  templateUrl: './one-group.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  styleUrls: ['./one-group.component.css']
})
export class OneGroupComponent implements OnInit {
  @Input() group: any;
  @Input() isMember:boolean;
  @Input() isAdmin:boolean;
  @Input() isRequestSent:boolean;
  @Input() isUserBannedInGroup:boolean;
  //isRequestSent: boolean = false;
  editing = false;
  systemAdmin=false;
  selectedAdminToRemove: any;
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
	currentUser:any;
	showDropdown = false;
	createGroup=false;
	form6 = new FormGroup({
    name: new FormControl('', Validators.required),
    description: new FormControl('')
  });
	commentDropdownStatus: { [commentId: number]: { open: boolean } } = {};
	chosedGroup = false;
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
	
	if (this.authService.tokenIsPresent()) {
    this.userService.getMyInfo().subscribe(user => {
		this.currentUser=user.id;
		if(user.role=='ADMIN'){
			this.systemAdmin=true;
		}
    });
    
  }
  console.log(this.isAdmin,this.isMember);

  }
  
    suspend(user:number,group:number) {
  // Nakon što se korisnik suspenduje, možete ažurirati tabelu ili obavestiti korisnika o uspehu ili neuspehu operacije.
	this.userService.suspend2(user,group).subscribe((rep)=>{	    
		this.groupService.getOneGroup(group).subscribe((gr=>{
			this.group=gr;
			this.cdr.detectChanges();
		}))
  });
}

  addAdmin(user:number,group:number) {
  // Nakon što se korisnik suspenduje, možete ažurirati tabelu ili obavestiti korisnika o uspehu ili neuspehu operacije.
	this.userService.addGrAdmin(user,group).subscribe((rep)=>{	    
		this.groupService.getOneGroup(group).subscribe((gr=>{
			this.group=gr;
			this.cdr.detectChanges();
		}))
  });
}
removeAdmin() {
  this.userService.removeGrAdmin(this.selectedAdminToRemove, this.group.id).subscribe(() => {
    this.groupService.getOneGroup(this.group.id).subscribe((gr) => {
      if (gr == null) {
        this.group = null;
        console.log('Grupa je obrisana');

        this.router.navigate(['/profile']);
      } else {
        this.group = gr;
      }
      this.cdr.detectChanges();
    }, (error) => {
      if (error.status === 404) {
        this.group = null; // Grupa ne postoji
        console.log('Grupa ne postoji');

        this.router.navigate(['/profile']);
      } else {
        console.error('Greška pri dohvatanju grupe:', error);
      }
    });
  });
}


posaljiZahtevZaGrupu() {
  if(this.isRequestSent==false){	
  this.groupService.posZahtev(this.group.id)
    .subscribe(res => {
	this.isRequestSent = true;
	this.cdr.detectChanges();
    });
}
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


toggleReplies(comment: any) {
    comment.showReplies = !comment.showReplies;
}


formatTimestamp(timestamp: number[]): string {
    const date = new Date(timestamp[0], timestamp[1] - 1, timestamp[2], timestamp[3], timestamp[4]);
    const formattedDate = `${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()} ${date.getHours()}:${date.getMinutes()}`;
    return formattedDate;
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
