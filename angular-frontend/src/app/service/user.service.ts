import {Injectable} from '@angular/core';
import {ApiService} from './api.service';
import {ConfigService} from './config.service';
import {map} from 'rxjs/operators';
import { mergeMap, take } from 'rxjs/operators';
import { of } from 'rxjs';
import { HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  currentUser;

  constructor(
    private apiService: ApiService,
    private config: ConfigService
  ) {
  }

  getMyInfo() {
    return this.apiService.get(this.config.whoami_url)
      .pipe(map(user => {
	console.log(user);
        this.currentUser = user;
        this.currentUser.role=user.role;
        console.log(this.currentUser.role);
        return user;
      }));
  }
  
   getOne(id:number) {
    return this.apiService.get(this.config.user_url+"/"+id)
      .pipe(map(user => {
	console.log(user);
        return user;
      }));
  }
  
   deleteFriendRequest(id){
	  const body = {
	'id':id
  };
    return this.apiService.delete(this.config.user_url+ "/deleteFriendRequest/"+id )
  }
  
   prihvati(id) {
	console.log(id);
	const body = {
  		'friendRequest': id,
	};
	console.log(body);
    return this.apiService.put(this.config.user_url+'/approve/'+id);
  }
  
    getFriendRequests() {
    return this.apiService.get(this.config.user_url+'/friendRequests/all');
  }
  
    getFriendRequest(id) {
	console.log(id);
    return this.apiService.post(this.config.user_url+'/friendRequests/all/'+id);
  }
  
      getFriendRequests2(id) {
	console.log(id);
    return this.apiService.post(this.config.user_url+'/friendRequests/all2/'+id);
  }
  
    userIsPresent() {
    return this.currentUser != undefined && this.currentUser != null;
  }
  
    posZahtev( a:number) {
  const body = {
    'toWho': a
  };
	console.log(body);
  return this.apiService.post(this.config.user_url+'/createRequest/'+a, JSON.stringify(body));
}

   izbaciIzPrijatelja(id) {
    return this.apiService.put(this.config.user_url+'/removeFriend/'+id);
  }
  report(react) {
  
   const novaReakcija = {
    reported:react.id,
    reportReason:react.reason
  };

  console.log(novaReakcija);

  return this.apiService.post("http://localhost:8080/api/groups/report", JSON.stringify(novaReakcija));
}
  
report2(react) {
  
   const novaReakcija = {
    reported2Id:react.id,
    reportReason:react.reason
  };

  console.log(novaReakcija);

  return this.apiService.post("http://localhost:8080/api/groups/report", JSON.stringify(novaReakcija));
}

   blokiraj(id) {
	console.log(id);
	const body = {
  		'userId': id,

	};
	console.log(body);
    return this.apiService.put(this.config.user_url+'/'+id+'/'+'block');
  }
  
   search(keyword, firstName?,lastName?) {
	let url = this.config.user_url + '/search?keyword=' + keyword;

    if (firstName) {
        url += '&firstName=' + firstName;
    }

    if (lastName) {
        url += '&lastName=' + lastName;
    }

    return this.apiService.get(url);
  }
  
report3(react) {
  
   const novaReakcija = {
    reported3Id:react.id,
    reportReason:react.reason
  };

  console.log(novaReakcija);

  return this.apiService.post("http://localhost:8080/api/groups/report", JSON.stringify(novaReakcija));
}
editUser(user, image?) {
  const userData = {
    'id': user.id,
    'displayName': user.displayName,
    'description': user.description
  };

  return this.apiService.post(this.config.editUser_url, JSON.stringify(userData)).pipe(
    mergeMap((response: any) => {
      if (image) {
		const headers = new HttpHeaders({
    'Accept': 'application/json',
    'Content-Type': 'multipart/formdata'
  });
   const headers3 = new HttpHeaders({
    'Access-Control-Allow-Methods': 'GET, POST, PUT, PATCH, DELETE',
    'Access-Control-Allow-Origin': 'http://localhost:4200'
  });

        const formData = new FormData();
        formData.append('id', user.id.toString());
        formData.append('file', image, image.name);
		console.log(this.config.uploadPhoto_url);
        return this.apiService.post("http://localhost:8080/api/users/uploadProfilePhoto", formData,headers3).pipe(
          map(() => response) // Vraćamo originalni odgovor nakon što se slika uploaduje
        );
      } else {
        return of(response); // Ako nema slike, samo vraćamo originalni odgovor
      }
    })
  );
}

suspend(report) {
   const body = {
    reported: report.reported ? report.reported : null,
    reported2Id: report.reported2 ? report.reported2.id : null,
    reported3Id: report.reported3 ? report.reported3.id : null,
    id: report.id
  };

  console.log(body);

  return this.apiService.post("http://localhost:8080/api/groups/report/suspend", JSON.stringify(body));
}

suspendUser(user) {

  return this.apiService.post("http://localhost:8080/api/users/suspend/"+user.id);
}


suspend2(user,group) {

  return this.apiService.post("http://localhost:8080/api/groups/report/suspendByGrAdmin/"+user+"/"+group);
}

addGrAdmin(user,group) {

  return this.apiService.post("http://localhost:8080/api/groups/addGroupAdmin/"+group+"/"+user);
}
removeGrAdmin(user,group) {

  return this.apiService.post("http://localhost:8080/api/groups/removeGroupAdmin/"+group+"/"+user);
}

unblock(userId) {
  return this.apiService.put("http://localhost:8080/api/groups/unban/"+userId);
}


unblock2(userId,groupId) {
  return this.apiService.put("http://localhost:8080/api/groups/unban2/"+userId+"/"+groupId);
}


  getAll() {
    return this.apiService.get(this.config.users_url);
  }
     getAllBanns() {
    return this.apiService.get(this.config.banns_url);
  }
       getAllBanns2() {
    return this.apiService.get(this.config.banns2_url);
  }
   getAllReports() {
    return this.apiService.get(this.config.reports_url);
  }
     getAllReports2() {
    return this.apiService.get(this.config.reports2_url);
  }
 get() {
    return this.apiService.put(this.config.user_url+"/edit");
  }
   getA(body?:any) {
	console.log("pozv");
    return this.apiService.put(this.config.post_url+"/edit", JSON.stringify(body));
  }
}
