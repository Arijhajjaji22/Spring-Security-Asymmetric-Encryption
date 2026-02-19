export interface RegistrationRequest {
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  password: string;
  confirmPassword: string;
}

export interface AuthenticationRequest {
  email: string;
  password: string;
  rememberMe?: boolean;
}
export interface GoogleAuthRequest {
  idToken: string; // ✅ obligatoire
}



export interface AuthenticationResponse {
  accessToken: string;
  refreshToken: string;
  tokenType: string;
}

export interface RefreshRequest {
  refreshToken: string;
}

// ✅ AJOUTER CETTE INTERFACE
export interface GoogleAuthRequest {
  idToken: string;
}

export interface ErrorResponse {
    message: string;
    code?: string;     
    validationErrors?: Array<{ field: string; message: string }>;
}

export interface ValidationError {
  field: string;
  code: string;
  message: string;
}

