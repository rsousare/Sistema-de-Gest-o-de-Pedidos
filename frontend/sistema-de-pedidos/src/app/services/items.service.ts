import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Item } from '../models/item';
import { API_CONFIG } from '../config/api.config';

@Injectable({
  providedIn: 'root'
})
export class ItemsService {

  constructor(private http: HttpClient) { }

  findById(id: any): Observable<Item> {
    return this.http.get<Item>(`${API_CONFIG.baseUrl}/items/${id}`)
  }

  findAll(): Observable<Item[]> {
    return this.http.get<Item[]>(`${API_CONFIG.baseUrl}/items`)
  }

  create(item: Item): Observable<string> {
    return this.http.post<string>(`${API_CONFIG.baseUrl}/items`, item, {responseType: 'text' as 'json'})
  }

  update(item: Item): Observable<string> {
    return this.http.put<string>(`${API_CONFIG.baseUrl}/items/${item.id}`, item, {responseType: 'text' as 'json'})
  }

  delete(id: any): Observable<string> {
    return this.http.delete<string>(`${API_CONFIG.baseUrl}/items/${id}`, {responseType: 'text' as 'json'})
  }

  returnProductToStock(id: any): Observable<string> {
    return this.http.delete<string>(`${API_CONFIG.baseUrl}/items/stock/${id}`, {responseType: 'text' as 'json'})
  }
}
