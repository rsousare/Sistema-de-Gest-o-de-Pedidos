import { Component, OnInit } from '@angular/core';
import { User } from '../../../models/user';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from '../../../services/user.service';
import { ToastrService } from 'ngx-toastr';
import { ActivatedRoute, Router } from '@angular/router';
import { ErrorHandlerService } from '../../../exceptions/ErrorHandlerService ';
import { formatDate } from '@angular/common';

@Component({
  selector: 'app-user-update',
  templateUrl: './user-update.component.html',
  styleUrl: './user-update.component.css'
})
export class UserUpdateComponent implements OnInit {
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
      address: ['', [Validators.required]],
      phone: ['', [Validators.required]],
      birthDate: ['', [Validators.required]],
      clientId: ['', [Validators.required]]
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


  update(): void {
    if (this.userForm.valid) {
      const updatedUser = {...this.user, ...this.userForm.value}
      this.service.update(updatedUser).subscribe({
        next: (message: string) => {
          this.toast.success("User updated successfully");
          this.router.navigate(['user']);
        },
        error: (error) => {
          this.errorHandler.handleError(error)
        }
      });
    }
  }


  validFields(): boolean {
    return this.userForm.valid
  }
}
