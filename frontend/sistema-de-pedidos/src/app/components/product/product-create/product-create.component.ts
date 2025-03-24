import { Component } from '@angular/core';
import { Product } from '../../../models/product';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProductService } from '../../../services/product.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { ErrorHandlerService } from '../../../exceptions/ErrorHandlerService ';

@Component({
  selector: 'app-product-create',
  templateUrl: './product-create.component.html',
  styleUrl: './product-create.component.css'
})
export class ProductCreateComponent {

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

      create(): void {
        if (this.productForm.valid) {
          const formValues = this.productForm.value
          this.product = {
            name: formValues.name,
            description: formValues.description,
            price: formValues.price,
            stock: formValues.stock,
            categoryId: formValues.categoryId,
            created: formValues.created
          };
          this.service.create(this.product).subscribe({
            next: (message: string) => {
              this.toast.success('Product created sucssessfully');
              this.router.navigate(['product']);
            },
              error: (error)=> {
                this.errorHandler.handleError(error)
              }
          })
        }
      }


      validFields(): boolean {
        return this.productForm.valid
      }
}
