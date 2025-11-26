import { Component } from '@angular/core';
import { RouterModule, Router } from '@angular/router'; // <-- import RouterModule
import { AuthService } from '../auth/auth.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  templateUrl: './login.component.html',
  imports: [CommonModule, FormsModule, RouterModule], // <-- add RouterModule here
  styleUrl: './login.component.css'
})
export class LoginComponent {
  email = '';
  password = '';
  errorMessage = '';

  constructor(private auth: AuthService, private router: Router) { }

  ngOnInit() {
    const role = this.auth.getRole();
    if (role === 'CUSTOMER') this.router.navigate(['/customer-dashboard']);
    // else if (role === 'DELIVERY_AGENT') this.router.navigate(['/delivery-dashboard']);
    else if (role === 'ADMIN') this.router.navigate(['/admin-dashboard']);
  }

  onLogin() {
    if (!this.email || !this.password) {
      this.errorMessage = 'Email and password are required.';
      return;
    }

    this.auth.login({ email: this.email, password: this.password }).subscribe({
      next: (res: any) => {
        const parsed = JSON.parse(res);

        this.auth.saveSession(parsed.token);

        const role = localStorage.getItem('role');

        if (role === 'CUSTOMER') this.router.navigate(['/customer-dashboard']);
        else if (role === 'DELIVERY_AGENT') this.router.navigate(['/delivery-dashboard']);
        else this.router.navigate(['/admin-dashboard']);
      },
      error: () => (this.errorMessage = 'Invalid email or password'),
    });
  }
}
