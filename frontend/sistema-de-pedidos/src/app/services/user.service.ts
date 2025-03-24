import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../models/user';
import { API_CONFIG } from '../config/api.config';
import { Client } from '../models/clients';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  findById(id: any): Observable<User> {
    return this.http.get<User>(`${API_CONFIG.baseUrl}/users/${id}`)
  }

  findAll(): Observable<User[]> {
    return this.http.get<User[]>(`${API_CONFIG.baseUrl}/users`)
  }

  create(user: User): Observable<string> {
    return this.http.post<string>(`${API_CONFIG.baseUrl}/users`, user, {responseType: 'text' as 'json'})
  }

  update(user: User): Observable<string> {
    return this.http.put<string>(`${API_CONFIG.baseUrl}/users/${user.id}`, user, {responseType: 'text' as 'json'})
  }

  delete(id: any): Observable<string> {
    return this.http.delete<string>(`${API_CONFIG.baseUrl}/users/${id}`, {responseType: 'text' as 'json'})
  }
}
