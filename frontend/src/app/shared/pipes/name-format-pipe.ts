import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'nameFormatPipe',
  standalone: true,
})
export class NameFormatPipe implements PipeTransform {
  transform(value: string | null | undefined, maxLength: number = 25): string {
    if (!value) return '';

    const words = value.trim().toLowerCase().split(/\s+/);

    const prepositions = ['de', 'da', 'do', 'das', 'dos', 'e'];

    let formattedName = words
      .map((word, index) => {
        if (index > 0 && prepositions.includes(word)) {
          return word;
        }
        return word.charAt(0).toUpperCase() + word.slice(1);
      })
      .join(' ');

    if (formattedName.length > maxLength) {
      return formattedName.substring(0, maxLength).trim() + '...';
    }
    return formattedName;
  }
}
