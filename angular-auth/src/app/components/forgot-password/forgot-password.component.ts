import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html'
})
export class ForgotPasswordComponent {
  email = '';
  message = '';

  constructor(private http: HttpClient) {}

 onSubmit() {
    this.http.post('http://localhost:8081/api/v1/auth/forgot-password', { email: this.email })
      .subscribe({
        next: (res: any) => this.message = res.message, // ✅ juste le message
        error: err => this.message = 'Error: ' + err.error.message
      });
  }
}
