import { Component, OnInit } from '@angular/core';
import { Product } from '../../../models/product';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProductService } from '../../../services/product.service';
import { ToastrService } from 'ngx-toastr';
import { ActivatedRoute, Router } from '@angular/router';
import { ErrorHandlerService } from '../../../exceptions/ErrorHandlerService ';

@Component({
  selector: 'app-product-update',
  templateUrl: './product-update.component.html',
  styleUrl: './product-update.component.css'
})
export class ProductUpdateComponent implements OnInit{

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
      name: ['', [Validators.required]],
      description: ['', [Validators.required]],
      price: ['', [Validators.required]],
      stock: ['', [Validators.required]],
      categoryId: ['', [Validators.required]]
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

  update(): void {
    if (this.productForm.valid) {
      const updatedProduct = {...this.product, ...this.productForm.value}
      this.service.update(updatedProduct).subscribe({
        next: (message: string) => {
          this.toast.success('Product updated successfully');
          this.router.navigate(['product']);
        },
        error: (error) => {
          this.errorHandler.handleError(error)
        }
      })
    }
  }



  validFields(): boolean {
    return this.productForm.valid
  }
}
