import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Client } from '../models/clients';
import { API_CONFIG } from '../config/api.config';

@Injectable({
  providedIn: 'root'
})
export class ClientService {

  constructor(private http: HttpClient) { }

  findById(id: any): Observable<Client> {
    return this.http.get<Client>(`${API_CONFIG.baseUrl}/clients/${id}`)
  }

  findAll(): Observable<Client[]> {
    return this.http.get<Client[]>(`${API_CONFIG.baseUrl}/clients`)
  }

  create(client: Client): Observable<string> {
    const endpoint = client.roles.includes('ROLE_ADMIN')
    ? `${API_CONFIG.baseUrl}/auths`
    : `${API_CONFIG.baseUrl}/clients`
    return this.http.post<string>(endpoint, client, {responseType: 'text' as 'json'})
  }

  update(client: Client): Observable<string> {
    return this.http.put<string>(`${API_CONFIG.baseUrl}/clients/${client.id}`, client, {responseType: 'text' as 'json'})
  }

  delete(id: any): Observable<string> {
    return this.http.delete<string>(`${API_CONFIG.baseUrl}/clients/${id}`, {responseType: 'text' as 'json'})
  }
}
