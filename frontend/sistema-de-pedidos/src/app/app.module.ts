import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { LoginComponent } from './components/login/login.component';
import { NavComponent } from './components/nav/nav.component';
import { HomeComponent } from './components/home/home.component';
import { HeaderComponent } from './components/header/header.component';
import { ClientListComponent } from './components/client/client-list/client-list.component';

import {MatSidenavModule} from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import {MatCardModule} from '@angular/material/card';
import {MatTableModule} from '@angular/material/table';
import {MatPaginator, MatPaginatorModule} from '@angular/material/paginator';
import {MatInputModule} from '@angular/material/input';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ToastrModule } from 'ngx-toastr';
import { HttpClientModule } from '@angular/common/http';
import { AuthInterceptorProvider } from './interceptors/auth.interceptor';
import { ClientCreateComponent } from './components/client/client-create/client-create.component';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatButtonModule} from '@angular/material/button';
import { provideNgxMask } from 'ngx-mask';
import { ClientUpdateComponent } from './components/client/client-update/client-update.component';
import { ClientDeleteComponent } from './components/client/client-delete/client-delete.component';
import { RegisterComponent } from './components/register/register.component';
import { UserListComponent } from './components/user/user-list/user-list.component';
import { UserCreateComponent } from './components/user/user-create/user-create.component';
import { PhoneMaskDirective } from './config/phone-mask.directive';
import { BithdateMaskDirective } from './config/birthdate-mask.directive';
import {MatDatepickerModule} from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { UserUpdateComponent } from './components/user/user-update/user-update.component';
import { UserDeleteComponent } from './components/user/user-delete/user-delete.component';
import { ProductListComponent } from './components/product/product-list/product-list.component';
import { ProductCreateComponent } from './components/product/product-create/product-create.component';
import { ProductUpdateComponent } from './components/product/product-update/product-update.component';
import { ProductDeleteComponent } from './components/product/product-delete/product-delete.component';
import { OrderListComponent } from './components/order/order-list/order-list.component';
import {MatSelectModule} from '@angular/material/select';
import { OrderCreateComponent } from './components/order/order-create/order-create.component';
import { OrderDeleteComponent } from './components/order/order-delete/order-delete.component';
import { ItemsListComponent } from './components/items/items-list/items-list.component';
import { ItemsCreateComponent } from './components/items/items-create/items-create.component';
import { ItemsUpdateComponent } from './components/items/items-update/items-update.component';
import { ItemsDeleteComponent } from './components/items/items-delete/items-delete.component';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { ReturnProductToStockComponent } from './components/dialogBox/return-product-to-stock/return-product-to-stock.component';
import { MatDialogModule } from '@angular/material/dialog';
import { ProductCategoryMenuComponent } from './components/product-category-menu/product-category-menu.component';
import { CategoriesCardComponent } from './components/categories-card/categories-card.component';
import {MatGridListModule} from '@angular/material/grid-list';
import { CategoryListComponent } from './components/category/category-list/category-list.component';
import { CategoryCreateComponent } from './components/category/category-create/category-create.component';
import { CategoryUpdateComponent } from './components/category/category-update/category-update.component';
import { CategoryDeleteComponent } from './components/category/category-delete/category-delete.component';
import { CategoryProductsComponent } from './components/category-products/category-products.component';
import { CartStatusComponent } from './components/cart-status/cart-status.component';
import { CartDetailsComponent } from './components/cart-details/cart-details.component';
import {MatSlideToggleModule} from '@angular/material/slide-toggle';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    NavComponent,
    HomeComponent,
    HeaderComponent,
    ClientListComponent,
    ClientCreateComponent,
    ClientUpdateComponent,
    ClientDeleteComponent,
    RegisterComponent,
    UserListComponent,
    UserCreateComponent,
    PhoneMaskDirective,
    BithdateMaskDirective,
    UserUpdateComponent,
    UserDeleteComponent,
    ProductListComponent,
    ProductCreateComponent,
    ProductUpdateComponent,
    ProductDeleteComponent,
    OrderListComponent,
    OrderCreateComponent,
    OrderDeleteComponent,
    ItemsListComponent,
    ItemsCreateComponent,
    ItemsUpdateComponent,
    ItemsDeleteComponent,
    ReturnProductToStockComponent,
    ProductCategoryMenuComponent,
    CategoriesCardComponent,
    CategoryListComponent,
    CategoryCreateComponent,
    CategoryUpdateComponent,
    CategoryDeleteComponent,
    CategoryProductsComponent,
    CartStatusComponent,
    CartDetailsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    MatSidenavModule,
    MatListModule,
    MatIconModule,
    MatCardModule,
    MatTableModule,
    MatPaginatorModule,
    MatPaginator,
    MatInputModule,
    FormsModule,
    ReactiveFormsModule,
    ToastrModule.forRoot({
      timeOut: 4000,
      closeButton: true,
      progressBar: true
    }),
    HttpClientModule,
    MatCheckboxModule,
    MatButtonModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatSelectModule,
    MatSnackBarModule,
    MatDialogModule,
    MatGridListModule,
    MatSlideToggleModule
  ],
  providers: [
    provideAnimationsAsync(),
    AuthInterceptorProvider,
    provideNgxMask()
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
