import { Component, OnInit } from '@angular/core';
import { Category } from '../../models/category';
import { CategoryService } from '../../services/category.service';
import { ProductCategoryMenuService } from '../../services/product-category-menu.service';
import { HttpClient } from '@angular/common/http';


@Component({
  selector: 'app-product-category-menu',
  templateUrl: './product-category-menu.component.html',
  styleUrl: './product-category-menu.component.css'
})
export class ProductCategoryMenuComponent implements OnInit {

  categories: Category[] = []

  pageSize = 4
  currentPage = 0

  constructor(private service: ProductCategoryMenuService,
              private http: HttpClient
  ) {}

  ngOnInit(): void {
    this.loadCategories()
  }

  getPaginatedCards() {
    const startIndex = this.currentPage * this.pageSize
    return this.categories.slice(startIndex, startIndex + this.pageSize)
  }

  onPageChange(page: number) {
    this.currentPage = page
  }

  loadCategories(): void {
    this.service.findAll().subscribe((categories)=> {
      this.categories = categories.map((category)=> {
        this.loadImage(category)
        return category
      })
    })
  }

  loadImage(category: Category): void {
    this.http.get(category.iconImage, {responseType: 'blob'}).subscribe((imageBlob)=> {
      category.iconImage = URL.createObjectURL(imageBlob)
    })
  }
}
