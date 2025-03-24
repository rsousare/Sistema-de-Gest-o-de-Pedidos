import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {

  logged?: string

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.logged = this.authService.loggedUser()
  }
}
