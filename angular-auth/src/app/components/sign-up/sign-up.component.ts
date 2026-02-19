import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { RegistrationRequest, ErrorResponse } from '../../models/auth.model';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.scss'],
})
export class SignUpComponent implements OnInit {

  signUpForm!: FormGroup;
  loading = false;
  submitted = false;
  errorMessage: string = '';
  successMessage: string = '';

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.createForm();
  }

  createForm(): void {
    this.signUpForm = this.formBuilder.group({
      firstName: ['', [Validators.required, Validators.minLength(1)]],
      lastName: ['', [Validators.required, Validators.minLength(1)]],
      email: ['', [Validators.required, Validators.email]],
      phoneNumber: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      confirmPassword: ['', [Validators.required]],
    });
  }

  get f() {
    return this.signUpForm.controls;
  }



onSubmit(): void {
  this.submitted = true;
  this.errorMessage = '';
  this.successMessage = '';

  if (this.signUpForm.invalid) {
    return;
  }

  this.loading = true;
  const request: RegistrationRequest = this.signUpForm.value;

  this.authService.register(request).subscribe(
    // ✅ Success callback (RxJS 6 style)
    () => {
      this.loading = false;
      this.successMessage = 'Registration successful! Redirecting...';
      setTimeout(() => this.router.navigate(['/login']), 2000);
    },
    // ✅ Error callback (RxJS 6 style)
    (err: HttpErrorResponse) => {
      this.loading = false;

      console.log('========== DEBUG ERREUR COMPLÈTE ==========');
      console.log('1. err:', err);
      console.log('2. err.status:', err.status);
      console.log('3. err.error:', err.error);
      console.log('4. Type de err.error:', typeof err.error);
      console.log('==========================================');

      let backendError: ErrorResponse = { 
        message: 'An unknown error occurred', 
        code: 'UNKNOWN' 
      };

      // Extraction de l'erreur
      if (err.error) {
        if (typeof err.error === 'object' && err.error.message) {
          // L'erreur est déjà un objet JSON
          backendError = {
            message: err.error.message,
            code: err.error.code || 'UNKNOWN',
            validationErrors: err.error.validationErrors
          };
        } else if (typeof err.error === 'string') {
          // L'erreur est une chaîne, essayer de la parser
          try {
            const parsed = JSON.parse(err.error);
            if (parsed.message) {
              backendError = parsed;
            }
          } catch (e) {
            backendError = { message: err.error, code: 'UNKNOWN' };
          }
        }
      } else if (err.message) {
        backendError = { message: err.message, code: 'UNKNOWN' };
      }

      console.log('✅ ERREUR FINALE EXTRAITE:', backendError);

      // Nettoyage des erreurs précédentes
      Object.keys(this.signUpForm.controls).forEach(key => {
        const control = this.signUpForm.get(key);
        if (control?.errors && control.errors['backendError']) {
          const { backendError, ...otherErrors } = control.errors;
          control.setErrors(Object.keys(otherErrors).length ? otherErrors : null);
        }
      });

      // Mapping des erreurs sur les champs
      const fieldMap: { [key: string]: string } = {
        'EMAIL_ALREADY_EXISTS': 'email',
        'PHONE_ALREADY_EXISTS': 'phoneNumber',
        'PASSWORD_MISMATCH': 'confirmPassword',
      };

      if (backendError.code && fieldMap[backendError.code]) {
        const fieldName = fieldMap[backendError.code];
        const control = this.signUpForm.get(fieldName);

        if (control) {
          control.setErrors({ 
            ...control.errors, 
            backendError: backendError.message 
          });
          control.markAsTouched();
        }
      } else {
        this.errorMessage = backendError.message || 'Registration failed';
      }
    }
  );
}

}
