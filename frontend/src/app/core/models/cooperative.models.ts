export interface AddressDto {
  street: string;
  number: string;
  neighborhood: string;
  city: string;
  zipCode: string;
  state: string;
}

export interface CooperativeResponse {
  id: number;
  uuid: string;
  legalName: string;
  tradeName: string;
  cnpj: string;
  stateRegistration: string;
  email: string;
  phone: string;
  foundationDate: string;
  registrationDate: string;
  status: 'ACTIVE' | 'INACTIVE' | 'SUSPENDED';
  address: AddressDto;
}

export interface CooperativeRequest {
  legalName: string;
  tradeName: string;
  cnpj: string;
  stateRegistration: string;
  email: string;
  phone: string;
  foundationDate: string;
  address: AddressDto;
}

export interface CooperativeMin {
  id: number;
  legalName: string;
  tradeName: string;
  cnpj: string;
  email: string;
  registrationDate: string;
  status: 'ACTIVE' | 'INACTIVE' | 'SUSPENDED';
}

