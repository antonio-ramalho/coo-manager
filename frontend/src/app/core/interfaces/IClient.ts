import { IAddressDto } from './IAddress';

export interface IClientMinDto {
  id: number;
  legalName: string;
  documentNumber: string;
}

export interface IClientDto {
  id: number;
  legalName: string;
  address: IAddressDto;
  email: string;
  phone: string;
  status: string;
  personType: string;
}

export interface INaturalClientDto extends IClientDto {
  cpf: string;
  birthDate: string;
  gender: string;
}

export interface IJuridicClientDto extends IClientDto {
  cnpj: string;
  foundationDate: string;
  tradeName: string;
}

export type IClient = INaturalClientDto | IJuridicClientDto;
export interface IUpdateNaturalClient extends Omit<INaturalClientDto, 'cpf' | 'id'> {}
export interface IUpdateJuridicClient extends Omit<IJuridicClientDto, 'cnpj' | 'id'> {}
export type IUpdateClient = IUpdateNaturalClient | IUpdateJuridicClient;
