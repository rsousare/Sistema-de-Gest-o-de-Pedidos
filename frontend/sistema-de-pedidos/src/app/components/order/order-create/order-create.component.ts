import { Component } from '@angular/core';
import { Order } from '../../../models/order';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { OrderService } from '../../../services/order.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { ErrorHandlerService } from '../../../exceptions/ErrorHandlerService ';
import { Status } from '../../../models/status.enum';

@Component({
  selector: 'app-order-create',
  templateUrl: './order-create.component.html',
  styleUrl: './order-create.component.css'
})
export class OrderCreateComponent {

  order: Order = {
    id: '',
    status: Status.PENDING,
    totalPrice: 0,
    created: new Date(),
    client: { id: null },
    clientId: ''
  }

  orderForm: FormGroup

  constructor(private fb: FormBuilder,
    private service: OrderService,
    private toast: ToastrService,
    private router: Router,
    private errorHandler: ErrorHandlerService
  ) {

    this.orderForm = this.fb.group({
      clientId: ['', [Validators.required]]
    })
  }

  create(): void {
    if (this.orderForm.valid) {
      this.order = this.orderForm.value
      this.service.create(this.order).subscribe({
        next: (message: string) => {
          this.toast.success(message);
          this.router.navigate(['order']);
        },
        error: (error) => {
          this.errorHandler.handleError(error)
        }
      })
    }
  }


  validFields(): boolean {
    return this.orderForm.valid
  }
}
