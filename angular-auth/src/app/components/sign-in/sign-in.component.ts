declare const google: any;

import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { AuthenticationRequest, GoogleAuthRequest } from '../../models/auth.model';

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.scss'],
})
export class SignInComponent {

  email = '';
  password = '';
  loading = false;
  submitted = false;
   rememberMe = false;
  errorMessage: string = '';
  showPassword = false;

  constructor(private authService: AuthService, private router: Router) {
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/dashboard']);
    }

    // Initialisation Google UNE SEULE FOIS
    google.accounts.id.initialize({
      client_id: '836034687683-n7kf94oc417n1qj3brvhc1l9hmpkadbr.apps.googleusercontent.com',
      callback: (response: any) => this.handleGoogleResponse(response),
    });
  }

  onSubmit(): void {
    this.submitted = true;
    this.errorMessage = '';

    if (!this.email || !this.password || this.password.length < 8) return;

    this.loading = true;
    const request: AuthenticationRequest = { email: this.email, password: this.password, rememberMe: this.rememberMe };

    this.authService.login(request).subscribe({
      next: (res) => {
        this.loading = false;
        // Stocke le token selon Remember Me
        if (this.rememberMe) {
          localStorage.setItem('accessToken', res.accessToken);
          localStorage.setItem('refreshToken', res.refreshToken);
        } else {
          sessionStorage.setItem('accessToken', res.accessToken);
          sessionStorage.setItem('refreshToken', res.refreshToken);
        }
        this.router.navigate(['/dashboard']);
      },
      error: (error) => {
        this.loading = false;
        this.errorMessage = error.error?.message || 'Login failed';
      }
    });
  }

  // 🔥 Ton bouton appelle cette méthode
  onGoogleSignIn(): void {
    this.loading = true;
    this.errorMessage = '';

    google.accounts.id.prompt(); // ouvre le popup Google
  }

  handleGoogleResponse(response: any) {
    const request: GoogleAuthRequest = { idToken: response.credential };

    this.authService.loginWithGoogle(request).subscribe({
      next: () => this.router.navigate(['/dashboard']),
      error: (error) => {
        this.loading = false;
        this.errorMessage = error.error?.message || 'Google login failed';
      }
    });
  }
}
