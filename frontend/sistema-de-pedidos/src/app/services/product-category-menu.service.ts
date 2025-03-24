import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Category } from '../models/category';
import { map, Observable } from 'rxjs';
import { API_CONFIG } from '../config/api.config';

@Injectable({
  providedIn: 'root'
})
export class ProductCategoryMenuService {


  constructor(private http: HttpClient) { }


  findAll(): Observable<Category[]> {
    return this.http.get<Category[]>(`${API_CONFIG.baseUrl}/categories`).pipe(
      map((categories: Category[]) => {
        return categories.map(category => {

          category.iconImage = `${API_CONFIG.baseUrl}${category.iconImage}`;
          return category;
        });
      })
    );
  }

}
