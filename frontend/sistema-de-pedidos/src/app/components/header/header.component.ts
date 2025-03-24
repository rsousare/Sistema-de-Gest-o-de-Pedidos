import { Component, OnInit } from '@angular/core';
import { NavComponent } from '../nav/nav.component';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit {

  imagePath: string = 'assets/img/header.jpg';

  logged?: string
  isAdmin: boolean = false
  isUser: boolean = false

  constructor(private user: NavComponent,
              private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.logged = this.authService.loggedUser()
    this.user.login()
    this.isAdmin = this.user.isAdmin
    this.isUser = this.user.isUser
  }

}
