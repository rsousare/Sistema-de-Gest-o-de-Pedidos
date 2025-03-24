import { Component, OnInit } from '@angular/core';
import { Client } from '../../models/clients';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ClientService } from '../../services/client.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent implements OnInit {

  client: Client = {
    id: '',
    name: '',
    email: '',
    password: '',
    roles: [],
    created: ''
  }


  clientForm: FormGroup
  isAdmin: boolean = false
  isCodeValid: boolean = false

  constructor(private fb: FormBuilder,
    private service: ClientService,
    private toast: ToastrService,
    private router: Router
  ) {

    this.clientForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(3)]],
      accessCode: ['']
    })
  }


  ngOnInit(): void {
  }

  validateAccessCode() {

    const accessCode = this.clientForm.get('accessCode')?.value
      if (accessCode === 'ADMIN-123') {
        this.isCodeValid = true
        this.isAdmin = true
      } else {
        this.isCodeValid = false
        this.isAdmin = false
      }
  }


  create(): void {
    if (this.clientForm.valid) {
      this.client = this.clientForm.value;
      this.client.roles = this.isAdmin ? ['ROLE_ADMIN'] : ['ROLE_USER']
      //console.log('Dados enviados ', this.client)

      this.service.create(this.client).subscribe({
        next: (message: string) => {
          this.toast.success(message);
          this.router.navigate(['login']);
        },
        error: (ex: HttpErrorResponse) => {
          if (ex.status === 409) {
            this.toast.error(ex.error || 'This email is already registered');
          } else if (ex.status === 403) {
            this.toast.error('You do not have permission to perform this action');
          } else {
            this.toast.error('An unexpected error occurred');
          }
        }
      })
    }
  }

  validFields(): boolean {
    return this.clientForm.valid
  }
}
