import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReturnProductToStockComponent } from './return-product-to-stock.component';

describe('ReturnProductToStockComponent', () => {
  let component: ReturnProductToStockComponent;
  let fixture: ComponentFixture<ReturnProductToStockComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ReturnProductToStockComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReturnProductToStockComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
