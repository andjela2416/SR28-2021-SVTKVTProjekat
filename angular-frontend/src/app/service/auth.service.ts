import { Injectable } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ApiService } from './api.service';
import { UserService } from './user.service';
import { ConfigService } from './config.service';
import { catchError, map } from 'rxjs/operators';
import { Router } from '@angular/router';
import { of } from 'rxjs/internal/observable/of';
import { Observable } from 'rxjs';
import { _throw } from 'rxjs/observable/throw';
import { tap } from 'rxjs/operators';



@Injectable()
export class AuthService {

  constructor(
    private apiService: ApiService,
    private userService: UserService,
    private config: ConfigService,
    private router: Router,

  ) {
  }

  private access_token = null;
  

  login(user) {
  const loginHeaders = new HttpHeaders({
    'Accept': 'application/json',
    'Content-Type': 'application/json',
    'Access-Control-Allow-Methods': 'GET, POST, PUT, PATCH, DELETE',
    'Access-Control-Allow-Origin': 'http://localhost:4200'
  });

  const body = {
    'username': user.username,
    'password': user.password
  };

  return this.apiService.post(this.config.login_url, JSON.stringify(body), loginHeaders)
    .pipe(
      tap((res) => {
        console.log('Login success');
        this.access_token = res.accessToken;
        localStorage.setItem("jwt", res.accessToken);

        // Preusmeravanje na /posts nakon uspe�ne prijave
        this.router.navigate(['/allPosts']);
      })
    );
}

    changePassword(body) {
    console.log(JSON.stringify(body))
    return this.apiService.put(this.config.change_pass_url, JSON.stringify(body))
      .pipe(map(() => {
        console.log("Change success");
      }))

  }

  signup(user) {
    const signupHeaders = new HttpHeaders({
      'Accept': 'application/json',
      'Content-Type': 'application/json',
       'Access-Control-Allow-Methods': 'GET, POST, PUT, PATCH, DELETE',
    'Access-Control-Allow-Origin': 'http://localhost:4200'
    });
    console.log(user.displayname);
    return this.apiService.post(this.config.signup_url, JSON.stringify(user), signupHeaders)
      .pipe(map(() => {
        console.log('Sign up success');
      }));
  }

  logout() {
    this.userService.currentUser = null;
    this.access_token = null;
    this.router.navigate(['/login']);
  }

  tokenIsPresent() {
    return this.access_token != undefined && this.access_token != null;
  }
  
  getToken() {
    return this.access_token;
  }
  

}
