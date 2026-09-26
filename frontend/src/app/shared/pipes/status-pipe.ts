import { Pipe, PipeTransform } from '@angular/core';
import { Status } from '../enums/status';

@Pipe({
  name: 'statusPipe',
})
export class StatusPipe implements PipeTransform {

  private readonly statusMap: Record<Status, string> = {
    [Status.ACTIVE]: 'Ativo',
    [Status.INACTIVE]: 'Inativo',
    [Status.SUSPENDED]: 'Suspenso',
  };

  transform(value: Status | string | undefined | null): string {
    if (!value) return 'Desconhecido';

    return this.statusMap[value as Status] || 'Desconhecido';
  }
}
