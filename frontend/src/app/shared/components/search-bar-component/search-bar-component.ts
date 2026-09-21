import { Component, EventEmitter, OnDestroy, OnInit, Output } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { Subject, debounceTime, distinctUntilChanged, takeUntil } from 'rxjs';

@Component({
  selector: 'app-search-bar-component',
  imports: [ReactiveFormsModule],
  standalone: true,
  templateUrl: './search-bar-component.html',
})
export class SearchBarComponent implements OnInit, OnDestroy {
  @Output() modifiedSearch = new EventEmitter<string>();
  @Output() openFilters = new EventEmitter<void>();

  searchControl = new FormControl('');
  private destroy$ = new Subject<void>();

  ngOnInit(): void {
    this.searchControl.valueChanges
      .pipe(debounceTime(500), distinctUntilChanged(), takeUntil(this.destroy$))
      .subscribe((value) => {
        this.modifiedSearch.emit(value || '');
      });
  }

  operModalFilters() {
    this.openFilters.emit();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
