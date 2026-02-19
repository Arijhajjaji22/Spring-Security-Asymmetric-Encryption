# Spring Boot + Angular Authentication Project

Complete authentication system with Spring Boot backend and Angular frontend.

## Project Structure

```
springsecurityproject/
├── src/                          # Spring Boot Backend
│   ├── main/
│   │   ├── java/com/arij/project/
│   │   │   ├── auth/            # Authentication endpoints and services
│   │   │   ├── Security/        # JWT and Security configuration
│   │   │   ├── user/            # User management
│   │   │   ├── role/            # Role management
│   │   │   ├── exception/       # Exception handling
│   │   │   └── validation/      # Custom validators
│   │   └── resources/
│   │       └── application.yml
│   └── test/
│
├── angular-auth/                 # Angular Frontend
│   ├── src/
│   │   ├── app/
│   │   │   ├── components/
│   │   │   │   ├── sign-up/
│   │   │   │   ├── sign-in/
│   │   │   │   └── layout/
│   │   │   ├── services/       # HTTP services and interceptors
│   │   │   ├── models/         # TypeScript interfaces
│   │   │   ├── guards/         # Route protection
│   │   │   └── pages/
│   │   │       └── dashboard/
│   │   ├── environments/       # Environment configuration
│   │   └── assets/
│   └── package.json
│
├── pom.xml                       # Maven dependencies
├── docker-compose.yml
└── Dockerfile
```

## Backend (Spring Boot)

### Setup

1. Navigate to project root
2. Ensure Java 11+ and Maven installed
3. Configure database in `application.yml`
4. Generate JWT keys (if needed)

### Running the Backend

```bash
mvn spring-boot:run
# or
./mvnw spring-boot:run
```

Backend runs on `http://localhost:8080`

### API Endpoints

**Authentication:**
- `POST /api/v1/auth/register` - Register new user
- `POST /api/v1/auth/login` - Login user
- `POST /api/v1/auth/refresh` - Refresh JWT token

**User:**
- `GET /api/v1/users` - List users (protected)
- `GET /api/v1/users/{id}` - Get user (protected)

### Response Format

**Success (2xx):**
```json
{
  "accessToken": "jwt_token",
  "refreshToken": "refresh_token",
  "tokenType": "Bearer"
}
```

**Error (4xx/5xx):**
```json
{
  "code": "ERROR_CODE",
  "message": "Error description",
  "validationErrors": [
    {
      "field": "fieldName",
      "code": "FIELD_CODE",
      "message": "Field error"
    }
  ]
}
```

### Error Codes

- `PASSWORD_MISMATCH` - Passwords do not match
- `EMAIL_ALREADY_EXISTS` - Email already registered
- `PHONE_ALREADY_EXISTS` - Phone already registered
- `BAD_CREDENTIALS` - Invalid credentials
- `USER_NOT_FOUND` - User not found
- `ERR_USER_DISABLED` - User account disabled

## Frontend (Angular)

### Setup

```bash
cd angular-auth
npm install
```

### Running the Frontend

```bash
npm start
# App opens at http://localhost:4200
```

### Configuration

Update API URL in `src/environments/environment.ts`:

```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api/v1',
};
```

### Features

- **Reactive Forms** with validation
- **JWT Authentication** with token storage
- **HTTP Interceptors** for request/response handling
- **Route Guards** for protected pages
- **Error Handling** with custom error response
- **Bootstrap 5** styling

### Project Structure

```
src/app/
├── components/
│   ├── sign-up/          # Registration component
│   ├── sign-in/          # Login component
│   └── layout/           # Navbar component
├── services/
│   ├── auth.service.ts                    # API authentication
│   ├── auth-interceptor.service.ts        # JWT token injection
│   └── error-interceptor.service.ts       # Error handling
├── models/
│   ├── auth.model.ts     # Auth interfaces
│   └── user.model.ts     # User interfaces
├── guards/
│   └── auth.guard.ts     # Route protection
├── pages/
│   └── dashboard/        # Protected page
├── app.module.ts         # Main module
├── app-routing.module.ts # Routes
└── app.component.ts      # Root component
```

## Testing Flow

1. **Start Backend:**
   ```bash
   mvn spring-boot:run
   ```

2. **Start Frontend:**
   ```bash
   cd angular-auth
   npm start
   ```

3. **Test Registration:**
   - Navigate to `http://localhost:4200/register`
   - Fill in form with valid data
   - Errors will show password mismatch, existing email, etc.

4. **Test Login:**
   - Navigate to `http://localhost:4200/login`
   - Enter registered credentials
   - JWT token stored in localStorage
   - Redirected to dashboard

5. **Test Protected Route:**
   - Try accessing `/dashboard` without login
   - Guard redirects to login

6. **Test Token Refresh:**
   - Token automatically refreshed on expiry

## Build for Production

### Backend
```bash
mvn clean package
```

### Frontend
```bash
cd angular-auth
npm run build
```

## Docker

```bash
docker-compose up
```

## Technologies

**Backend:**
- Spring Boot 3.x
- Spring Security 6.x
- JWT Authentication
- JPA/Hibernate
- MySQL/PostgreSQL

**Frontend:**
- Angular 17
- TypeScript
- Bootstrap 5
- RxJS
- Reactive Forms

## Notes

- CORS is configured in Spring Security
- JWT expires based on configuration
- All passwords are hashed with BCrypt
- Email validation with custom validator
- Phone number validation with regex
- Comprehensive error handling on both sides
