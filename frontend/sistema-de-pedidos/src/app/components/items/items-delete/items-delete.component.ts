import { Component } from '@angular/core';
import { Item } from '../../../models/item';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ItemsService } from '../../../services/items.service';
import { ToastrService } from 'ngx-toastr';
import { ActivatedRoute, Router } from '@angular/router';
import { ErrorHandlerService } from '../../../exceptions/ErrorHandlerService ';

@Component({
  selector: 'app-items-delete',
  templateUrl: './items-delete.component.html',
  styleUrl: './items-delete.component.css'
})
export class ItemsDeleteComponent {
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
    private errorHandler: ErrorHandlerService,
    private route: ActivatedRoute
  ) {

    this.itemForm = this.fb.group({
      orderId: [{value: '', disabled: true}],
      quantity: [{value: '', disabled: true}],
      price: [{value: '', disabled: true}],
      productId: [{value: '', disabled: true}]
    })
  }

  ngOnInit(): void {
    this.item.id = this.route.snapshot.paramMap.get('id')
    this.findById()
  }

  findById(): void {
    this.service.findById(this.item.id).subscribe(response=> {
      this.item = response

      this.itemForm.patchValue({
        orderId: this.item.orderId,
        quantity: this.item.quantity,
        price: this.item.price,
        productId: this.item.productId
      })
    })
  }

  delete(): void {
    this.service.delete(this.item.id).subscribe({
      next: (message: string) => {
        this.toast.success('Order Item deleted successfully');
        this.router.navigate(['item']);
      },
      error: (error) => {
        this.errorHandler.handleError(error)
      }
    })
  }
}
