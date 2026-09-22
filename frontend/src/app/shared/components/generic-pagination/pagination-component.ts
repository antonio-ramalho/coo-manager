import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-pagination-component',
  standalone: true,
  imports: [],
  templateUrl: './pagination-component.html',
})
export class PaginationComponent {
  @Input() currentPage: number = 1;
  @Input() totalItens: number = 0;
  @Input() itensPerPage: number = 10;

  @Output() swappedPage = new EventEmitter<number>();

  get totalPages(): number {
    if (this.totalItens === 0) return 1;
    return Math.ceil(this.totalItens / this.itensPerPage);
  }

  get canReturn(): boolean {
    return this.currentPage > 1;
  }

  get canAdvance(): boolean {
    return this.currentPage < this.totalPages;
  }

  return(): void {
    if (this.canReturn) {
      this.swappedPage.emit(this.currentPage - 1);
    }
  }

  advance(): void {
    if (this.canAdvance) {
      this.swappedPage.emit(this.currentPage + 1);
    }
  }
}
