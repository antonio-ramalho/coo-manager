import { Pipe, PipeTransform } from '@angular/core';
import { Gender } from '../enums/gender';

@Pipe({
  name: 'genderLabel',
})
export class GenderPipe implements PipeTransform {
  private readonly genderMap: Record<Gender, string> = {
    [Gender.MALE]: 'Masculino',
    [Gender.FEMALE]: 'Feminino',
    [Gender.OTHER]: 'Outro',
    [Gender.NOT_SAY]: 'Prefiro não dizer',
  };

  transform(value: Gender | string | undefined | null): string {
    if (!value) return 'Não informado';

    return this.genderMap[value as Gender] || 'Desconhecido';
  }
}
