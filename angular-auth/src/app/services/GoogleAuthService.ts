// src/app/services/google-auth.service.ts

import { Injectable } from '@angular/core';

declare const google: any;

@Injectable({
  providedIn: 'root'
})
export class GoogleAuthService {
private clientId = '836034687683-n7kf94oc417n1qj3brvhc1l9hmpkadbr.apps.googleusercontent.com';

  constructor() {}

  initializeGoogleSignIn(callback: (response: any) => void): void {
    google.accounts.id.initialize({
      client_id: this.clientId,
      callback: callback
    });
  }

  renderButton(elementId: string): void {
    google.accounts.id.renderButton(
      document.getElementById(elementId),
      {
        theme: 'outline',
        size: 'large',
        text: 'signin_with',
        shape: 'rectangular',
        width: '100%'
      }
    );
  }
}