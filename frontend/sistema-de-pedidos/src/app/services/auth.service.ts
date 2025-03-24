import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Credentials } from '../models/credentials';
import { API_CONFIG } from '../config/api.config';
import { JwtHelperService } from '@auth0/angular-jwt';


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  jwtService: JwtHelperService = new JwtHelperService()

  constructor(private http: HttpClient) { }

  authenticate(creds: Credentials) {
    return this.http.post(`${API_CONFIG.baseUrl}/login`, creds, {
      observe: 'response',
      responseType: 'text'
    })
  }

  successfullLogin(authToken: string) {
    localStorage.setItem('token', authToken)
  }

  isAuthenticated(): boolean {
    let token = localStorage.getItem('token')
    if(token != null) {
      return !this.jwtService.isTokenExpired(token)
    }
    return false
  }

  logout() {
    localStorage.clear()
  }

  loggedUser(): string | null {
    const token = localStorage.getItem('token')
    if(token != null) {
      const decoded = this.jwtService.decodeToken(token)
      //console.log('Decoded ', decoded)
      return decoded ? decoded.name : null
    }
    return null
  }

  getRoles(): string[] | null {
    let token = localStorage.getItem('token')
    if(token != null) {
      const decoded = this.jwtService.decodeToken(token)
      return decoded ? decoded.roles : null
    }
    return null
  }
}
