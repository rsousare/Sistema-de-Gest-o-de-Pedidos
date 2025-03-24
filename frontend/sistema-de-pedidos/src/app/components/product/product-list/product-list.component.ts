import { Component, ViewChild } from '@angular/core';
import { Product } from '../../../models/product';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { ProductService } from '../../../services/product.service';
import { ToastrService } from 'ngx-toastr';
import { ErrorHandlerService } from '../../../exceptions/ErrorHandlerService ';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrl: './product-list.component.css'
})
export class ProductListComponent {
  ELEMENT_DATA: Product[] = []

    updateProductId: number
    updateQuantity: number

      displayedColumns: string[] = ['id', 'name', 'description', 'price', 'stock', 'category', 'date', 'actions'];
      dataSource = new MatTableDataSource<Product>(this.ELEMENT_DATA);

      @ViewChild(MatPaginator) paginator: MatPaginator;

      constructor(private service: ProductService,
                  private toast: ToastrService,
                  private errorHandler: ErrorHandlerService
      ) {}

      ngOnInit(): void {
        this.findAll()
      }


      findAll() {
          this.service.findAll().subscribe(response => {
            if (response && response.length > 0) {
              this.ELEMENT_DATA = response;
              this.dataSource = new MatTableDataSource<Product>(response);
              this.dataSource.paginator = this.paginator;
            } else {
              this.toast.info('Empty List');
              this.ELEMENT_DATA = [];
              this.dataSource = new MatTableDataSource<Product>([]);
            }
          }, error => {
            this.errorHandler.handleError(error)
          });
        }

      updateStock(productId: number, newStock: number): void {
        this.service.updateStock(productId, newStock).subscribe({
          next: (message: string)=> {
            this.toast.success(message)
            this.findAll()
            this.updateProductId = null
            this.updateQuantity = null
          },
          error: (error)=> this.errorHandler.handleError(error)
        })
      }

      applyFilter(event: Event) {
        const filterValue = (event.target as HTMLInputElement).value;
        this.dataSource.filter = filterValue.trim().toLowerCase();
      }

      validFields(): boolean {
        return !!this.updateProductId && !!this.updateQuantity
      }
}
