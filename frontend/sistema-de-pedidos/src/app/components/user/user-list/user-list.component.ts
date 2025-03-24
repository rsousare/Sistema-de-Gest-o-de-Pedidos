import { Component, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { ClientService } from '../../../services/client.service';
import { User } from '../../../models/user';
import { UserService } from '../../../services/user.service';
import { ToastrService } from 'ngx-toastr';
import { ErrorHandlerService } from '../../../exceptions/ErrorHandlerService ';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrl: './user-list.component.css'
})
export class UserListComponent {

  ELEMENT_DATA: User[] = []

    displayedColumns: string[] = ['position', 'name', 'weight', 'created', 'client', 'actions'];
    dataSource = new MatTableDataSource<User>(this.ELEMENT_DATA);

    @ViewChild(MatPaginator) paginator: MatPaginator;

    constructor(private service: UserService,
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
            this.dataSource = new MatTableDataSource<User>(response);
            this.dataSource.paginator = this.paginator;
          } else {
            this.toast.info('Empty List');
            this.ELEMENT_DATA = [];
            this.dataSource = new MatTableDataSource<User>([]);
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
