import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ConfigService {

  private _api_url = 'http://localhost:8080/api';
  private _user_url = this._api_url + '/users';
  
     private _usersa_url = this._user_url + '/all/3';

  get usersa_url(): string {
    return this._usersa_url;
  }
     private _uploadPhoto_url = this._api_url+"/users/upload";
	
  get uploadPhoto_url(): string {
    return this._uploadPhoto_url;
  }
  
    private _jedan = this._api_url+"/users/all";
	
  get jedan(): string {
    return this._jedan;
  }
        private _tri = this._api_url+"/groups/nisuuclanjeni";
	
  get tri(): string {
    return this._tri;
  }
          private _userGroupsAll = this._api_url+"/groups/all/";
	
  get userGroupsAll(): string {
    return this._userGroupsAll;
  }
  
          private _nula = this._api_url+"/groups";
	
  get nula(): string {
    return this._nula;
  }
  
            private _susGr = this._api_url+"/groups/suspend";
	
  get susGr(): string {
    return this._susGr;
  }
            private _nula2 = this._api_url+"/groups/gru";
	
  get nula2(): string {
    return this._nula2;
  }
  
     private _sest = this._api_url+"/groups/uclanjeni/{id}";

  get sest(): string {
    return this._sest;
  }
  
      private _sedam = this._api_url+"/groups/allPosts";
	
  get sedam(): string {
    return this._sedam;
  }
  
        private _postUserId = this._api_url+"/posts/all/user/";
	
  get postUserId(): string {
    return this._postUserId;
  }
  
      private _osam =this._api_url+"/posts/all";
	
  get osam(): string {
    return this._osam;

  }
    private _devet = this._api_url+"/groups/random";

	
  get devet(): string {
    return this._devet;
  }  
  
      private _deset = this._api_url+"/posts";
	
  get deset(): string {
    return this._deset;
  }
  
      private _jedanaest = this._api_url+"/comments/all/post";
	
  get jedanaest(): string {
    return this._jedanaest;
  }
    private _pet = this._api_url+"/groups/uclanjeni";
	
      private _dvanaest = this._api_url+"/comments/{id}";
  get pet(): string {
    return this._pet;
  }
  	  get dvanaest(): string {
    return this._dvanaest;
  }

        private _trinaest = this._api_url+"/posts/all/user";
	
  get trinaest(): string {
    return this._trinaest;
  }
        private _sesnaest = this._api_url+"/groups/all/user";
	
  get sesnaest(): string {
    return this._sesnaest;
  }
  
        private _sedamnaest = this._api_url+"/groups/allPosts/your";
	
  get sedamnaest(): string {
    return this._sedamnaest;
  }
          private _cetiri = this._api_url+"/groups/nisuuclanjeni/{id}";
	
  get cetiri(): string {
    return this._cetiri;
  }

  
        private _osamnaest = this._api_url+"/comments/all/user";
	
  get osamnaest(): string {
    return this._osamnaest;
  }
  
        private _devetnaest = this._api_url+"/comments";
	
  get devetnaest(): string {
    return this._devetnaest;
  }
        private _dvadeset = this._api_url+"/groups/reports";
	
  get dvadeset(): string {
    return this._dvadeset;
  }    
    private _cetrnaest = this._api_url+"/posts/{id}";
	
  get cetrnaest(): string {
    return this._cetrnaest;
  }

      private _petnaest = this._api_url+"/posts/{id}";
	
  get petnaest(): string {
    return this._petnaest;
  }
  
      private _dva = this._api_url+"/groups/all";
	
  get dva(): string {
    return this._dva;
  }
  
  get user_url(): string {
    return this._user_url;
  }
  private _postcreate_url = this._api_url + "/posts/create";
	
  get postcreate_url(): string {
    return this._postcreate_url;
  }
   private _postcreate2_url = this._api_url + "/posts/createInGroup";
	
  get postcreate2_url(): string {
    return this._postcreate2_url;
  }
  
      private _grouprequestcreate_url = this._api_url + "/group-requests/create";
	
  get grouprequestcreate_url(): string {
    return this._grouprequestcreate_url;
  }
    private _groupcreate_url = this._api_url + "/groups/create";
	
  get groupcreate_url(): string {
    return this._groupcreate_url;
  }
  
  private _commentsForPost_url = this._api_url + "/comments/all/post";
	
  get commentsForPost_url(): string {
    return this._commentsForPost_url;
  }
  
    private _reactionsForPost_url = this._api_url + "/posts/post-reaction-count";
	
  get reactionsForPost_url(): string {
    return this._reactionsForPost_url;
  }
      private _reactionsForCom_url = this._api_url + "/comments/comments-reaction-count";
	
  get reactionsForCom_url(): string {
    return this._reactionsForCom_url;
  }
  
   private _repliesForComment_url = this._api_url + "/comments/commentReplies";
	
  get repliesForComment_url(): string {
    return this._repliesForComment_url;
  }
  
  private _editCom_url = this._api_url + "/comments/edit";
	
  get editCom_url(): string {
    return this._editCom_url;
  }
  
    private _editGroup_url = this._api_url + "/groups/edit";
	
  get editGroup_url(): string {
    return this._editGroup_url;
  }
   private _groups_url = this._api_url + "/groups/all";
	
  get groups_url(): string {
    return this._groups_url;
  }
     private _groupsa_url = this._api_url + "/groups/all/g";
	
  get groupsa_url(): string {
    return this._groupsa_url;
  }
       private _groupsForUser_url = this._api_url + "/groups/nisuuclanjeni";
	
  get groupsForUser_url(): string {
    return this._groupsForUser_url;
  }
        private _groupsForUser2_url = this._api_url + "/groups/uclanjeni";
	
  get groupsForUser2_url(): string {
    return this._groupsForUser2_url;
  }
          private _groupsForUser3_url = this._api_url + "/groups/admin";
	
  get groupsForUser3_url(): string {
    return this._groupsForUser3_url;
  }
  
  private _usersComms_url = this._api_url + "/comments/all/user";
	
  get usersComms_url(): string {
    return this._usersComms_url;
  }
    private _usersGroups_url = this._api_url + "/groups/all/user";
	
  get usersGroups_url(): string {
    return this._usersGroups_url;
  }
      
  private _replyToCom_url = this._api_url + "/comments/{commentId}/reply";
	
  get replyToCom_url(): string {
    return this._replyToCom_url;
  }
    
  private _addCom_url = this._api_url + "/comments/create";
	
  get addCom_url(): string {
    return this._addCom_url;
  }
  
    private _getOneCom_url = this._api_url + "/comments/{id}";
	
  get getOneCom_url(): string {
    return this._getOneCom_url;
  }
  
      private _getOneGroup_url = this._api_url + "/groups/{id}";
	
  get getOneGroup_url(): string {
    return this._getOneGroup_url;
  }
   
      private _getOneGroupN_url = this._api_url + "/groups/nisuuclanjeni/{id}";
	
  get getOneGroupN_url(): string {
    return this._getOneGroupN_url;
  }
        private _getOneGroupU_url = this._api_url + "/groups/uclanjeni/{id}";
	
  get getOneGroupU_url(): string {
    return this._getOneGroupU_url;
  }
  
      private _getOneCom2_url = this._api_url + "/comments";
	
  get getOneCom2_url(): string {
    return this._getOneCom2_url;
  }
          private _banns_url = this._api_url + "/groups/banns";
	
  get banns_url(): string {
    return this._banns_url;
  }
  
            private _banns2_url = this._api_url + "/groups/banns2";
	
  get banns2_url(): string {
    return this._banns2_url;
  }
  
  
        private _reports_url = this._api_url + "/groups/reports";
	
  get reports_url(): string {
    return this._reports_url;
  }
  
          private _reports2_url = this._api_url + "/groups/reportsGrAdmin";
	
  get reports2_url(): string {
    return this._reports2_url;
  }
  
        private _getOneGroup2_url = this._api_url + "/groups";
	
  get getOneGroup2_url(): string {
    return this._getOneGroup2_url;
  }
          private _getOneGroupp_url = this._api_url + "/groupsb";
	
  get getOneGroupp_url(): string {
    return this._getOneGroupp_url;
  }
	private _allGroupPosts_url = this._api_url + "/groups/allPosts";
	
  get allGroupPosts_url(): string {
    return this._allGroupPosts_url;
  }
		private _allGroupPostsYour_url = this._api_url + "/groups/allPosts/your";
	
  get allGroupPostsYour_url(): string {
    return this._allGroupPostsYour_url;
  }
	

  private _change_pass_url = this._user_url + '/password-change';

  get change_pass_url(): string {
    return this._change_pass_url;
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
  private _posts_random_url = this._api_url + '/groups/random';

  get posts_random_url(): string {
    return this._posts_random_url;
  }
 private _postsFromUser_url = this._api_url + '/posts/all/user';

  get postsFromUser_url(): string {
    return this._postsFromUser_url;
  }
  
   private _userComments_url = this._api_url + '/posts/user-comments';

  get userComments_url(): string {
    return this._userComments_url;
  }
  
   private _getOnePost_url = this._api_url + '/posts/{id}';

  get getOnePost_url(): string {
    return this._getOnePost_url;
  }
    private _getOnePostt_url = this._api_url + '/posts/a';

  get getOnePostt_url(): string {
    return this._getOnePostt_url;
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
  private _editUser_url = this._user_url + '/edit';

  get editUser_url(): string {
    return this._editUser_url;
  }

}
