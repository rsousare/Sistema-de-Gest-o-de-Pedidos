import { Component, OnInit, ViewChild } from '@angular/core';
import { Category } from '../../../models/category';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { CategoryService } from '../../../services/category.service';
import { ToastrService } from 'ngx-toastr';
import { ErrorHandlerService } from '../../../exceptions/ErrorHandlerService ';

@Component({
  selector: 'app-category-list',
  templateUrl: './category-list.component.html',
  styleUrl: './category-list.component.css'
})
export class CategoryListComponent implements OnInit {

  ELEMENT_DATA: Category[] = []


    displayedColumns: string[] = ['position', 'name', 'weight', 'actions'];
    dataSource = new MatTableDataSource<Category>(this.ELEMENT_DATA);

    @ViewChild(MatPaginator) paginator: MatPaginator;

    constructor(private service: CategoryService,
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
            this.dataSource = new MatTableDataSource<Category>(response);
            this.dataSource.paginator = this.paginator;
          } else {
            this.toast.info('Empty List');
            this.ELEMENT_DATA = [];
            this.dataSource = new MatTableDataSource<Category>([]);
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
