import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators ,FormControl} from "@angular/forms";
import { Router } from "@angular/router";
import { PostService } from "src/app/service/post.service";
import { Location } from '@angular/common';
@Component({
  selector: 'app-add-post',
  templateUrl: './add-post.component.html',
  styleUrls: ['./add-post.component.css']
})
export class AddPostComponent implements OnInit {
  post = {};
  form: FormGroup;
  submitted=false;

  constructor(
    private postService: PostService,
    private router: Router,
    private formBuilder: FormBuilder,
       private location: Location
  ) { }

ngOnInit() {
  this.form = this.formBuilder.group({
    post: ['', Validators.required],
    pathSlike: ['']
  });
}

  onSubmit2() {

	if (!this.form.value.post){
			 alert('Tekst je obavezno polje objave');
		
	console.log(this.form.value.pathSlike);
    console.warn('Your order has been submitted', this.form.value);
    this.postService.add(this.form.value);//.subscribe((result) => {
  //    this.router.navigate(["/posts"]);
   //s });

  }
  
}
onSubmit() {
  if (!this.form.get('post').value) {
    alert('Tekst je obavezno polje objave');
    return;
  }

  console.log(this.form.value.pathSlike);
  console.warn('Your order has been submitted', this.form.value);
  
 this.postService.add(this.form.value).subscribe(() => {
    // Vraćanje na prethodnu stranicu
    this.location.back();
  });

}

} 
