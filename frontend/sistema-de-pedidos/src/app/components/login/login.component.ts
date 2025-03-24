import { Component } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { Credentials } from '../../models/credentials';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  creds: Credentials = {
    email: '',
    password: ''
  }


  email = new FormControl(null, Validators.email)
  password = new FormControl(null, Validators.minLength(3))

  constructor(private toast: ToastrService,
              private service: AuthService,
              private router: Router) {}

  login() {
    this.service.authenticate(this.creds).subscribe(response=> {
      this.service.successfullLogin(response.headers.get('Authorization').substring(7))
      this.router.navigate(['home'])
    }, ()=> {
      this.toast.error('Invalid username and/or password')
    })
  }

  validationFields(): boolean {
    return this.email.valid && this.password.valid
  }

  register(event: any) {
    event.preventDefault()
    this.router.navigate(['register'])
  }

}
