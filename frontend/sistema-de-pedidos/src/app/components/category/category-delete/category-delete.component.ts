import { Component, OnInit } from '@angular/core';
import { Category } from '../../../models/category';
import { FormBuilder, FormGroup } from '@angular/forms';
import { CategoryService } from '../../../services/category.service';
import { ToastrService } from 'ngx-toastr';
import { ActivatedRoute, Router } from '@angular/router';
import { ErrorHandlerService } from '../../../exceptions/ErrorHandlerService ';

@Component({
  selector: 'app-category-delete',
  templateUrl: './category-delete.component.html',
  styleUrl: './category-delete.component.css'
})
export class CategoryDeleteComponent implements OnInit {

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
        categoryName: [{value: '', disabled: true}],
        iconImage: [{value: '', disabled: true}]
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


  delete(): void {
      this.service.delete(this.category.id).subscribe({
        next: response=> {
          this.toast.success('Category deleted successfully')
          this.router.navigate(['category'])
        },
        error: (err)=> {
          this.errorHandler.handleError(err)
        }
      })
  }
}
