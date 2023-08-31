import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {HttpClientModule} from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { CardComponent } from './card/card.component';
import { HomeComponent } from './home/home.component';
import { HeaderComponent } from './header/header.component';
import { UserMenuComponent } from './user-menu/user-menu.component';
import { LoginComponent } from './login/login.component';
import { SignUpComponent } from './sign-up/sign-up.component';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';


import {AngularMaterialModule} from './angular-material/angular-material.module';

import {FormsModule, ReactiveFormsModule} from '@angular/forms';


import {ApiService} from './service/api.service';
import {ClubService} from './service/club.service';
import {AuthService} from './service/auth.service';
import {UserService} from './service/user.service';
import {ConfigService} from './service/config.service';
import {PostService} from './service/post.service';
import {ComService} from './service/com.service';
import {GroupService} from './service/group.service';

import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { TokenInterceptor } from './interceptor/TokenInterceptor';
import { AddPostComponent } from './posts/add-post/add-post.component';
import { PostListComponent } from './posts/post-list/post-list.component';
import { YourCommentsComponent } from './comments/your-comments/your-comments.component';
import { AllPostListComponent } from './allPosts/allPost-list/allPost-list.component';
import { GroupsListComponent } from './groups/groups-list/groups-list.component';
import { CommentListComponent } from './allPosts/comment-list/comment-list.component';
import { PostListContentComponent } from './posts/post-list-content/post-list-content.component';
import { YourCommentsContentComponent } from './comments/your-comments-content/your-comments-content.component';
import { AllPostListContentComponent } from './allPosts/allPost-list-content/allPost-list-content.component';
import { GroupsListContentComponent } from './groups/groups-list-content/groups-list-content.component';

@NgModule({
  declarations: [
    AppComponent,
    CardComponent,
    HomeComponent,
    HeaderComponent,
    UserMenuComponent,
    LoginComponent,
    SignUpComponent,
    AddPostComponent,
    PostListComponent,
    YourCommentsComponent,
    YourCommentsContentComponent,
    AllPostListComponent,
    GroupsListContentComponent,
    GroupsListComponent,
    PostListContentComponent,
    CommentListComponent,
    AllPostListContentComponent,
  ],
  imports: [
    MatIconModule,
    MatToolbarModule,
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    NoopAnimationsModule,
    AngularMaterialModule,
    FormsModule,
    ReactiveFormsModule,
  ],
  providers: [ 
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    },
    ClubService,
    AuthService,
    ApiService,
    UserService,
    ConfigService,
    PostService,
    ComService,
    GroupService,
    CardComponent
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
