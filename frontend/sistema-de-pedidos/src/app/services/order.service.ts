import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Order } from '../models/order';
import { API_CONFIG } from '../config/api.config';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  constructor(private http: HttpClient) { }

  findById(id: any): Observable<Order> {
    return this.http.get<Order>(`${API_CONFIG.baseUrl}/orders/${id}`)
  }

  findAll(): Observable<Order[]> {
    return this.http.get<Order[]>(`${API_CONFIG.baseUrl}/orders`)
  }

  create(order: Order): Observable<string> {
    return this.http.post<string>(`${API_CONFIG.baseUrl}/orders`, order, {responseType: 'text' as 'json'})
  }

  updateStatus(orderId: number, newStatus: string): Observable<string> {
    return this.http.put<string>(`${API_CONFIG.baseUrl}/orders/${orderId}/update-status?newStatus=${newStatus}`, {status: newStatus}, {responseType: 'text' as 'json'})
  }

  delete(id: any): Observable<string> {
    return this.http.delete<string>(`${API_CONFIG.baseUrl}/orders/${id}`, {responseType: 'text' as 'json'})
  }
}
