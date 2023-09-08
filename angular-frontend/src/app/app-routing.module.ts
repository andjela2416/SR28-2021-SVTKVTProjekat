import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { EditProfileComponent } from './edit-profile/edit-profile.component';
import { SignUpComponent } from './sign-up/sign-up.component';
import { PostListContentComponent } from './posts/post-list-content/post-list-content.component';
import { CommentListComponent } from './allPosts/comment-list/comment-list.component';
import { AllPostListContentComponent } from './allPosts/allPost-list-content/allPost-list-content.component';
import { GroupsListContentComponent } from './groups/groups-list-content/groups-list-content.component';
import { OneGroupContentComponent } from './groups/one-group-content/one-group-content.component';
import { AddPostComponent } from './posts/add-post/add-post.component';
import { GroupRequestsComponent } from './groups/group-requests/group-requests.component';
import { FriendRequestsComponent } from './edit-profile/friend-requests/friend-requests.component';

const routes: Routes = [
  {
    path: '',
    component: LoginComponent,
    pathMatch: 'full'
  },
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: 'signup',
    component: SignUpComponent,
  },
  { path: 'profile/:userId',
   component: UserProfileComponent 
  }
  ,
  { path: 'profile',
   component: EditProfileComponent 
  },
  {
    path: 'posts',
    component: PostListContentComponent,
  },
   {
    path: 'groups',
    component: GroupsListContentComponent,
  }
  ,
   {
    path: 'groupsRequests',
    component: GroupRequestsComponent,
  }
  ,
  {
    path: 'friendRequests',
    component: FriendRequestsComponent,
  }
  ,
   {
    path: 'group/:id',
    component: OneGroupContentComponent,
  },
   {
    path: 'comments',
    component: CommentListComponent,
  },
    {
    path: 'allPosts',
    component: AllPostListContentComponent,
  },
  {
    path: 'posts/create',
    component: AddPostComponent,}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
