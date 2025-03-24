import { Component, OnInit, ViewChild } from '@angular/core';
import { Client } from '../../../models/clients';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { ClientService } from '../../../services/client.service';
import { ErrorHandlerService } from '../../../exceptions/ErrorHandlerService ';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-client-list',
  templateUrl: './client-list.component.html',
  styleUrl: './client-list.component.css'
})
export class ClientListComponent implements OnInit{

  ELEMENT_DATA: Client[] = []

  displayedColumns: string[] = ['position', 'name', 'weight', 'created', 'actions'];
  dataSource = new MatTableDataSource<Client>(this.ELEMENT_DATA);

  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(private service: ClientService,
              private errorHandler: ErrorHandlerService,
              private toast: ToastrService
  ) {}

  ngOnInit(): void {
    this.findAll()
  }


  findAll() {
      this.service.findAll().subscribe(response => {
        if (response && response.length > 0) {
          this.ELEMENT_DATA = response;
          this.dataSource = new MatTableDataSource<Client>(response);
          this.dataSource.paginator = this.paginator;
        } else {
          this.toast.info('Empty List');
          this.ELEMENT_DATA = [];
          this.dataSource = new MatTableDataSource<Client>([]);
        }
      }, error => {
        this.errorHandler.handleError(error)
      });
    }


  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

}
