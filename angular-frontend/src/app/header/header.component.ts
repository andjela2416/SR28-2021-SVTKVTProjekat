import { Component, OnInit } from '@angular/core';
import {UserService} from '../service/user.service';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  searchKeyword: string = '';
  searchResults: any = { users: [], groups: [],usersByFirstNameOrLastName:[] };

  constructor( private userService: UserService,
  private cdr: ChangeDetectorRef
  ) { }

  ngOnInit() {
  }
  
	search() {
	    if (this.searchKeyword.trim() !== '') {
			console.log(this.searchKeyword);
			if (this.searchKeyword.includes(' ')) {
	        	const [firstName, lastName] = this.searchKeyword.split(' ');
		        this.userService.search(this.searchKeyword,firstName,lastName).subscribe((result) => {
		            this.searchResults = result;
		            console.log(this.searchResults);
		            this.cdr.detectChanges();
		        });}
		     else {
				this.userService.search(this.searchKeyword).subscribe((result) => {
	            this.searchResults = result;
	            console.log(this.searchResults);
	            this.cdr.detectChanges();
	        });
		}
	        
	    } else {
	        this.searchResults = {
	            users: [],
	            groups: [],
	        };
	        this.cdr.detectChanges();
	    }
	}
	
	hideSearchResults(){
		this.searchKeyword="";
		this.searchResults = {
	            users: [],
	            groups: [],
	        };
	        this.cdr.detectChanges();
	}

  hasSignedIn() {
    return !!this.userService.currentUser;
  }

  userName() {
    const user = this.userService.currentUser;
    return user.username;
  }
  

}
