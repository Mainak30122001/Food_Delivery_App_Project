import { Component, ChangeDetectorRef } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../auth/auth.service';
import { RouterModule, Router } from '@angular/router';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.css'
})
export class AdminDashboardComponent {
  restaurants: any[] = [];
  loading = false;
  error = '';

  modalOpen = false;
  isUpdateMode = false;
  modalRestaurant: any = { name: '', address: '', cuisine: '', image: '', id: null };

  viewModalOpen = false;
  viewRestaurant: any = { name: '', address: '', cuisine: '', image: '', id: null };

  menuModalOpen = false;
  selectedRestaurant: any = null;
  menuItems: any[] = [];
  menuItem: any = { name: '', price: null, type: null, restaurantId: null, image: '' };
  menuEditMode = false;

  private BASE_URL = 'http://localhost:9999/api/restaurants';
  private MENU_URL = 'http://localhost:9999/api/menu-items';

  pendingOrders: any[] = [];
  orderStatusOptions = ['ORDER_PLACED', 'OUT_FOR_DELIVERY', 'DELIVERED'];

  private ORDER_URL = 'http://localhost:9999/api/orders';

  constructor(
    private http: HttpClient,
    private cdr: ChangeDetectorRef,
    private auth: AuthService,
    private router: Router
  ) { }

  ngOnInit() {
    const role = this.auth.getRole();

    // Only allow CUSTOMER role to access Customer Dashboard
    if (role !== 'ADMIN') {
      if (role === 'CUSTOMER') {
        this.router.navigate(['/customer-dashboard']);
        return;
      }
      this.router.navigate(['/login']); // redirect unauthorized users
      return;
    }
    this.loadRestaurants();
    this.loadPendingOrders();
    this.loadCustomerOrderAnalysis();
  }

  logout() {
    this.auth.logout();
    this.router.navigate(['/login']);
  }

  orderItemsModalOpen = false;
  menuDetails: any[] = [];

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

  customerOrderStats: any[] = [];

  loadCustomerOrderAnalysis() {
    this.http.get<any[]>(`${this.ORDER_URL}/analysis/orders-by-customer`).subscribe({
      next: (stats) => {
        this.customerOrderStats = stats;
        this.cdr.detectChanges();
      },
      error: () => alert('Failed to load customer order analytics'),
    });
  }

  loadPendingOrders() {
    this.http.get<any[]>(`${this.ORDER_URL}/pending`).subscribe({
      next: (res) => {
        this.pendingOrders = res || [];
        this.cdr.detectChanges();
      },
      error: () => {
        console.error('Failed to load pending orders');
        this.pendingOrders = [];
        this.cdr.detectChanges();
      },
    });
  }

  updateOrderStatus(order: any) {
    const updatedStatus = order.status;

    this.http
      .put(
        `${this.ORDER_URL}/${order.id}/status?status=${updatedStatus}`,
        {} // empty body because backend does NOT accept JSON
      )
      .subscribe({
        next: () => {
          alert('Order status updated!');
          this.loadPendingOrders();
        },
        error: () => {
          alert('Failed to update status');
        },
      });
  }

  loadRestaurants() {
    this.loading = true;
    this.error = '';
    this.cdr.detectChanges();
    this.http.get<any[]>(this.BASE_URL).subscribe({
      next: (res) => {
        this.restaurants = Array.isArray(res) ? [...res] : [];
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.error = 'Failed to load restaurants';
        this.restaurants = [];
        this.loading = false;
        this.cdr.detectChanges();
      },
    });
  }

  openAddModal() {
    this.isUpdateMode = false;
    this.modalRestaurant = { name: '', address: '', cuisine: '', image: '', id: null };
    this.modalOpen = true;
  }

  openUpdateModal(restaurant: any) {
    this.isUpdateMode = true;
    this.modalRestaurant = { ...restaurant };
    this.modalOpen = true;
  }

  closeModal() {
    this.modalOpen = false;
  }

  saveRestaurant() {
    this.loading = true;
    this.error = '';
    if (this.isUpdateMode) {
      this.http.put(`${this.BASE_URL}/${this.modalRestaurant.id}`, this.modalRestaurant).subscribe({
        next: () => {
          const index = this.restaurants.findIndex((r) => r.id === this.modalRestaurant.id);
          if (index !== -1) this.restaurants[index] = { ...this.modalRestaurant };
          this.closeModal();
          this.loading = false;
          this.cdr.detectChanges();
        },
        error: () => {
          this.error = 'Failed to update restaurant';
          this.loading = false;
        },
      });
    } else {
      this.http.post(this.BASE_URL, this.modalRestaurant).subscribe({
        next: (res: any) => {
          this.restaurants.push(res);
          this.closeModal();
          this.loading = false;
        },
        error: () => {
          this.error = 'Failed to add restaurant';
          this.loading = false;
        },
      });
    }
  }

  delete(id: number) {
    if (!confirm('Are you sure you want to delete this menu item?')) return;
    this.loading = true;
    this.http.delete(`${this.BASE_URL}/${id}`, { responseType: 'text' }).subscribe({
      next: () => {
        this.restaurants = this.restaurants.filter((r) => r.id !== id);
        this.loading = false;
        this.cdr.detectChanges(); // update UI after deletion
      },
      error: () => {
        this.error = 'Error deleting Restaurant';
        this.loading = false;
        this.cdr.detectChanges(); // update UI after error
      },
    });
  }

  view(restaurant: any) {
    this.viewRestaurant = { ...restaurant };
    this.viewModalOpen = true;
  }

  openMenuModal(restaurant: any) {
    this.selectedRestaurant = restaurant;
    this.menuModalOpen = true;
    this.menuEditMode = false;
    this.menuItem = { name: '', price: null, type: null, restaurantId: restaurant.id, image: null };

    // Fetch the latest menu items from the backend
    this.http.get<any[]>(`${this.MENU_URL}/restaurant/${restaurant.id}`).subscribe({
      next: (res) => {
        this.menuItems = res; // always latest data
        this.cdr.detectChanges(); // ensure UI updates immediately
      },
      error: () => {
        this.menuItems = [];
        this.cdr.detectChanges();
      },
    });
  }

  saveMenuItem() {
    if (this.menuEditMode) {
      this.http.put(`${this.MENU_URL}/${this.menuItem.id}`, this.menuItem).subscribe({
        next: () => {
          const index = this.menuItems.findIndex((m) => m.id === this.menuItem.id);
          if (index !== -1) this.menuItems[index] = { ...this.menuItem }; // update in-place
          this.menuItem = {
            name: '',
            price: null,
            type: null,
            restaurantId: this.selectedRestaurant.id,
            image: null,
          };
          this.menuEditMode = false;
          this.cdr.detectChanges(); // trigger change detection
        },
        error: () => {
          alert('Failed to update menu item');
        },
      });
    } else {
      this.http.post(this.MENU_URL, this.menuItem).subscribe({
        next: (res: any) => {
          this.menuItems.push(res); // add new item directly
          this.menuItem = {
            name: '',
            price: null,
            type: null,
            restaurantId: this.selectedRestaurant.id,
            image: null,
          };
          this.cdr.detectChanges();
        },
        error: () => {
          alert('Failed to add menu item');
        },
      });
    }
  }

  editMenuItem(menu: any) {
    this.menuItem = { ...menu }; // Populate the form with selected menu item
    this.menuEditMode = true; // Set edit mode to true
  }

  deleteMenuItem(id: number) {
    if (!confirm('Are you sure you want to delete this menu item?')) return;
    this.http.delete(`${this.MENU_URL}/${id}`, { responseType: 'text' }).subscribe({
      next: () => {
        this.menuItems = this.menuItems.filter((m) => m.id !== id); // remove from array directly
        this.cdr.detectChanges();
      },
      error: () => {
        alert('Failed to delete menu item');
      },
    });
  }
}
