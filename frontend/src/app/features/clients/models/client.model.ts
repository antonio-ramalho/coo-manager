import { Address } from '../../../shared/models/address.model';
import { Attachment } from '../../../shared/models/attachment.model';
import { Gender } from '../../../shared/enums/gender';
import { PersonType } from '../../../shared/enums/person-type';
import { Status } from '../../../shared/enums/status';

/** Campos comuns a qualquer cliente (espelha o `IClientDto` do backend). */
interface ClientBase {
  id: number;
  legalName: string;
  email: string;
  phone: string;
  status: Status;
  address: Address;
}

/** Cliente pessoa física. Datas trafegam no formato ISO `YYYY-MM-DD`. */
export interface NaturalClient extends ClientBase {
  personType: PersonType.NATURAL;
  cpf: string;
  birthDate: string;
  gender: Gender;
}

/** Cliente pessoa jurídica. */
export interface JuridicClient extends ClientBase {
  personType: PersonType.JURIDIC;
  cnpj: string;
  foundationDate: string;
  tradeName: string;
}

/**
 * União discriminada por `personType` (definido via `@JsonTypeInfo` no backend).
 * Dispensa casts: após checar `personType`, o TypeScript estreita o tipo sozinho.
 */
export type Client = NaturalClient | JuridicClient;

/** Extrai o subtipo de cliente correspondente a um `PersonType`. */
export type ClientOf<T extends PersonType> = Extract<Client, { personType: T }>;

/** Resumo usado na listagem paginada (`ClientMinDto`). */
export type ClientSummary = Pick<ClientBase, 'id' | 'legalName' | 'status'> & {
  documentNumber: string;
};

/**
 * Corpo de cadastro e atualização: o backend recebe o mesmo `IClientDto` polimórfico
 * nos dois casos (por isso `personType` é obrigatório; `id` e `status` são definidos pelo servidor).
 */
export type ClientPayload = Omit<NaturalClient, 'id' | 'status'> | Omit<JuridicClient, 'id' | 'status'>;

/** Modelo de exibição do painel de detalhes, já normalizado para PF e PJ. */
export interface ClientDetails {
  id: number;
  legalName: string;
  status: Status;
  personType: PersonType;
  phone: string;
  email: string;
  address: Address;
  attachments: Attachment[];
  documentNumber: string;
  documentLabel: string;
  dateValue: string;
  dateLabel: string;
  gender: Gender | null;
}
