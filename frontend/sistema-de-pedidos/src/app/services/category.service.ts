import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Category } from '../models/category';
import { API_CONFIG } from '../config/api.config';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  constructor(private http: HttpClient) { }

  findById(id: number): Observable<Category> {
    return this.http.get<Category>(`${API_CONFIG.baseUrl}/categories/${id}`)
  }

  findAll(): Observable<Category[]> {
    return this.http.get<Category[]>(`${API_CONFIG.baseUrl}/categories`)
  }

  create(categoryName: string, iconImage: File): Observable<Category> {
    const formData = new FormData()
    formData.append('categoryName', categoryName)
    formData.append('iconImage', iconImage)
    return this.http.post<Category>(`${API_CONFIG.baseUrl}/categories`, formData)
  }

  update(category: Category, categoryName: string, iconImage?: File): Observable<Category> {
    const formData = new FormData()
    formData.append('categoryName', categoryName)
    if(iconImage) {
    formData.append('iconImage', iconImage)
    }
    return this.http.put<Category>(`${API_CONFIG.baseUrl}/categories/${category.id}`, formData)
  }

  delete(id: any): Observable<string> {
    return this.http.delete<string>(`${API_CONFIG.baseUrl}/categories/${id}`, {responseType: 'text' as 'json'})
  }
}
