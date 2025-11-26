import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth/auth.service';

@Component({
  selector: 'app-home',
  standalone: true,
  template: `<p>Redirecting...</p>`,
})
export class HomeComponent implements OnInit {
  constructor(private auth: AuthService, private router: Router) {}

  ngOnInit(): void {
    const role = this.auth.getRole();
    if (role === 'CUSTOMER') this.router.navigate(['/customer-dashboard']);
    // else if (role === 'DELIVERY_AGENT') this.router.navigate(['/delivery-dashboard']);
    else if (role === 'ADMIN') this.router.navigate(['/admin-dashboard']);
    else this.router.navigate(['/login']); // not logged in
  }
}
