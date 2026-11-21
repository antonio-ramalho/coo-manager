import { RoleAuth } from './auth.models';

export interface EmployeeResponse {
  id: number;
  uuid: string;
  fullName: string;
  email: string;
  cooperativeId: number;
  cooperativeName: string;
  status: 'ACTIVE' | 'INACTIVE' | 'SUSPENDED';
  twoFactorEnabled: boolean;
  roles: RoleAuth[];
  createdAt: string;
}

export interface EmployeeMin {
  id: number;
  fullName: string;
  email: string;
  status: 'ACTIVE' | 'INACTIVE' | 'SUSPENDED';
  twoFactorEnabled: boolean;
  roleNames: string[];
  createdAt: string;
}

export interface EmployeeRequest {
  fullName: string;
  email: string;
  password?: string;
  cooperativeId: number;
  roleIds: number[];
}

export interface EmployeeUpdate {
  fullName: string;
  email: string;
  roleIds: number[];
}

export interface RoleRequest {
  name: string;
  description: string;
  cooperativeId: number | null;
  permissions: string[];
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

