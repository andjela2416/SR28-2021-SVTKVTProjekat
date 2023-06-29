import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { Router } from "@angular/router";
import { PostService } from "src/app/service/post.service";
@Component({
  selector: 'app-add-post',
  templateUrl: './add-post.component.html',
  styleUrls: ['./add-post.component.css']
})
export class AddPostComponent implements OnInit {
  post = {};
  form: FormGroup;


  constructor(
    private postService: PostService,
    private router: Router,
    private formBuilder: FormBuilder
  ) { }

  ngOnInit() {
    this.form = this.formBuilder.group({
      content: [
        "",
        Validators.compose([
          Validators.required,
          Validators.minLength(3),
          Validators.maxLength(64),
        ]),
      ]
    });
  }

  onSubmit() {
    console.log(this.form.value)
    this.postService.add(this.form.value).subscribe((result) => {
      this.router.navigate(["/posts"]);
    });
  }



}
