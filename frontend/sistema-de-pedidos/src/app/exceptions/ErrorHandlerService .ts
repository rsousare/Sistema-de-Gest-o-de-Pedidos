import { HttpErrorResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { ToastrService } from "ngx-toastr";
import { of } from "rxjs";

@Injectable({
  providedIn: 'root'
})

export class ErrorHandlerService {

  constructor(private toast: ToastrService) {}

  handleError(error: HttpErrorResponse): void {
    if(error.error && typeof error.error === 'object') {
      for(let [key, message] of Object.entries(error.error)) {
        this.toast.error(`${key}: ${message}`)
      }
    } else if(typeof error.error === 'string') {
      this.toast.error(error.error)
    } else {
      this.toast.error('An expected error occurred')
    }
  }
}
