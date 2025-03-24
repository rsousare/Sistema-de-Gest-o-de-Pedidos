import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Product } from '../models/product';
import { HttpClient } from '@angular/common/http';
import { API_CONFIG } from '../config/api.config';

@Injectable({
  providedIn: 'root'
})
export class ProductService {


  constructor(private http: HttpClient) { }

  findById(id: any): Observable<Product> {
    return this.http.get<Product>(`${API_CONFIG.baseUrl}/products/${id}`)
  }

  findByCategoryId(categoryId: number): Observable<Product[]> {
    return this.http.get<Product[]>(`${API_CONFIG.baseUrl}/products/by-category?categoryId=${categoryId}`)
  }

  findAll(): Observable<Product[]> {
    return this.http.get<Product[]>(`${API_CONFIG.baseUrl}/products`)
  }

  create(product: Product): Observable<string> {
    return this.http.post<string>(`${API_CONFIG.baseUrl}/products`, product, {responseType: 'text' as 'json'})
  }

  update(product: Product): Observable<string> {
    return this.http.put<string>(`${API_CONFIG.baseUrl}/products/${product.id}`, product, {responseType: 'text' as 'json'})
  }

  delete(id: any): Observable<string> {
    return this.http.delete<string>(`${API_CONFIG.baseUrl}/products/${id}`, {responseType: 'text' as 'json'})
  }

  updateStock(productId: number, newStock: number): Observable<string> {
    return this.http.put<string>(`${API_CONFIG.baseUrl}/products/${productId}/update-stock?newStock=${newStock}`, {stock: newStock}, {responseType: 'text' as 'json'})
  }

  searchProductsPaginate(thePage: number,
    thePageSize: number,
    theKeyWord: string): Observable<Product> {

    const searchUrl = `${API_CONFIG.baseUrl}/search/findByNameContaining?name=${theKeyWord}`
      + `&page=${thePage}&size=${thePageSize}`
    return this.http.get<Product>(searchUrl)
  }

  getProductListPaginate(thePage: number,
    thePageSize: number,
    theCategoryId: number): Observable<Product> {

    const searchUrl = `${API_CONFIG.baseUrl}/search/findByCategoryId?id=${theCategoryId}`
      + `&page=${thePage}&size=${thePageSize}`
    return this.http.get<Product>(searchUrl)
  }
}
