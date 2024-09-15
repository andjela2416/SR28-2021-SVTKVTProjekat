import {Injectable} from '@angular/core';
import {ApiService} from './api.service';
import {ConfigService} from './config.service';
import {HttpHeaders} from "@angular/common/http";
import { map } from 'rxjs/operators';
import {ActivatedRoute, Router} from "@angular/router";
 

@Injectable({
  providedIn: 'root'
})
export class GroupService {

  constructor(
    private apiService: ApiService,
    private config: ConfigService,
     private router: Router,
     private route: ActivatedRoute,
  ) {
  }

  getAll() {
    return this.apiService.get(this.config.groups_url);
  }
   getAllForUser() {
    return this.apiService.get(this.config.groupsForUser_url);
  }
     getAllForUser2() {
    return this.apiService.get(this.config.groupsForUser2_url);
  }
       getAllForUser22(userId) {
    return this.apiService.get(this.config.groupsForUser2_url+"/profil/"+userId);
  }
       getAllGroupsUsers(id) {
    return this.apiService.get(this.config.userGroupsAll+id+"/users");
  }
  
     getAllForUser3() {
    return this.apiService.get(this.config.groupsForUser3_url);
  }
  getGroupRequests(id) {
	console.log(id);
	const body = {
  		'group': id,

	};
	console.log(body);
    return this.apiService.post(this.config.getOneGroup2_url+'/groupRequests/all/'+id);
  }
  
   checkIfBanned(id) {
    return this.apiService.get(this.config.getOneGroup2_url+'/isBanned/'+id);
  }
  
    deleteGroupRequest(id){
	  const body = {
	'id':id
  };
    return this.apiService.delete(this.config.getOneGroup2_url+ "/deleteGroupRequest/"+id )
  }
  
    getGroupsRequests() {
    return this.apiService.get(this.config.getOneGroup2_url+'/groupRequests/all');
  }
  
    getGroupPosts(id) {
    return this.apiService.get(this.config.sedam+'?id='+id);
  }
    prihvati(id) {
	console.log(id);
	const body = {
  		'group': id,

	};
	console.log(body);
    return this.apiService.put(this.config.getOneGroup2_url+'/approve/'+id);
  }


  getAllFromUser() {
    return this.apiService.get(this.config.usersGroups_url);
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
editGroup(post) {

  const body = {
	'id':post.id,
    'name': post.name,
    'description':post.description
  };

  console.log(body);

  return this.apiService.put(this.config.editGroup_url, JSON.stringify(body));
}
  delete(postId){
	  const body = {
	'id':postId
  };
	console.log("De"+postId);
    return this.apiService.delete(this.config.getOneGroup2_url+ "/delete?id=" + postId, JSON.stringify(postId))
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

  suspend(postId,id){
	const body ={
		'id': id,
		'suspendedReason':postId.suspendedReason
	}
	console.log("De"+postId.SuspendedReason, id);
    return this.apiService.delete( "http://localhost:8080/api/groups/suspend" ,JSON.stringify(body))
    .pipe(map(() => {
      console.log("Delete success");
      this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
        this.router.navigate(['']);}); 
    }))
  }

 getOneGroup(id: number) {
	//const body = id;
  const url = this.config.getOneGroup_url.replace("{id}",id.toString());
  console.log(id.toString()+"getOneGroup u gr serveru",url);
  return this.apiService.get(url);
}

 getOneGroupN(id: number) {
	//const body = id;
  const url = this.config.getOneGroupN_url.replace("{id}",id.toString());
  
  return this.apiService.get(url);
}
 getOneGroupU(id: number) {
	//const body = id;
  const url = this.config.getOneGroupU_url.replace("{id}",id.toString());
  
  return this.apiService.get(url);
}
 getAllGroupPosts(id: number) {
	//const body = id;
  const url = this.config.allGroupPosts_url+"?id="+id;
  console.log(id.toString()+"getOneGroup u gr serveru",url);
  return this.apiService.get(url);
}
 getAllGroupPostsYour(id: number) {
	//const body = id;
  const url = this.config.allGroupPostsYour_url+"?id="+id;
  
  return this.apiService.get(url);
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
  const body = {
    'post': post.post,
    'text': post.text
  };
	console.log(body);
  return this.apiService.post(this.config.addCom_url, JSON.stringify(body));
}
  posZahtev( a:number,post?) {
  const body = {
    'group_id': a
  };
	console.log(body);
  return this.apiService.post(this.config.getOneGroup2_url+'/createRequest/'+a, JSON.stringify(body));
}
sort(sort) {
  
  console.log(sort);

  return this.apiService.get("http://localhost:8080/api/comments/sort?sort="+sort.sort+"&id="+sort.post);
}
sort2(sort) {
  
  console.log(sort);

  return this.apiService.get("http://localhost:8080/api/posts/all/sorted?sort="+sort.sort);
}
 suspendGr(values:any) {
	const body = {
  		'id': values.group,
  		'suspendedReason': values.suspendedReason
	};
;
    return this.apiService.delete(this.config.susGr, JSON.stringify(body))

  }

 create(values:any) {
    const loginHeaders = new HttpHeaders({
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    });
    // const body = `username=${user.username}&password=${user.password}`;
  	
	const body = {
  		'name': values.name,
  		'description': values.description
	};
    console.log(body);
    return this.apiService.post(this.config.groupcreate_url, JSON.stringify(body), loginHeaders)
     /* .subscribe((res) => {
        if(res.body == "NOT_ACCEPTABLE" || res.name == "HttpErrorResponse")
        {
          alert("try again")
        }else {
          alert("Uspesno ste kreirali grupu");
        }
      });*/
  }
}
