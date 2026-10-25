import {
  Component,
  Input,
  TemplateRef,
  ContentChildren,
  QueryList,
  AfterContentInit,
} from '@angular/core';
import { CommonModule, CurrencyPipe, DatePipe } from '@angular/common';
import { TableColumn } from './table-column.model';

/**
 * Dumb Component reutilizável para renderização de tabelas com colunas variáveis.
 * Suporta formatações pré-definidas (text, date, currency, badge), formatadores manuais
 * e customização por templates.
 */
@Component({
  selector: 'app-generic-table',
  standalone: true,
  imports: [CommonModule, DatePipe, CurrencyPipe],
  templateUrl: './generic-table.component.html',
  styleUrls: ['./generic-table.component.scss'],
})
export class GenericTableComponent<T = any> implements AfterContentInit {
  @Input({ required: true }) columns: TableColumn<T>[] = [];
  @Input({ required: true }) data: T[] = [];
  @Input() loading: boolean = false;
  @Input() emptyMessage: string = 'Nenhum registro encontrado.';
  @Input() trackByKey: keyof T | string = 'id';

  @ContentChildren('customCell') customTemplates!: QueryList<{
    name: string;
    template: TemplateRef<any>;
  }>;

  private templateMap = new Map<string, TemplateRef<any>>();

  ngAfterContentInit(): void {
    if (this.customTemplates) {
      this.customTemplates.forEach((item) => {
        if (item.name) {
          this.templateMap.set(item.name, item.template);
        }
      });
    }
  }

  getTemplate(key: string): TemplateRef<any> | undefined {
    return this.templateMap.get(key);
  }

  trackByItem(index: number, item: T): any {
    if (this.trackByKey && item && typeof item === 'object') {
      const key = (item as any)[this.trackByKey];
      return key !== undefined ? key : index;
    }
    return index;
  }

  getCellValue(row: T, column: TableColumn<T>): any {
    if (column.formatter) {
      return column.formatter(row);
    }
    if (column.field && typeof row === 'object' && row !== null) {
      return (row as any)[column.field];
    }
    return null;
  }
}
