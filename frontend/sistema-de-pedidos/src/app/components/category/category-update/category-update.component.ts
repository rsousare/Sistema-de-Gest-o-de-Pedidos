import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CategoryService } from '../../../services/category.service';
import { ToastrService } from 'ngx-toastr';
import { ActivatedRoute, Router } from '@angular/router';
import { ErrorHandlerService } from '../../../exceptions/ErrorHandlerService ';
import { Category } from '../../../models/category';

@Component({
  selector: 'app-category-update',
  templateUrl: './category-update.component.html',
  styleUrl: './category-update.component.css'
})
export class CategoryUpdateComponent implements OnInit {

  category: Category = {
    id: '',
    categoryName: '',
    iconImage: ''
  }

selectetFile: File | null = null
categoryForm: FormGroup
existingFilePath: string | null = null

  constructor(private fb: FormBuilder,
    private service: CategoryService,
    private toast: ToastrService,
    private router: Router,
    private route: ActivatedRoute,
    private errorHandler: ErrorHandlerService
  ) {}


  ngOnInit(): void {
      this.categoryForm = this.fb.group({
        categoryName: ['', Validators.required]
      })
      this.category.id = this.route.snapshot.paramMap.get('id')
      this.findById()
  }

  findById(): void {
    this.service.findById(this.category.id).subscribe({
      next: response=> {
      this.category = response

      this.categoryForm.patchValue({
        categoryName: this.category.categoryName
      })
      this.existingFilePath = this.category.iconImage
    },
    error: err=> this.errorHandler.handleError(err)
    })
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement
    if(input.files && input.files.length > 0) {
      this.selectetFile = input.files[0]
    }
  }

  update(): void {
    if(this.categoryForm.valid) {
      const categoryName = this.categoryForm.value.categoryName
      this.service.update(this.category, categoryName, this.selectetFile).subscribe({
        next: response=> {
          this.toast.success('Category updated successfully')
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
    return this.categoryForm.valid
  }

}
