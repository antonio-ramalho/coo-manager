export * from './helpers/document-digits.helper';
export * from './documents/cpf.validator';
export * from './documents/cnpj.validator';
export * from './documents/ncm.validator';
export * from './dates/birth-date.validator';
export * from './dates/future-date.validator';
export * from './numbers/positive-number.validator';
import { cpfValidator } from './documents/cpf.validator';
import { cnpjValidator } from './documents/cnpj.validator';
import { ncmValidator } from './documents/ncm.validator';
import { birthDateValidator } from './dates/birth-date.validator';
import { futureDateValidator } from './dates/future-date.validator';
import { positiveNumberValidator } from './numbers/positive-number.validator';

export class CustomValidators {
  static cpf = cpfValidator;
  static cnpj = cnpjValidator;
  static ncm = ncmValidator;
  static birthDate = birthDateValidator;
  static futureDate = futureDateValidator;
  static positiveNumber = positiveNumberValidator;
}
