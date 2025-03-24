import { Directive, ElementRef, HostListener } from "@angular/core";

@Directive({
  selector: '[appBirthdateMask]'
})

export class BithdateMaskDirective {

  constructor(private el: ElementRef) {}

  @HostListener('input', ['$event']) onInputChange(event: InputEvent) {
    const input = this.el.nativeElement
    let trimmed = input.value.replace(/\D+/g, '')
    if(trimmed.length > 8) {
      trimmed = trimmed.substr(0, 8)
    }
    const formatted = trimmed.replace(/(\d{4})(\d{2})(\d{2})/, '$1/$2/$3')
    input.value = formatted
  }
}
