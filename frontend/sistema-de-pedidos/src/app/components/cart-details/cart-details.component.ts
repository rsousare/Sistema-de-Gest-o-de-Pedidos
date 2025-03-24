import { Component, OnInit } from '@angular/core';
import { CartItem } from '../../models/cart-item';
import { CartService } from '../../services/cart.service';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-cart-details',
  templateUrl: './cart-details.component.html',
  styleUrl: './cart-details.component.css'
})
export class CartDetailsComponent implements OnInit {

  cartItems: CartItem[] = []
  totalPrice: number = 0
  totalQuantity: number = 0

  displayedColumns: string[] = ['name', 'description', 'controls', 'subtotal']
  dataSource = new MatTableDataSource<CartItem>(this.cartItems)

  constructor(private cartService: CartService) {}

  ngOnInit(): void {
      this.listCartDetails()
  }

  listCartDetails() {
    this.cartItems = this.cartService.cartItems

    this.dataSource = new MatTableDataSource(this.cartItems)

    this.cartService.totalPrice.subscribe(
      data=> this.totalPrice = data
    )

    this.cartService.totalQuantity.subscribe(
      data=> this.totalQuantity = data
    )

    this.cartService.computeCartTotals()
  }

  incrementQuantity(theCartItem: CartItem) {
    this.cartService.addToCart(theCartItem)
  }

  decrementQuantity(theCartItem: CartItem) {
    this.cartService.decrementQuantity(theCartItem)
    this.listCartDetails()
  }

  remove(theCartItem: CartItem) {
    this.cartService.remove(theCartItem)
    this.listCartDetails()
  }

}
