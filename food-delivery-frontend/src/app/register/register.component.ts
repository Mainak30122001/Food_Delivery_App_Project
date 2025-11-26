import { Component } from '@angular/core';
import { AuthService } from '../auth/auth.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router'; // <-- import RouterModule

@Component({
  selector: 'app-register',
  standalone: true,
  templateUrl: './register.component.html',
  imports: [CommonModule, FormsModule, RouterModule],
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  name = '';
  email = '';
  password = '';
  role = 'CUSTOMER';
  errorMessage = '';
  successMessage = '';

  constructor(private auth: AuthService, private router: Router) { }

  ngOnInit(): void {
    const role = this.auth.getRole();
    if (role === 'CUSTOMER') this.router.navigate(['/customer-dashboard']);
    // else if (role === 'DELIVERY_AGENT') this.router.navigate(['/delivery-dashboard']);
    else if (role === 'ADMIN') this.router.navigate(['/admin-dashboard']);
  }

  onRegister() {
    if (!this.name || !this.email || !this.password) {
      this.errorMessage = 'All fields are required.';
      return;
    }

    const data = {
      name: this.name,
      email: this.email,
      password: this.password,
      role: this.role,
    };

    this.auth.register(data).subscribe({
      next: () => {
        this.successMessage = 'Registration successful. Redirecting to login...';

        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 1500);
      },
      error: () => (this.errorMessage = 'Registration failed. Try again.'),
    });
  }
}
