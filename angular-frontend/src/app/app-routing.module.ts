import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { SignUpComponent } from './sign-up/sign-up.component';
import { PostListContentComponent } from './posts/post-list-content/post-list-content.component';
import { YourCommentsContentComponent } from './comments/your-comments-content/your-comments-content.component';
import { CommentListComponent } from './allPosts/comment-list/comment-list.component';
import { AllPostListContentComponent } from './allPosts/allPost-list-content/allPost-list-content.component';
import { GroupsListContentComponent } from './groups/groups-list-content/groups-list-content.component';
import { AddPostComponent } from './posts/add-post/add-post.component';

const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
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

  {
    path: 'posts',
    component: PostListContentComponent,
  },
   {
    path: 'groups',
    component: GroupsListContentComponent,
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
