import {Injectable} from '@angular/core';
import {ApiService} from './api.service';
import {ConfigService} from './config.service';
import {map} from 'rxjs/operators';

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
    userIsPresent() {
    return this.currentUser != undefined && this.currentUser != null;
  }
report2(react) {
  
   const novaReakcija = {
    reported2:react.id,
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
report3(react) {
  
   const novaReakcija = {
    reported3:react.id,
    reportReason:react.reason
  };

  console.log(novaReakcija);

  return this.apiService.post("http://localhost:8080/api/groups/report", JSON.stringify(novaReakcija));
}

  getAll() {
    return this.apiService.get(this.config.users_url);
  }
   getAllReports() {
    return this.apiService.get(this.config.reports_url);
  }
 get() {
    return this.apiService.put(this.config.user_url+"/edit");
  }
   getA(body?:any) {
	console.log("pozv");
    return this.apiService.put(this.config.post_url+"/edit", JSON.stringify(body));
  }
}
