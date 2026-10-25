import { TemplateRef } from '@angular/core';

export type ColumnAlign = 'left' | 'center' | 'right';

export interface TableColumn<T = any> {
  key: string;
  header: string;
  field?: keyof T | string;
  formatter?: (row: T) => string | number | null | undefined;
  type?: 'text' | 'date' | 'currency' | 'number' | 'badge' | 'custom';
  cellClass?: (row: T) => string;
  align?: ColumnAlign;
  width?: string;
  cellTemplate?: TemplateRef<{ $implicit: T; column: TableColumn<T> }>;
}
