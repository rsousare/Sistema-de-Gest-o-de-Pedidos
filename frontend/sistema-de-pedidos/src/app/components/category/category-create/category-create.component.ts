import { Category } from './../../../models/category';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CategoryService } from '../../../services/category.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { ErrorHandlerService } from '../../../exceptions/ErrorHandlerService ';

@Component({
  selector: 'app-category-create',
  templateUrl: './category-create.component.html',
  styleUrl: './category-create.component.css'
})
export class CategoryCreateComponent implements OnInit {

  selectetFile: File | null = null
  categoryForm: FormGroup

  constructor(private fb: FormBuilder,
    private service: CategoryService,
    private toast: ToastrService,
    private router: Router,
    private errorHandler: ErrorHandlerService
  ) {}


  ngOnInit(): void {
      this.categoryForm = this.fb.group({
        categoryName: ['', Validators.required]
      })
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement
    if(input.files && input.files.length > 0) {
      this.selectetFile = input.files[0]
    }
  }

  create(): void {
    if(this.categoryForm.valid && this.selectetFile) {
      const categoryName = this.categoryForm.value.categoryName
      this.service.create(categoryName, this.selectetFile).subscribe({
        next: response=> {
          this.toast.success('Category created successfully')
          this.router.navigate(['category'])
        },
        error: (err)=> {
          this.errorHandler.handleError(err)
        }
      })
    } else {
      this.toast.warning('Invalid form data')
    }
  }


  validFields(): boolean {
    return this.categoryForm.valid && this.selectetFile != null
  }
}
