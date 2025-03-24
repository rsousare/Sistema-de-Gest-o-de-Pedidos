import { Component, ViewChild } from '@angular/core';
import { Item } from '../../../models/item';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { ItemsService } from '../../../services/items.service';
import { ToastrService } from 'ngx-toastr';
import { ErrorHandlerService } from '../../../exceptions/ErrorHandlerService ';
import { MatDialog } from '@angular/material/dialog';
import { ReturnProductToStockComponent } from '../../dialogBox/return-product-to-stock/return-product-to-stock.component';

@Component({
  selector: 'app-items-list',
  templateUrl: './items-list.component.html',
  styleUrl: './items-list.component.css'
})
export class ItemsListComponent {
  ELEMENT_DATA: Item[] = []

  displayedColumns: string[] = ['position', 'name', 'weight', 'created', 'product', 'actions'];
  dataSource = new MatTableDataSource<Item>(this.ELEMENT_DATA);

  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(private service: ItemsService,
              private errorHandler: ErrorHandlerService,
              private toast: ToastrService,
              private dialog: MatDialog
  ) { }

  ngOnInit(): void {
    this.findAll()
  }


  findAll() {
    this.service.findAll().subscribe(response => {
      if (response && response.length > 0) {
        this.ELEMENT_DATA = response;
        this.dataSource = new MatTableDataSource<Item>(response);
        this.dataSource.paginator = this.paginator;
      } else {
        this.toast.info('Empty List');
        this.ELEMENT_DATA = [];
        this.dataSource = new MatTableDataSource<Item>([]);
      }
    }, error => {
      this.errorHandler.handleError(error)
    });
  }


  // returnProductToStock(itemId: number): void {
  //   const confirmDelete = window.confirm('Are you sure you want to return this item to stock?')
  //   if (confirmDelete) {
  //     this.service.returnProductToStock(itemId).subscribe({
  //       next: (message: string) => {
  //         this.toast.success('Item returned to stock successfully')
  //         this.findAll()
  //       },
  //       error: error => {
  //         this.errorHandler.handleError(error)
  //       }
  //     })
  //   }
  // }


  returnProductToStock(itemId: number): void {
    const dialogRef = this.dialog.open(ReturnProductToStockComponent);

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.service.returnProductToStock(itemId).subscribe({
          next: (message: string) => {
            this.toast.success('Item returned to stock successfully');
            this.findAll();
          },
          error: error => {
            this.errorHandler.handleError(error);
          }
        });
      }
    });
  }


  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }
}
