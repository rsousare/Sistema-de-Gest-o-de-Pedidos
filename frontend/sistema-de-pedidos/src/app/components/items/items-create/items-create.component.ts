import { Component } from '@angular/core';
import { Item } from '../../../models/item';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ItemsService } from '../../../services/items.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { ErrorHandlerService } from '../../../exceptions/ErrorHandlerService ';

@Component({
  selector: 'app-items-create',
  templateUrl: './items-create.component.html',
  styleUrl: './items-create.component.css'
})
export class ItemsCreateComponent {
  item: Item = {
    id: '',
    orderId: '',
    quantity: 0,
    price: 0,
    productId: '',
    product: { id: null }
  }

  itemForm: FormGroup

  constructor(private fb: FormBuilder,
    private service: ItemsService,
    private toast: ToastrService,
    private router: Router,
    private errorHandler: ErrorHandlerService
  ) {

    this.itemForm = this.fb.group({
      orderId: ['', [Validators.required]],
      quantity: ['', [Validators.required]],
      productId: ['', [Validators.required]]
    })
  }

  create(): void {
    if (this.itemForm.valid) {
      const formValues = this.itemForm.value;
      this.item = {
        orderId: formValues.orderId,
        quantity: formValues.quantity,
        price: formValues.price,
        productId: parseInt(formValues.productId, 10),
        product: { id: parseInt(formValues.productId, 10) }
      };

      this.service.create(this.item).subscribe({
        next: (message: string) => {
          this.toast.success('Order Item created successfully');
          this.router.navigate(['item']);
        },
        error: (error) => {
          this.errorHandler.handleError(error)
        }
      });
    }
  }


  validFields(): boolean {
    return this.itemForm.valid
  }
}
