import {
  Component,
  DestroyRef,
  ElementRef,
  EventEmitter,
  forwardRef,
  HostListener,
  inject,
  Input,
  OnChanges,
  OnInit,
  Output,
  signal,
  SimpleChanges,
  viewChild,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  ControlValueAccessor,
  FormControl,
  FormsModule,
  NG_VALUE_ACCESSOR,
  ReactiveFormsModule,
} from '@angular/forms';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { catchError, debounceTime, distinctUntilChanged, finalize, Observable, of, switchMap, tap } from 'rxjs';
import { LucideChevronDown, LucideSearch, LucideX } from '@lucide/angular';

const REMOTE_DEBOUNCE_MS = 300;

/**
 * Dumb Component genérico e reutilizável de Seleção com Busca e Autocomplete.
 * Suporta modo local (filtro em memória de options) e modo remoto (busca via searchFn conectada ao backend).
 * Implementa ControlValueAccessor para integração nativa com Reactive Forms.
 */
@Component({
  selector: 'app-searchable-select',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    LucideSearch,
    LucideX,
    LucideChevronDown,
  ],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => SearchableSelectComponent),
      multi: true,
    },
  ],
  templateUrl: './searchable-select.component.html',
  styleUrl: './searchable-select.component.scss',
})
export class SearchableSelectComponent<T = any> implements ControlValueAccessor, OnInit, OnChanges {
  @Input() options: T[] = [];
  @Input() searchFn?: (term: string) => Observable<T[]>;
  @Input() labelKey?: string;
  @Input() valueKey?: string;
  @Input() placeholder: string = 'Pesquisar...';
  @Input() notFoundText: string = 'Nenhum resultado encontrado.';
  @Input() labelFormatter?: (item: T) => string;
  @Input() searchFunction?: (item: T, term: string) => boolean;
  @Input() isInvalid: boolean = false;
  @Output() selectedOptionChange = new EventEmitter<T | null>();

  private readonly elementRef = inject(ElementRef);
  private readonly destroyRef = inject(DestroyRef);
  private readonly searchInputElement = viewChild<ElementRef<HTMLInputElement>>('searchInput');

  readonly searchControl = new FormControl('', { nonNullable: true });
  readonly isOpen = signal<boolean>(false);
  readonly selectedItem = signal<T | null>(null);
  readonly disabled = signal<boolean>(false);
  readonly searchTerm = signal<string>('');
  readonly remoteOptions = signal<T[]>([]);
  readonly isLoading = signal<boolean>(false);

  private currentValue: any = null;
  private onChange: (value: any) => void = () => { };
  private onTouched: () => void = () => { };

  @HostListener('document:click', ['$event'])
  onClickOutside(event: MouseEvent): void {
    if (!this.elementRef.nativeElement.contains(event.target)) {
      this.close();
    }
  }

  ngOnInit(): void {
    if (this.searchFn) {
      if (this.options.length > 0) {
        this.remoteOptions.set(this.options);
      }
      this.setupRemoteSearch();
    } else {
      this.setupLocalSearch();
    }
  }

  private setupRemoteSearch(): void {
    this.searchControl.valueChanges
      .pipe(
        debounceTime(REMOTE_DEBOUNCE_MS),
        distinctUntilChanged(),
        tap(() => this.isLoading.set(true)),
        switchMap((term) =>
          this.searchFn!(term).pipe(
            catchError(() => of([])),
            finalize(() => this.isLoading.set(false)),
          ),
        ),
        takeUntilDestroyed(this.destroyRef),
      )
      .subscribe((results) => {
        this.remoteOptions.set(results);
        this.syncSelectedItem();
      });
  }

  private setupLocalSearch(): void {
    this.searchControl.valueChanges
      .pipe(
        distinctUntilChanged(),
        takeUntilDestroyed(this.destroyRef),
      )
      .subscribe((term) => {
        this.searchTerm.set(term);
      });
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['options']) {
      if (this.searchFn && this.remoteOptions().length === 0 && this.options.length > 0) {
        this.remoteOptions.set(this.options);
      }
      this.syncSelectedItem();
    }
  }

  get displayedOptions(): T[] {
    if (this.searchFn) {
      return this.remoteOptions();
    }
    return this.filteredLocalOptions;
  }

  get filteredOptions(): T[] {
    return this.displayedOptions;
  }

  private get filteredLocalOptions(): T[] {
    const term = this.searchTerm().trim().toLowerCase();
    if (!term) {
      return this.options;
    }
    if (this.searchFunction) {
      return this.options.filter((item) => this.searchFunction!(item, term));
    }
    return this.options.filter((item) => {
      const label = this.getLabel(item).toLowerCase();
      return label.includes(term);
    });
  }

  getLabel(item: T): string {
    if (!item) return '';
    if (this.labelFormatter) {
      return this.labelFormatter(item);
    }
    if (this.labelKey && typeof item === 'object') {
      return String((item as Record<string, any>)[this.labelKey] ?? '');
    }
    return String(item);
  }

  getItemValue(item: T): any {
    if (!item) return null;
    if (this.valueKey && typeof item === 'object') {
      return (item as Record<string, any>)[this.valueKey];
    }
    return item;
  }

  toggleDropdown(): void {
    if (this.disabled()) return;
    if (this.isOpen()) {
      this.close();
    } else {
      this.open();
    }
  }

  open(): void {
    if (this.disabled()) return;
    this.isOpen.set(true);

    if (this.searchFn && this.remoteOptions().length === 0) {
      this.isLoading.set(true);
      this.searchFn('')
        .pipe(
          catchError(() => of([])),
          finalize(() => this.isLoading.set(false)),
          takeUntilDestroyed(this.destroyRef),
        )
        .subscribe((results) => {
          this.remoteOptions.set(results);
          this.syncSelectedItem();
        });
    }

    setTimeout(() => {
      this.searchInputElement()?.nativeElement.focus();
    }, 0);
  }

  close(): void {
    if (this.isOpen()) {
      this.isOpen.set(false);
      this.onTouched();
    }
  }

  selectItem(item: T): void {
    this.selectedItem.set(item);
    const value = this.getItemValue(item);
    this.currentValue = value;
    this.onChange(value);
    this.onTouched();
    this.selectedOptionChange.emit(item);
    this.close();
    this.searchControl.setValue('');
    this.searchTerm.set('');
  }

  clearSelection(event?: MouseEvent): void {
    if (event) {
      event.stopPropagation();
    }
    this.selectedItem.set(null);
    this.currentValue = null;
    this.onChange(null);
    this.onTouched();
    this.selectedOptionChange.emit(null);
    this.searchControl.setValue('');
    this.searchTerm.set('');
    this.open();
  }

  writeValue(value: any): void {
    this.currentValue = value;
    this.syncSelectedItem();
  }

  registerOnChange(fn: (value: any) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    this.disabled.set(isDisabled);
    if (isDisabled) {
      this.searchControl.disable({ emitEvent: false });
      this.close();
    } else {
      this.searchControl.enable({ emitEvent: false });
    }
  }

  private syncSelectedItem(): void {
    if (this.currentValue === null || this.currentValue === undefined) {
      this.selectedItem.set(null);
      return;
    }

    const availableItems = [...this.remoteOptions(), ...this.options];
    if (!availableItems.length) return;

    const match = availableItems.find((item) => this.getItemValue(item) === this.currentValue);
    if (match) {
      this.selectedItem.set(match);
    }
  }
}
