import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../auth/auth.service';
import { RouterModule, Router } from '@angular/router';

interface MenuItem {
  id: number;
  name: string;
  price: number;
  veg: boolean | null;
  image: string;
  quantity?: number;
}

interface Order {
  id?: number;
  menuItems: Record<number, number>; // {itemId: quantity}
  totalPrice: number;
  customerId: number;
  deliveryPartnerId: number;
  status: string;
}

@Component({
  selector: 'app-customer-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './customer-dashboard.component.html',
  styleUrl: './customer-dashboard.component.css',
})
export class CustomerDashboardComponent implements OnInit {
  menuItems: MenuItem[] = [];
  cart: MenuItem[] = [];
  orders: Order[] = [];
  loading = false;
  error = '';

  constructor(
    private auth: AuthService,
    private http: HttpClient,
    private cdr: ChangeDetectorRef,
    private router: Router
  ) { }

  orderItemsModalOpen = false;
  menuDetails: any[] = [];

  ngOnInit() {
    const role = this.auth.getRole();
    if (role !== 'CUSTOMER') {
      if (role === 'ADMIN') {
        this.router.navigate(['/admin-dashboard']);
        return;
      }
      this.router.navigate(['/login']);
      return;
    }
    this.fetchCustomerIdAndLoadData();
  }

  logout() {
    this.auth.logout();
    this.router.navigate(['/login']);
  }

  openOrderItemsModal(order: any) {
    const ids = Object.keys(order.menuItems);

    this.http.get<any[]>(`${this.MENU_URL}/by-ids?ids=${ids.join(',')}`).subscribe({
      next: (items) => {
        this.menuDetails = items.map((item) => ({
          ...item,
          quantity: order.menuItems[item.id],
        }));
        this.orderItemsModalOpen = true;
        this.cdr.detectChanges();
      },
      error: (err) => console.error(err),
    });
  }

  closeOrderItemsModal() {
    this.orderItemsModalOpen = false;
  }

  isStepActive(orderStatus: string, step: string): boolean {
    const steps = ['ORDER_PLACED', 'OUT_FOR_DELIVERY', 'DELIVERED'];
    const orderIndex = steps.indexOf(orderStatus);
    const stepIndex = steps.indexOf(step);
    return stepIndex <= orderIndex;
  }

  private MENU_URL = 'http://localhost:9999/api/menu-items';
  private ORDER_URL = 'http://localhost:9999/api/orders';

  // Fetch customer ID based on name
  customerId!: number; // store fetched ID

  fetchCustomerIdAndLoadData() {
    const name = this.auth.getName(); // get user name from AuthService
    this.http.get<{ id: number }>(`http://localhost:9999/api/users/by-name/${name}`).subscribe({
      next: (res) => {
        this.customerId = res.id;
        this.loadMenuItems();
        this.loadOrderHistory();
        this.cdr.detectChanges();
      },
      error: () => {
        this.error = 'Failed to fetch customer info';
        this.cdr.detectChanges();
      },
    });
  }

  loadMenuItems() {
    this.loading = true;
    this.cdr.detectChanges();
    this.http.get<MenuItem[]>(this.MENU_URL).subscribe({
      next: (res) => {
        this.menuItems = res.map((item) => ({ ...item, quantity: 0 }));
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.error = 'Failed to load menu items';
        this.loading = false;
        this.cdr.detectChanges();
      },
    });
  }

  increment(item: MenuItem) {
    item.quantity!++;
    this.cdr.detectChanges();
  }

  decrement(item: MenuItem) {
    if (item.quantity! > 0) item.quantity!--;
    this.cdr.detectChanges();
  }

  addToCart(item: MenuItem) {
    const existing = this.cart.find((c) => c.id === item.id);
    if (existing) {
      existing.quantity = item.quantity;
    } else {
      this.cart.push({ ...item });
    }
    this.cdr.detectChanges();
  }

  removeFromCart(item: MenuItem) {
    this.cart = this.cart.filter((c) => c.id !== item.id);
    const menuItem = this.menuItems.find((m) => m.id === item.id);
    if (menuItem) menuItem.quantity = 0;
    this.cdr.detectChanges();
  }

  get totalPrice() {
    return this.cart.reduce((sum, item) => sum + item.price * (item.quantity || 0), 0);
  }

  placeOrder() {
    if (!this.cart.length || !this.customerId) return;

    const menuItemsMap: Record<number, number> = {};
    this.cart.forEach((item) => {
      menuItemsMap[item.id] = item.quantity || 0;
    });

    const order: Order = {
      menuItems: menuItemsMap,
      totalPrice: this.totalPrice,
      customerId: this.customerId,
      deliveryPartnerId: Math.floor(Math.random() * 10) + 1,
      status: 'ORDER_PLACED',
    };

    this.http.post<Order>(this.ORDER_URL, order).subscribe({
      next: (res) => {
        this.cart = [];
        this.menuItems.forEach((m) => (m.quantity = 0));
        this.orders.unshift(res);
        this.cdr.detectChanges();
      },
      error: () => {
        this.error = 'Failed to place order';
        this.cdr.detectChanges();
      },
    });
  }

  loadOrderHistory() {
    if (!this.customerId) return; // ensure we have the ID

    this.http.get<Order[]>(`${this.ORDER_URL}/customer/${this.customerId}`).subscribe({
      next: (res) => {
        this.orders = res.reverse();
        this.cdr.detectChanges();
      },
      error: () => {
        this.error = 'Failed to load order history';
        this.cdr.detectChanges();
      },
    });
  }

  getMenuItemKeys(obj: Record<number, number>): number[] {
    return Object.keys(obj).map((k) => +k);
  }

  isLastKey(obj: Record<number, number>, key: number) {
    const keys = this.getMenuItemKeys(obj);
    return keys.indexOf(key) === keys.length - 1;
  }
}
