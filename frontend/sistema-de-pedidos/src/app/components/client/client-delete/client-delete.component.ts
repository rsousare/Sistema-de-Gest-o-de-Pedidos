import { Component, OnInit } from '@angular/core';
import { Client } from '../../../models/clients';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ClientService } from '../../../services/client.service';
import { ToastrService } from 'ngx-toastr';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { ErrorHandlerService } from '../../../exceptions/ErrorHandlerService ';

@Component({
  selector: 'app-client-delete',
  templateUrl: './client-delete.component.html',
  styleUrl: './client-delete.component.css'
})
export class ClientDeleteComponent implements OnInit{
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
          name: [{value: '', disabled: true}],
          email: [{value: '', disabled: true}],
          password: [{value: '', disabled: true}]
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

    delete(): void {
        this.service.delete(this.client.id).subscribe({
          next: (message: string) => {
            this.toast.success('Customer deleted successfully');
            this.router.navigate(['client']);
          },
          error: (error) => {
            this.errorHandler.handleError(error)
          }
        })
    }
}
