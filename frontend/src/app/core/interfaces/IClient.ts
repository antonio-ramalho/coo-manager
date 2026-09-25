import { IAddressDto } from './IAddress';

export interface IClientMinDto {
  id: number;
  legalName: string;
  documentNumber: string;
  status: string;
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

export interface ClientDetailsView {
  id: number;
  legalName: string;
  status: string;
  personType: string;
  phone: string;
  email: string;
  address: any;
  attachments: any[];

  documentNumber: string;
  documentLabel: string;
  dateValue: string;
  dateLabel: string;
  gender: string | null;
  tradeName: string | null;
}

export type IClient = INaturalClientDto | IJuridicClientDto;
export interface IUpdateNaturalClient extends Omit<INaturalClientDto, 'cpf' | 'id'> {}
export interface IUpdateJuridicClient extends Omit<IJuridicClientDto, 'cnpj' | 'id'> {}
export type IUpdateClient = IUpdateNaturalClient | IUpdateJuridicClient;
