export interface User {
  id?: string;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  enabled?: boolean;
  createdAt?: Date;
  roles?: Role[];
  
}

export interface Role {
  id?: string;
  name: string;
}
