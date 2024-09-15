import {HttpClient, HttpHeaders, HttpRequest, HttpResponse, HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {catchError, filter, map} from 'rxjs/operators';
import { tap } from 'rxjs/operators';

export enum RequestMethod {
  Get = 'GET',
  Head = 'HEAD',
  Post = 'POST',
  Put = 'PUT',
  Delete = 'DELETE',
  Options = 'OPTIONS',
  Patch = 'PATCH'
}

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  headers = new HttpHeaders({
    'Accept': 'application/json',
    'Content-Type': 'application/json',
    'Access-Control-Allow-Methods': 'GET, POST, PUT, PATCH, DELETE',
    'Access-Control-Allow-Origin': 'http://localhost:4200'
  });
   headers2 = new HttpHeaders({
    'Accept': 'application/json',
    'Content-Type': 'application/json'
  });
 
              
  constructor(private http: HttpClient) { }

  get(path: string, args?: any): Observable<any> {
    const options = {
      headers: this.headers,
    };

    if (args) {
      options['params'] = this.serialize(args);
    }
	console.log(path+" apiServ");
    return this.http.get(path, options)
      .pipe(catchError(this.checkError.bind(this)));
  }
  

  post(path: string, body?: any, customHeaders?: HttpHeaders): Observable<any> {
	
    return this.request(path, body, RequestMethod.Post, customHeaders);
  }

  put(path: string, body?: any): Observable<any> {
    return this.request(path,body, RequestMethod.Put);
  }

  delete(path: string, body?: any): Observable<any> {
	console.log(path,body);
 	//this.headers2.append('Access-Control-Allow-Origin', 'http://localhost:4200');
  	//this.headers2.append('Access-Control-Allow-Credentials', 'true');
    return this.request(path, body, RequestMethod.Delete);
  }

  private request(path: string, body?: any, method = RequestMethod.Post, custemHeaders?: HttpHeaders): Observable<any> {
    const req = new HttpRequest(method, path, body, {
      headers: custemHeaders || this.headers,
    });
    //req.headers.append('Access-Control-Allow-Methods', 'GET, POST, PUT, PATCH, DELETE');
    //req.headers.append('Access-Control-Allow-Origin', 'http://localhost:4200');
	console.log(method,path);
  /*  return this.http.request(req).pipe(filter(response => response instanceof HttpResponse))
      .pipe(map((response: HttpResponse<any>) => {
  console.log('Response received:', response.body);
  return response.body;
}))
      .pipe(catchError(error => this.checkError(error)));
  }*/
  return this.http.request(req).pipe(
  filter(response => response instanceof HttpResponse),
  tap((response: HttpResponse<any>) => {
    console.log('Response received:', response.body);
  }),
  map((response: HttpResponse<any>) => response.body),
  catchError(error => this.checkError(error))
);

}
  private checkError(error: any): any {
    throw error;
  }

  private serialize(obj: any): HttpParams {
    let params = new HttpParams();
  
    for (const key in obj) {
      if (obj.hasOwnProperty(key) && !this.looseInvalid(obj[key])) {
        params = params.set(key, obj[key]);
      }
    }
  
    return params;
  }

  private looseInvalid(a: string | number): boolean {
    return a === '' || a === null || a === undefined;
  }

}
