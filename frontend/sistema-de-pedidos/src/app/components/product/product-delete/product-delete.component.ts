import { Component, OnInit } from '@angular/core';
import { Product } from '../../../models/product';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProductService } from '../../../services/product.service';
import { ToastrService } from 'ngx-toastr';
import { ActivatedRoute, Router } from '@angular/router';
import { ErrorHandlerService } from '../../../exceptions/ErrorHandlerService ';

@Component({
  selector: 'app-product-delete',
  templateUrl: './product-delete.component.html',
  styleUrl: './product-delete.component.css'
})
export class ProductDeleteComponent implements OnInit{
product: Product = {
    id: '',
    name: '',
    description: '',
    price: 0,
    stock: 0,
    categoryId: '',
    created: ''
  }

  productForm: FormGroup

  constructor(private fb: FormBuilder,
    private service: ProductService,
    private toast: ToastrService,
    private router: Router,
    private route: ActivatedRoute,
    private errorHandler: ErrorHandlerService
  ) {

    this.productForm = this.fb.group({
      name: [{value: '', disabled: true}],
      description: [{value: '', disabled: true}],
      price: [{value: '', disabled: true}],
      stock: [{value: '', disabled: true}],
      categoryId: [{value: '', disabled: true}]
    })
  }


  ngOnInit(): void {
    this.product.id = this.route.snapshot.paramMap.get('id')
    this.findById()
  }

  findById(): void {
    this.service.findById(this.product.id).subscribe(response=> {
      this.product = response

      this.productForm.patchValue({
        name: this.product.name,
        description: this.product.description,
        price: this.product.price,
        stock: this.product.stock,
        categoryId: this.product.categoryId
      })
    })
  }

  delete(): void {
      this.service.delete(this.product.id).subscribe({
        next: (message: string) => {
          this.toast.success('Product deleted successfully');
          this.router.navigate(['product']);
        },
        error: (error) => {
          this.errorHandler.handleError(error)
        }
      })
  }
}
