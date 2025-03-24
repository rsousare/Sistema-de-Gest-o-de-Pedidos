import { Component, OnInit } from '@angular/core';
import { User } from '../../../models/user';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from '../../../services/user.service';
import { ToastrService } from 'ngx-toastr';
import { ActivatedRoute, Router } from '@angular/router';
import { ErrorHandlerService } from '../../../exceptions/ErrorHandlerService ';

@Component({
  selector: 'app-user-delete',
  templateUrl: './user-delete.component.html',
  styleUrl: './user-delete.component.css'
})
export class UserDeleteComponent implements OnInit{
  user: User = {
      id: '',
      address: '',
      phone: '',
      birthDate: '',
      clientId: '',
      client: { id: null }
    }

    userForm: FormGroup

    constructor(private fb: FormBuilder,
      private service: UserService,
      private toast: ToastrService,
      private router: Router,
      private errorHandler: ErrorHandlerService,
      private route: ActivatedRoute
    ) {


      this.userForm = this.fb.group({
      address: [{value: '', disabled: true}],
      phone: [{value: '', disabled: true}],
      birthDate: [{value: '', disabled: true}],
      clientId: [{value: '', disabled: true}]
      })
    }


    ngOnInit(): void {
      this.user.id = this.route.snapshot.paramMap.get('id')
      this.findById()
    }

    findById(): void {
      this.service.findById(this.user.id).subscribe(response => {
        this.user = response

        this.userForm.patchValue({
          address: this.user.address,
          phone: this.user.phone,
          birthDate: this.user.birthDate,
          clientId: this.user.clientId
        })
      })
    }


    delete(): void {
        this.service.delete(this.user.id).subscribe({
          next: (message: string) => {
            this.toast.success("User deleted successfully");
            this.router.navigate(['user']);
          },
          error: (error) => {
            this.errorHandler.handleError(error)
          }
        });
    }

    deleteMessage(): void {
      //alert('Action disabled! You will have to delete the customer! ')
      this.toast.warning('Action disabled! You will have to delete the customer! ')
    }
}
