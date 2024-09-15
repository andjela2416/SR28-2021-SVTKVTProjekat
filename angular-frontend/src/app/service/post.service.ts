import {Injectable} from '@angular/core';
import {ApiService} from './api.service';
import {ConfigService} from './config.service';
import {HttpHeaders} from "@angular/common/http";
import { map } from 'rxjs/operators';
import { catchError, filter, tap } from 'rxjs/operators';
import {ActivatedRoute, Router} from "@angular/router";
 import { Observable } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { mergeMap, take } from 'rxjs/operators';
import { forkJoin, of } from 'rxjs';




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
  
   getAllPostUserId(id) {
    return this.apiService.get(this.config.postUserId+id);
  }
   getAllRndm() {
    return this.apiService.get(this.config.posts_random_url);
  }
  
  getAllFromUser() {
    return this.apiService.get(this.config.postsFromUser_url);
  }
  
    getCountReactions(post:any) {
    return this.apiService.get(this.config.reactionsForPost_url+"?id="+post);
  }
  
    getAllUserComments() {
    return this.apiService.get(this.config.userComments_url);
  }
  
  edit1(post){
	const imageArray = post.pathSlike.split(',').map(path => ({ path: path }));
	const body = {
  		'content': post.post,
  		'images': imageArray
	};
    return this.apiService.put(this.config.post_url + "/edit", JSON.stringify(body))
    .pipe(map(() => {
      console.log("Edit success");
      this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
        this.router.navigate(['/posts']);}); 
    }))
  }

react(react) {
  
   const novaReakcija = {
    type: react.type,
    post: react.post,
    comment: react.comment
  };

  console.log(novaReakcija);

  return this.apiService.post("http://localhost:8080/api/reactions/create", JSON.stringify(novaReakcija));
}




editNe(post) {
  let imageArray: { path: string }[] = [];
  if (post.images) {
    imageArray = post.images.split(',').map((path: string) => ({ path }));
  }
  
  const body = {
    'id': post.id,
    'content': post.content,
    'images': imageArray
  };

  console.log(body);
return this.apiService.put(this.config.post_url+"/edit", JSON.stringify(body))
      .subscribe((res) => {
        if(res.body == "NOT_ACCEPTABLE" || res.name == "HttpErrorResponse")
        {
          alert("try again")
        }else {
          alert("Uspesno ste postavili objavu");
         // let returnUrl : String;
          //returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
          //this.router.navigate([returnUrl + "/HomePage"]);
          this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
        this.router.navigate(['/posts']);
      });
        }
      });
  }
 /* return this.apiService.put(this.config.post_url + "/edit", JSON.stringify(body))
    .pipe(map((response: HttpResponse<any>) => {
      if (response.ok) {
        console.log("Edit success");
      } else {
        console.log("Edit failed"+response.ok);
      }
      this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
        this.router.navigate(['/posts']);
      });
    }));
}*/

  delete(postId){
	console.log("De"+postId);
    return this.apiService.delete(this.config.post_url+ "/delete/"+postId ,JSON.stringify(postId))
    
  }
  
 getOnePost(id: number) {
	const body = id;
  const url = this.config.getOnePost_url.replace("{id}",id.toString());
  console.log(id.toString()+"getOnepost u post serveru",url);
  return this.apiService.get(url,body);
}
getOnePost2(id: number): Observable<any> {
  const url = this.config.getOnePost_url.replace("{id}", id.toString());
  console.log(id.toString() + " getOnePost u postService-u", url);
  return this.apiService.get(url).pipe(
    map((response: any) => {
      console.log('Response received:', response);
      return response;
    }),
    catchError(error => {
      console.error('Error:', error);
      throw error;
    })
  );
}

 add2(post){
	alert("add"+post);
	const imageArray = post.pathSlike.split(',').map(path => ({ path: path }));
	const body = {
  		'content': post.post,
  		'images': imageArray
	};
	
	
    return this.apiService.post(this.config.post_url + "/create", JSON.stringify(body))
     .subscribe((res) => {
        if(res.body == "NOT_ACCEPTABLE" || res.name == "HttpErrorResponse")
        {
          alert("try again")
        }else {
          alert("Uspesno ste postavili objavu");
          let returnUrl : String;
          returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
          this.router.navigate([returnUrl + ""]);
        }
      });
    
  }
  add(post: any) {
  const imageArray = post.pathSlike.split(',').map(path => ({ path: path }));
  const body = {
    'content': post.post,
    'images': imageArray
  };

  return this.apiService.post(this.config.post_url + "/create", JSON.stringify(body));
}
editPre(post) {

  
  const body = {
    'id': post.id,
    'content': post.content,
  };

  console.log(body);

  return this.apiService.put(this.config.post_url+"/edit", JSON.stringify(body));
}

		edit(values: any, selectedFiles?: any[] | undefined) {
		  const body = {
		    'id': values.id,
		    'content': values.content,
		  };
		
		  const formData = new FormData();
		  formData.append('post', values.id);
		
		  if (selectedFiles && selectedFiles.length > 0) {
		    selectedFiles.forEach((image, index) => {
		      formData.append('files', image, image.name);
		    });
		  }
		
		  return this.apiService.put(this.config.post_url + "/edit", JSON.stringify(body)).pipe(
		    mergeMap((response: any) => {
		      const headers3 = new HttpHeaders({
		        'Access-Control-Allow-Methods': 'GET, POST, PUT, PATCH, DELETE',
		        'Access-Control-Allow-Origin': 'http://localhost:4200'
		      });
		
		      return this.apiService.post("http://localhost:8080/api/users/uploadPostPhotoEdit", formData, headers3).pipe(
		        map(() => response) 
		      );
		    })
		  );
		}





	create(values: any, selectedFiles?: any[] | undefined) {
	
	  const body = {
	    'content': values.post,
	  };
	
	  return this.apiService.post(this.config.postcreate_url, JSON.stringify(body)).pipe(
	    mergeMap((response: any) => {
		   const headers3 = new HttpHeaders({
		    'Access-Control-Allow-Methods': 'GET, POST, PUT, PATCH, DELETE',
		    'Access-Control-Allow-Origin': 'http://localhost:4200'
		  });
	      if (selectedFiles && selectedFiles.length > 0) {
	        const uploadObservables = selectedFiles.map(image => {
	          const formData = new FormData();
	          formData.append('post', response.id);
	          console.log(response.id);
	          formData.append('file', image,image.name);
	          return this.apiService.post("http://localhost:8080/api/users/uploadPostPhoto", formData, headers3);
	        });
	
	        // Koristimo forkJoin da bismo obradili sve slike istovremeno
	        return forkJoin(uploadObservables).pipe(
	          map(() => response) // Vraćamo originalni odgovor nakon što se sve slike uploaduju
	        );
	      } else {
	        return of(response); // Ako nema slika, samo vraćamo originalni odgovor
	      }
	    })
	  );
	}

  
   createInGroup(values:any, selectedFiles?: any[] | undefined) {

	const body = {
  		'content': values.post,
  		'group':values.group
	};
	console.log(body);
	return this.apiService.post(this.config.postcreate2_url, JSON.stringify(body)).pipe(
	    mergeMap((response: any) => {
		   const headers3 = new HttpHeaders({
		    'Access-Control-Allow-Methods': 'GET, POST, PUT, PATCH, DELETE',
		    'Access-Control-Allow-Origin': 'http://localhost:4200'
		  });
	      if (selectedFiles && selectedFiles.length > 0) {
	        const uploadObservables = selectedFiles.map(image => {
	          const formData = new FormData();
	          formData.append('post', response.id);
	          console.log(response.id);
	          formData.append('file', image,image.name);
	          return this.apiService.post("http://localhost:8080/api/users/uploadPostPhoto", formData, headers3);
	        });
	
	        // Koristimo forkJoin da bismo obradili sve slike istovremeno
	        return forkJoin(uploadObservables).pipe(
	          map(() => response) // Vraćamo originalni odgovor nakon što se sve slike uploaduju
	        );
	      } else {
	        return of(response); // Ako nema slika, samo vraćamo originalni odgovor
	      }
	    })
	  );
  }
   create4(values:any,group:any) {
    const loginHeaders = new HttpHeaders({
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    });
    // const body = `username=${user.username}&password=${user.password}`;
  	let imageArray = [];
if (values.pathSlike) {
  imageArray = values.pathSlike.split(',').map(path => ({ path: path }));
}

	const body = {
  		'content': values.post,
  		'images': imageArray,
  		'group':group
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
          this.router.navigate([returnUrl + ""]);
        }
      });
  }
  
 create2(values:any) {
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
    return this.apiService.post(this.config.postcreate2_url, JSON.stringify(body), loginHeaders)
      /*.subscribe((res) => {
        if(res.body == "NOT_ACCEPTABLE" || res.name == "HttpErrorResponse")
        {
          alert("try again")
        }else {
          alert("Uspesno ste postavili objavu");
          let returnUrl : String;
          returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
          this.router.navigate([returnUrl + ""]);
        }
      });*/
  }
}
