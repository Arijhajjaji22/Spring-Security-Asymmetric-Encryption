# Quick Start Guide

## Prerequisites

- Java 11+ installed
- Node.js 18+ and npm installed
- Maven installed

## Project Initialization

### 1. Backend Setup (Spring Boot)

```bash
# Navigate to project root
cd c:\Users\DevTeam\Desktop\springsecurityproject

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

Backend will be available at: `http://localhost:8080`

API Documentation: `http://localhost:8080/swagger-ui.html`

### 2. Frontend Setup (Angular)

```bash
# Navigate to Angular app
cd c:\Users\DevTeam\Desktop\springsecurityproject\angular-auth

# Install dependencies
npm install

# Start development server
npm start
```

Frontend will be available at: `http://localhost:4200`

## Testing the Application

### Register a New User

1. Open browser: `http://localhost:4200/register`
2. Fill the form:
   - First Name: John
   - Last Name: Doe
   - Email: john@example.com
   - Phone: +1234567890
   - Password: MyPassword123!
   - Confirm Password: MyPassword123!

3. Submit - you should see success message

### Login

1. Navigate to: `http://localhost:4200/login`
2. Enter email: john@example.com
3. Enter password: MyPassword123!
4. Click Sign In
5. You should be redirected to dashboard

### Test Error Handling

#### Password Mismatch Error
Register with different passwords:
- Password: MyPassword123!
- Confirm Password: Different123!
→ Should show: "Passwords do not match"

#### Email Already Exists
Try registering with same email:
→ Should show: "Email already exists"

#### Invalid Login
Try wrong password:
→ Should show: "Invalid email or password"

## Project Files Created

### Angular Components
- ✅ `sign-up.component.ts` - Registration form
- ✅ `sign-in.component.ts` - Login form
- ✅ `navbar.component.ts` - Navigation bar
- ✅ `dashboard.component.ts` - Protected page

### Angular Services
- ✅ `auth.service.ts` - Authentication API calls
- ✅ `auth-interceptor.service.ts` - JWT token injection
- ✅ `error-interceptor.service.ts` - Error handling

### Angular Models
- ✅ `auth.model.ts` - Auth interfaces
- ✅ `user.model.ts` - User interfaces

### Angular Configuration
- ✅ `app.module.ts` - Main module with providers
- ✅ `app-routing.module.ts` - Routes with guards
- ✅ `auth.guard.ts` - Route protection
- ✅ `environment.ts` - Development config
- ✅ `environment.prod.ts` - Production config

### Configuration Files
- ✅ `package.json` - Dependencies
- ✅ `tsconfig.json` - TypeScript config
- ✅ `tsconfig.app.json` - App-specific config
- ✅ `angular.json` - Angular CLI config
- ✅ `index.html` - HTML template with Bootstrap
- ✅ `main.ts` - Angular bootstrap
- ✅ `styles.scss` - Global styles

## API Endpoints Reference

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/api/v1/auth/register` | No | Register new user |
| POST | `/api/v1/auth/login` | No | Login user |
| POST | `/api/v1/auth/refresh` | No | Refresh token |
| GET | `/api/v1/users` | Yes | List users |

## Token Storage

After login, tokens are stored in browser localStorage:
- `accessToken` - JWT for API requests
- `refreshToken` - Token for refresh

## CORS Configuration

Angular dev server (4200) can call Spring Boot API (8080) because:
- Spring Security is configured with CORS
- Allowed origins include `localhost:4200`

## Troubleshooting

### Angular not connecting to API
- Check API URL in `angular-auth/src/environments/environment.ts`
- Ensure Spring Boot is running
- Check browser console for CORS errors

### Port 4200 already in use
```bash
ng serve --port 4300
```

### Port 8080 already in use
```bash
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dserver.port=8090"
```

### Dependencies issues
```bash
# Clear node_modules and reinstall
cd angular-auth
rm -rf node_modules package-lock.json
npm install
```

## Next Steps

1. Implement password reset functionality
2. Add user profile page
3. Add role-based access control
4. Implement email verification
5. Add OAuth2 authentication (Google, GitHub)
6. Deploy to production server
