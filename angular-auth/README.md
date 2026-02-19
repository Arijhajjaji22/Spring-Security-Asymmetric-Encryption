# Angular Auth App

Frontend Angular application for testing Spring Boot Security API.

## Installation

```bash
cd angular-auth
npm install
```

## Running the app

```bash
npm start
```

The app will be available at `http://localhost:4200`

## Features

- Sign Up with validation
- Sign In with JWT authentication
- Protected routes with Auth Guard
- HTTP interceptors for JWT tokens
- Error handling and display

## Configuration

Update the API URL in `src/environments/environment.ts`:

```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api/v1',
};
```

## Build for production

```bash
npm run build
```
