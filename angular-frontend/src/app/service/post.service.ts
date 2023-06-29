import {Injectable} from '@angular/core';
import {ApiService} from './api.service';
import {ConfigService} from './config.service';
import {HttpHeaders} from "@angular/common/http";
import { map } from 'rxjs/operators';
import {ActivatedRoute, Router} from "@angular/router";
@Injectable({
  providedIn: 'root'
})
export class PostService {

  constructor(
    private apiService: ApiService,
    private config: ConfigService,
     private router: Router,
     private route: ActivatedRoute,
  ) {
  }

  getAll() {
    return this.apiService.get(this.config.posts_url);
  }
  
  getAllFromUser() {
    return this.apiService.get(this.config.postsFromUser_url);
  }
  edit(post){
    return this.apiService.put(this.config.post_url + "/edit", JSON.stringify(post))
    .pipe(map(() => {
      console.log("Edit success");
      this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
        this.router.navigate(['/posts']);}); 
    }))
  }
  delete(postId){
	console.log("De"+postId);
    return this.apiService.delete(this.config.post_url+ "/delete?id=" + postId)
    .pipe(map(() => {
      console.log("Delete success");
      this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
        this.router.navigate(['/posts']);}); 
    }))
  }
  
 getOnePost(id: number) {
	const body = id;
  const url = this.config.getOnePost_url.replace("{id}",id.toString());
  console.log(id.toString()+"getOnepost u post serveru",url);
  return this.apiService.get(url,body);
}

 add(post){
    return this.apiService.post(this.config.posts_url + "/create", JSON.stringify(post))
  }
  create(values:any) {
    const loginHeaders = new HttpHeaders({
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    });
    // const body = `username=${user.username}&password=${user.password}`;
  	const imageArray = values.pathSlike.split(',').map(path => ({ path: path }));
	const body = {
  		'content': values.post,
  		'images': imageArray
	};
    console.log(body);
    return this.apiService.post(this.config.postcreate_url, JSON.stringify(body), loginHeaders)
      .subscribe((res) => {
        if(res.body == "NOT_ACCEPTABLE" || res.name == "HttpErrorResponse")
        {
          alert("try again")
        }else {
          alert("Uspesno ste postavili objavu");
          let returnUrl : String;
          returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
          this.router.navigate([returnUrl + "/HomePage"]);
        }
      });
  }
}
