import { Directive, ElementRef, HostListener } from "@angular/core";

@Directive({
  selector: '[appPhoneMask]'
})

export class PhoneMaskDirective {

  constructor(private el: ElementRef) {}

  @HostListener('input', ['$event']) onInputChange(event: InputEvent) {
    const input = this.el.nativeElement
    let trimmed = input.value.replace(/\s+/g, '')
    if(trimmed.length > 9) {
      trimmed = trimmed.substr(0,8)
    }
    const formatted = trimmed.replace(/(\d{3})(\d{3})(\d{3})/, '$1 $2 $3')
    input.value = formatted
  }
}
