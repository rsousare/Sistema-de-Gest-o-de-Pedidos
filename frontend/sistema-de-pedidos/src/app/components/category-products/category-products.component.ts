import { Product } from './../../models/product';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductService } from '../../services/product.service';
import { PageEvent } from '@angular/material/paginator';
import { CartItem } from '../../models/cart-item';
import { CartService } from '../../services/cart.service';

@Component({
  selector: 'app-category-products',
  templateUrl: './category-products.component.html',
  styleUrl: './category-products.component.css'
})
export class CategoryProductsComponent implements OnInit {

products: Product[] = []
paginatedProducts: Product[] = []

  thePageNumber: number = 0
  thePageSize: number = 5
  theTotalElements: number = 0


constructor(private route: ActivatedRoute,
            private service: ProductService,
            private cartService: CartService
){}

  ngOnInit(): void {
    const categoryId = +this.route.snapshot.paramMap.get('id')!

    this.service.findByCategoryId(categoryId).subscribe((data) => {
      this.products = data || []
      this.theTotalElements = this.products.length
      this.updatePaginetedProducts()
    },
      (error)=> {
        console.error('Erro', error)
        this.products = []
      })
  }

  addToCart(theProduct: Product) {
    console.log(`Adding to cart: ${theProduct.name}, ${theProduct.price}`)

    const theCartItem = new CartItem(theProduct)

    this.cartService.addToCart(theCartItem)
    }



  onPageChange(event: PageEvent): void {
    this.thePageNumber = event.pageIndex
    this.thePageSize = event.pageSize
    this.updatePaginetedProducts()
  }

  updatePaginetedProducts(): void {
    const startIndex = this.thePageNumber * this.thePageSize
    const endIndex = startIndex + this.thePageSize

    this.paginatedProducts = this.products.slice(startIndex, endIndex)
  }
}
