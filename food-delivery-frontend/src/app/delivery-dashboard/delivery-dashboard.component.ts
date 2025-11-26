import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../auth/auth.service'; // import your AuthService

@Component({
  selector: 'app-delivery-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <div class="container mt-5">
      <h2>Delivery Dashboard</h2>
      <p>Delivery agent assignments and status will appear here.</p>
      <a routerLink="/home" class="btn btn-primary">Go Back Home</a>
    </div>
  `,
})
export class DeliveryDashboardComponent implements OnInit {
  constructor(private auth: AuthService, private router: Router) {}

  ngOnInit(): void {
    const role = this.auth.getRole();

    // Only allow DELIVERY_AGENT
    if (role !== 'DELIVERY_AGENT') {
      // Redirect unauthorized users
      this.router.navigate(['/home']);
    }
  }
}
