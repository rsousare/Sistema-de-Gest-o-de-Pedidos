import { Component, OnInit, ViewChild } from '@angular/core';
import { Client } from '../../../models/clients';
import { ClientService } from '../../../services/client.service';
import { HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { ErrorHandlerService } from '../../../exceptions/ErrorHandlerService ';

@Component({
  selector: 'app-client-update',
  templateUrl: './client-update.component.html',
  styleUrl: './client-update.component.css'
})
export class ClientUpdateComponent implements OnInit{
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
                private route: ActivatedRoute,
                private errorHandler: ErrorHandlerService
    ) {

      this.clientForm = this.fb.group({
        name: ['', [Validators.required, Validators.minLength(3)]],
        email: ['', [Validators.required, Validators.email]],
        password: ['', [Validators.required, Validators.minLength(3)]]
      })
    }


  ngOnInit(): void {
    this.client.id = this.route.snapshot.paramMap.get('id')
    this.findById()
  }


  findById(): void {
    this.service.findById(this.client.id).subscribe(response=> {
      response.roles = []
      this.client = response

      this.clientForm.patchValue({
        name: this.client.name,
        email: this.client.email,
        password: this.client.password
      })
    })
  }

  update(): void {
    if (this.clientForm.valid) {
      const updatedClient = { ...this.client, ...this.clientForm.value }
      this.service.update(updatedClient).subscribe({
        next: (message: string) => {
          this.toast.success('Customer updated successfully');
          this.router.navigate(['client']);
        },
        error: (error) => {
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
