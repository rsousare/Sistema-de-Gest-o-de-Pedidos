import { Component } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ClientService } from '../../../services/client.service';
import { Client } from '../../../models/clients';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { ErrorHandlerService } from '../../../exceptions/ErrorHandlerService ';

@Component({
  selector: 'app-client-create',
  templateUrl: './client-create.component.html',
  styleUrl: './client-create.component.css'
})
export class ClientCreateComponent {

  client: Client = {
    id: '',
    name: '',
    email: '',
    password: '',
    roles: [],
    created: ''
  }

  clientForm: FormGroup

  constructor(private fb: FormBuilder,
              private service: ClientService,
              private toast: ToastrService,
              private router: Router,
              private errorHandler: ErrorHandlerService
  ) {

    this.clientForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(3)]]
    })
  }

  create(): void {
    if (this.clientForm.valid) {
      this.client = this.clientForm.value;
      this.service.create(this.client).subscribe({
        next: (message: string) => {
          this.toast.success(message);
          this.router.navigate(['client']);
        },
          error: (error)=> {
            this.errorHandler.handleError(error)
          }
      })
    }
  }

  addPerfil(perfil: any): void {
    if(this.client.roles.includes(perfil)) {
      this.client.roles.slice(this.client.roles.indexOf(perfil), 1)
    }else {
      this.client.roles.push(perfil)
    }
  }


  validFields(): boolean {
    return this.clientForm.valid
  }

}
