import { Component } from '@angular/core';
import { Order } from '../../../models/order';
import { FormBuilder, FormGroup } from '@angular/forms';
import { OrderService } from '../../../services/order.service';
import { ToastrService } from 'ngx-toastr';
import { ActivatedRoute, Router } from '@angular/router';
import { ErrorHandlerService } from '../../../exceptions/ErrorHandlerService ';
import { Status } from '../../../models/status.enum';

@Component({
  selector: 'app-order-delete',
  templateUrl: './order-delete.component.html',
  styleUrl: './order-delete.component.css'
})
export class OrderDeleteComponent {
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
    private errorHandler: ErrorHandlerService,
    private route: ActivatedRoute
  ) {


    this.orderForm = this.fb.group({
      status: [{ value: '', disabled: true }],
      totalPrice: [{ value: '', disabled: true }],
      created: [{ value: '', disabled: true }],
      clientId: [{ value: '', disabled: true }]
    })
  }


  ngOnInit(): void {
    this.order.id = this.route.snapshot.paramMap.get('id')
    this.findById()
  }

  findById(): void {
    this.service.findById(this.order.id).subscribe(response => {
      this.order = response

      this.orderForm.patchValue({
        status: this.order.status,
        totalPrice: this.order.totalPrice,
        created: this.order.created,
        clientId: this.order.clientId
      })
    })
  }


  delete(): void {
    this.service.delete(this.order.id).subscribe({
      next: (message: string) => {
        this.toast.success("Order deleted successfully");
        this.router.navigate(['order']);
      },
      error: (error) => {
        this.errorHandler.handleError(error)
      }
    });
  }
}
