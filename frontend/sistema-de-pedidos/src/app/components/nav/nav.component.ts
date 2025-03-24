import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrl: './nav.component.css'
})
export class NavComponent implements OnInit {

  logged?: string
  isAdmin: boolean = false
  isUser: boolean = false

  constructor(private router: Router,
              private authService: AuthService,
              private toast: ToastrService
  ) {}

  ngOnInit(): void {
    this.logged = this.authService.loggedUser()

    this.login()
  }

  login() {
    const roles = this.authService.getRoles()
    if(roles && roles.includes('ROLE_ADMIN')) {
      this.isAdmin = true
    }
    if(roles && roles.includes('ROLE_USER')) {
      this.isUser = true
    }
  }

  logout() {
    this.router.navigate(['login'])
    this.authService.logout()
    this.toast.info('Logout Successfully Completed', 'Logout', {
      timeOut: 4000
    })
  }
}
