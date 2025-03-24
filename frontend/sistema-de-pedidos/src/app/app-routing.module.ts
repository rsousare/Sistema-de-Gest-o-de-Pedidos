import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { NavComponent } from './components/nav/nav.component';
import { HomeComponent } from './components/home/home.component';
import { ClientListComponent } from './components/client/client-list/client-list.component';
import { AuthGuard } from './auth/auth.guard';
import { ClientCreateComponent } from './components/client/client-create/client-create.component';
import { ClientUpdateComponent } from './components/client/client-update/client-update.component';
import { ClientDeleteComponent } from './components/client/client-delete/client-delete.component';
import { RegisterComponent } from './components/register/register.component';
import { UserListComponent } from './components/user/user-list/user-list.component';
import { UserCreateComponent } from './components/user/user-create/user-create.component';
import { UserUpdateComponent } from './components/user/user-update/user-update.component';
import { UserDeleteComponent } from './components/user/user-delete/user-delete.component';
import { ProductListComponent } from './components/product/product-list/product-list.component';
import { ProductCreateComponent } from './components/product/product-create/product-create.component';
import { ProductUpdateComponent } from './components/product/product-update/product-update.component';
import { ProductDeleteComponent } from './components/product/product-delete/product-delete.component';
import { OrderListComponent } from './components/order/order-list/order-list.component';
import { OrderCreateComponent } from './components/order/order-create/order-create.component';
import { OrderDeleteComponent } from './components/order/order-delete/order-delete.component';
import { ItemsListComponent } from './components/items/items-list/items-list.component';
import { ItemsCreateComponent } from './components/items/items-create/items-create.component';
import { ItemsUpdateComponent } from './components/items/items-update/items-update.component';
import { ItemsDeleteComponent } from './components/items/items-delete/items-delete.component';
import { ProductCategoryMenuComponent } from './components/product-category-menu/product-category-menu.component';
import { CategoryListComponent } from './components/category/category-list/category-list.component';
import { CategoryCreateComponent } from './components/category/category-create/category-create.component';
import { CategoryUpdateComponent } from './components/category/category-update/category-update.component';
import { CategoryDeleteComponent } from './components/category/category-delete/category-delete.component';
import { CategoryProductsComponent } from './components/category-products/category-products.component';
import { CartDetailsComponent } from './components/cart-details/cart-details.component';

const routes: Routes = [

  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},

  {path: '', component: NavComponent, canActivate: [AuthGuard], children: [
    {path: 'home', component: HomeComponent},
    {path: 'productCategoryMenu', component: ProductCategoryMenuComponent},
    {path: 'category-products/:id', component: CategoryProductsComponent},

    {path: 'client', component: ClientListComponent},
    {path: 'client/create', component: ClientCreateComponent},
    {path: 'client/update/:id', component: ClientUpdateComponent},
    {path: 'client/delete/:id', component: ClientDeleteComponent},

    {path: 'user', component: UserListComponent},
    {path: 'user/create', component: UserCreateComponent},
    {path: 'user/update/:id', component: UserUpdateComponent},
    {path: 'user/delete/:id', component: UserDeleteComponent},

    {path: 'product', component: ProductListComponent},
    {path: 'product/create', component: ProductCreateComponent},
    {path: 'product/update/:id', component: ProductUpdateComponent},
    {path: 'product/delete/:id', component: ProductDeleteComponent},

    {path: 'order', component: OrderListComponent},
    {path: 'order/create', component: OrderCreateComponent},
    {path: 'order/delete/:id', component: OrderDeleteComponent},

    {path: 'item', component: ItemsListComponent},
    {path: 'item/create', component: ItemsCreateComponent},
    {path: 'item/update/:id', component: ItemsUpdateComponent},
    {path: 'item/delete/:id', component: ItemsDeleteComponent},

    {path: 'category', component: CategoryListComponent},
    {path: 'category/create', component: CategoryCreateComponent},
    {path: 'category/update/:id', component: CategoryUpdateComponent},
    {path: 'category/delete/:id', component: CategoryDeleteComponent},

    {path: 'cart-details', component: CartDetailsComponent}
  ]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
