import { Component, DestroyRef, EventEmitter, inject, Input, OnInit, Output } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { debounceTime, distinctUntilChanged } from 'rxjs';
import { ButtonComponent } from '../../ui/button/button-component';
import { LucideFunnel } from '@lucide/angular';

const SEARCH_DEBOUNCE_MS = 500;

/** Campo de busca com debounce; emite o termo digitado para o componente pai. */
@Component({
  selector: 'app-search-bar-component',
  imports: [ReactiveFormsModule, ButtonComponent, LucideFunnel],
  templateUrl: './search-bar-component.html',
})
export class SearchBarComponent implements OnInit {
  @Input() placeholder: string = 'Buscar...';
  @Output() modifiedSearch = new EventEmitter<string>();
  @Output() filterClick = new EventEmitter<void>();

  private readonly destroyRef = inject(DestroyRef);

  readonly searchControl = new FormControl('', { nonNullable: true });

  ngOnInit(): void {
    this.searchControl.valueChanges
      .pipe(debounceTime(SEARCH_DEBOUNCE_MS), distinctUntilChanged(), takeUntilDestroyed(this.destroyRef))
      .subscribe((value) => this.modifiedSearch.emit(value));
  }
}
