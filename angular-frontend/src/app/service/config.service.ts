import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ConfigService {

  private _api_url = 'http://localhost:8080/api';
  private _user_url = this._api_url + '/users';
  private _postcreate_url = this._api_url + "/posts/create";
	
  get postcreate_url(): string {
    return this._postcreate_url;
  }
	
  private _login_url = this._user_url + '/login';

  get login_url(): string {
    return this._login_url;
  }
   private _post_url = this._api_url + '/posts';

  get post_url(): string {
    return this._post_url;
  }

  private _whoami_url = this._user_url + '/whoami';

  get whoami_url(): string {
    return this._whoami_url;
  }

  private _users_url = this._user_url + '/all';

  get users_url(): string {
    return this._users_url;
  }
  private _posts_url = this._api_url + '/posts/all';

  get posts_url(): string {
    return this._posts_url;
  }
 private _postsFromUser_url = this._api_url + '/posts/all/user';

  get postsFromUser_url(): string {
    return this._postsFromUser_url;
  }
  
   private _getOnePost_url = this._api_url + '/posts/{id}';

  get getOnePost_url(): string {
    return this._getOnePost_url;
  }
  private _club_url = this._api_url + '/clubs';

  get club_url(): string {
    return this._club_url;
  }
  
  //TODO: implementirati :)
  private _signup_url = this._user_url + '/signup';

  get signup_url(): string {
    return this._signup_url;
  }

}
