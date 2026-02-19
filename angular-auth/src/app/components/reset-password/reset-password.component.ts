import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html'
})
export class ResetPasswordComponent implements OnInit {
  token = '';
  newPassword = '';
  confirmPassword = '';
  message = '';

  constructor(private http: HttpClient, private route: ActivatedRoute,private router: Router) {}

  ngOnInit() {
    // Récupère le token depuis l'URL
    this.token = this.route.snapshot.queryParamMap.get('token') || '';
  }

onSubmit() {
    if (this.newPassword !== this.confirmPassword) {
      this.message = 'Passwords do not match';
      return;
    }

    this.http.post<{ message: string }>(
      'http://localhost:8081/api/v1/auth/reset-password',
      { token: this.token, newPassword: this.newPassword }
    ).subscribe({
      next: (res) => {
        this.message = res.message;  // ✅ affiche le message du backend
        // Redirection après 2 secondes pour laisser l’utilisateur voir le message
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 2000);
      },
      error: (err) => this.message = err?.error?.message || 'Something went wrong'
    });
  }
}
