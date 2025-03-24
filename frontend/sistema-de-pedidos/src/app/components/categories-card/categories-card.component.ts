import { Component, Input, OnInit } from '@angular/core';
import { Category } from '../../models/category';
import { HttpClient } from '@angular/common/http';
import { Product } from '../../models/product';
import { Router } from '@angular/router';
import { ProductCategoryMenuService } from '../../services/product-category-menu.service';

@Component({
  selector: 'app-categories-card',
  templateUrl: './categories-card.component.html',
  styleUrl: './categories-card.component.css'
})
export class CategoriesCardComponent implements OnInit{

  @Input()
  category: Category

  products: Product[] = []

  categories: Category[] = []

  constructor(private router: Router
  ) {}


  onCardViewed(categoryId: number): void {
    this.router.navigate(['/category-products', categoryId])
  }

  ngOnInit(): void {
  }

}
