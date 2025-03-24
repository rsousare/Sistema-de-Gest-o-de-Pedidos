import { Component } from '@angular/core';
import { Item } from '../../../models/item';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ItemsService } from '../../../services/items.service';
import { ToastrService } from 'ngx-toastr';
import { ActivatedRoute, Router } from '@angular/router';
import { ErrorHandlerService } from '../../../exceptions/ErrorHandlerService ';

@Component({
  selector: 'app-items-update',
  templateUrl: './items-update.component.html',
  styleUrl: './items-update.component.css'
})
export class ItemsUpdateComponent {
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
      quantity: ['', [Validators.required]]
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

  update(): void {
    if (this.itemForm.valid) {
      const updatedProduct = {...this.item, ...this.itemForm.value}
      this.service.update(updatedProduct).subscribe({
        next: (message: string) => {
          this.toast.success('Order Item updated successfully');
          this.router.navigate(['item']);
        },
        error: (error) => {
          this.errorHandler.handleError(error)
        }
      })
    }
  }

  validFields(): boolean {
    return this.itemForm.valid
  }
}
