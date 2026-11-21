export interface RoleAuth {
  id: number;
  name: string;
  description: string;
  systemRole: boolean;
  cooperativeId: number | null;
  permissions: string[];
}

export interface UserAuth {
  id: number;
  uuid: string;
  fullName: string;
  email: string;
  cooperativeId: number | null;
  cooperativeName: string | null;
  status: 'ACTIVE' | 'INACTIVE' | 'SUSPENDED';
  twoFactorEnabled: boolean;
  roles: RoleAuth[];
  createdAt: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  is2faRequired: boolean;
  preAuthToken: string | null;
  accessToken: string | null;
  user: UserAuth | null;
}

export interface TotpVerifyRequest {
  preAuthToken: string;
  code: string;
}

export interface TotpSetupResponse {
  secret: string;
  qrCodeUri: string;
}

export interface TotpConfirmRequest {
  secret: string;
  code: string;
}

