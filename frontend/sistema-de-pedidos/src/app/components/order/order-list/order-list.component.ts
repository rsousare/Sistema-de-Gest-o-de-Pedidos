import { Component, OnInit, ViewChild } from '@angular/core';
import { Order } from '../../../models/order';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { OrderService } from '../../../services/order.service';
import { ToastrService } from 'ngx-toastr';
import { ErrorHandlerService } from '../../../exceptions/ErrorHandlerService ';
import { Status } from '../../../models/status.enum';

@Component({
  selector: 'app-order-list',
  templateUrl: './order-list.component.html',
  styleUrl: './order-list.component.css'
})
export class OrderListComponent implements OnInit{

ELEMENT_DATA: Order[] = []

  updateOrderId: number
  newStatus: Status
  StatusEnum: string[] = Object.keys(Status)


  displayedColumns: string[] = ['position', 'name', 'weight', 'created', 'client', 'actions'];
  dataSource = new MatTableDataSource<Order>(this.ELEMENT_DATA);

  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(private service: OrderService,
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
          this.dataSource = new MatTableDataSource<Order>(response);
          this.dataSource.paginator = this.paginator;
        } else {
          this.toast.info('Empty List');
          this.ELEMENT_DATA = [];
          this.dataSource = new MatTableDataSource<Order>([]);
        }
      }, error => {
        this.errorHandler.handleError(error)
      });
    }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  updateStatus(orderId: number, newStatus: string): void {
    this.service.updateStatus(orderId, newStatus).subscribe({
      next: (message: string)=> {
        this.toast.success('Status updated successfully')
        this.findAll()
        this.updateOrderId = null
        this.newStatus = null
      },
      error: (error)=> this.errorHandler.handleError(error)
    })
  }

  validFields(): boolean {
    return !!this.updateOrderId && !!this.newStatus
    //return !!this.updateOrderId && this.newStatus !== null && this.newStatus !== undefined;
  }
}
