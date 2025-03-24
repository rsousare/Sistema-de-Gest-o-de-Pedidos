import { Component } from '@angular/core';
import { User } from '../../../models/user';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from '../../../services/user.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { MAT_DATE_FORMATS, MAT_NATIVE_DATE_FORMATS } from '@angular/material/core';
import { formatDate } from '@angular/common';
import { ErrorHandlerService } from '../../../exceptions/ErrorHandlerService ';


@Component({
  selector: 'app-user-create',
  templateUrl: './user-create.component.html',
  styleUrl: './user-create.component.css',
  providers: [{provide: MAT_DATE_FORMATS, useValue: MAT_NATIVE_DATE_FORMATS}]
})
export class UserCreateComponent {
  user: User = {
      id: '',
      address: '',
      phone: '',
      birthDate: '',
      clientId: '',
      client: {id: null}
    }

    clientForm: FormGroup

    constructor(private fb: FormBuilder,
                private service: UserService,
                private toast: ToastrService,
                private router: Router,
                private errorHandler: ErrorHandlerService
    ) {

      this.clientForm = this.fb.group({
        address: ['', [Validators.required]],
        phone: ['', [Validators.required]],
        birthdate: ['', [Validators.required]],
        clientId: ['', [Validators.required]]
      })
    }

    create(): void {
      if (this.clientForm.valid) {
        const formValues = this.clientForm.value;
        this.user = {
          address: formValues.address,
          phone: formValues.phone,
          birthDate: formatDate(formValues.birthdate, 'dd/MM/yyyy', 'en-US'),
          clientId: parseInt(formValues.clientId, 10),
          client: { id: parseInt(formValues.clientId, 10) }
        };

        this.service.create(this.user).subscribe({
          next: (message: string) => {
            this.toast.success(message);
            this.router.navigate(['user']);
          },
            error: (error)=> {
              this.errorHandler.handleError(error)
            }
        });
      }
    }


    validFields(): boolean {
      return this.clientForm.valid
    }
}
