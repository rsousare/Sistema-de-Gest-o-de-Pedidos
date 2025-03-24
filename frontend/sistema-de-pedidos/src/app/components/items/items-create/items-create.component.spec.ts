import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ItemsCreateComponent } from './items-create.component';

describe('ItemsCreateComponent', () => {
  let component: ItemsCreateComponent;
  let fixture: ComponentFixture<ItemsCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ItemsCreateComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ItemsCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
