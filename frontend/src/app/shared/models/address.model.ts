import { BrazilianStateCode } from '../enums/brazilian-state';

/** Endereço compartilhado entre clientes, cooperados e agricultores (espelha o `AddressDto` do backend). */
export interface Address {
  zipCode: string;
  street: string;
  number: string;
  neighborhood: string;
  city: string;
  state: BrazilianStateCode;
}
