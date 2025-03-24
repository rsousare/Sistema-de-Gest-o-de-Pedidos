import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ItemsDeleteComponent } from './items-delete.component';

describe('ItemsDeleteComponent', () => {
  let component: ItemsDeleteComponent;
  let fixture: ComponentFixture<ItemsDeleteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ItemsDeleteComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ItemsDeleteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
