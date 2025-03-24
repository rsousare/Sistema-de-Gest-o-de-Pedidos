import { Product } from "./product"

export class CartItem {
  id: number
  name: string
  description: string
  unitPrice: number

  quantity: number

  constructor(product: Product) {
    this.id = product.id
    this.name = product.name
    this.description = product.description
    this.unitPrice = product.price

    this.quantity = 1
  }
}
