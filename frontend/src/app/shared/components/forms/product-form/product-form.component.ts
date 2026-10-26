import { Component, DestroyRef, inject, Input, OnInit, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { debounceTime, distinctUntilChanged, switchMap, tap } from 'rxjs';
import { NgxMaskDirective } from 'ngx-mask';
import { InputErrorComponent } from '../input-error/input-error-component';
import { MeasureUnit } from '../../../enums/measure-unit';
import { MeasureUnitPipe } from '../../../pipes/measure-unit-pipe';
import { NcmItem, NcmLookupService } from '../../../../core/services/ncm-lookup.service';

const DEBOUNCE_TIME_MS = 350;

/**
 * Dumb Component responsável pelos campos do formulário de Produto (Catálogo).
 * Gerencia a apresentação, validações visuais e preenchimento automático de NCM via BrasilAPI ao digitar o nome.
 */
@Component({
  selector: 'app-product-form',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    NgxMaskDirective,
    MeasureUnitPipe,
    InputErrorComponent,
  ],
  templateUrl: './product-form.component.html',
  styleUrl: './product-form.component.scss',
})
export class ProductFormComponent implements OnInit {
  @Input({ required: true }) group!: FormGroup;

  private readonly ncmLookupService = inject(NcmLookupService);
  private readonly destroyRef = inject(DestroyRef);

  readonly measureUnitOptions = Object.values(MeasureUnit);
  readonly ncmSuggestions = signal<NcmItem[]>([]);
  readonly isSearchingNcm = signal<boolean>(false);
  readonly selectedNcmDescription = signal<string | null>(null);

  ngOnInit(): void {
    this.listenToProductNameChanges();
    this.listenToNcmChanges();
  }

  private listenToProductNameChanges(): void {
    this.group
      .get('productName')
      ?.valueChanges.pipe(
        debounceTime(DEBOUNCE_TIME_MS),
        distinctUntilChanged(),
        tap((name: string | null) => {
          if (!name || name.trim().length < 3) {
            this.ncmSuggestions.set([]);
            this.isSearchingNcm.set(false);
          } else {
            this.isSearchingNcm.set(true);
          }
        }),
        switchMap((name: string | null) => {
          if (!name || name.trim().length < 3) {
            return [];
          }
          return this.ncmLookupService.searchByTerm(name);
        }),
        takeUntilDestroyed(this.destroyRef),
      )
      .subscribe({
        next: (items) => {
          this.isSearchingNcm.set(false);
          if (items && items.length > 0) {
            const first = items[0];
            this.group.get('ncm')?.setValue(first.codigo);
            this.group.get('ncm')?.markAsDirty();
            this.selectedNcmDescription.set(this.formatDescription(first.descricao));

            if (items.length > 1) {
              this.ncmSuggestions.set(items.slice(0, 5));
            } else {
              this.ncmSuggestions.set([]);
            }
          } else {
            this.ncmSuggestions.set([]);
          }
        },
        error: () => {
          this.isSearchingNcm.set(false);
          this.ncmSuggestions.set([]);
        },
      });
  }

  private listenToNcmChanges(): void {
    this.group
      .get('ncm')
      ?.valueChanges.pipe(
        debounceTime(DEBOUNCE_TIME_MS),
        distinctUntilChanged(),
        switchMap((ncmValue: string | null) => {
          const clean = this.ncmLookupService.clean(ncmValue || '');
          if (clean.length === 8) {
            return this.ncmLookupService.lookupByCode(clean);
          }
          this.selectedNcmDescription.set(null);
          return [];
        }),
        takeUntilDestroyed(this.destroyRef),
      )
      .subscribe((item) => {
        if (item && 'descricao' in item) {
          this.selectedNcmDescription.set(this.formatDescription(item.descricao));
        } else {
          this.selectedNcmDescription.set(null);
        }
      });
  }

  selectNcm(item: NcmItem): void {
    this.group.get('ncm')?.setValue(item.codigo);
    this.group.get('ncm')?.markAsDirty();
    this.group.get('ncm')?.markAsTouched();
    this.selectedNcmDescription.set(this.formatDescription(item.descricao));
    this.ncmSuggestions.set([]);
  }

  closeSuggestions(): void {
    this.ncmSuggestions.set([]);
  }

  private formatDescription(desc: string | undefined): string {
    if (!desc) return '';
    return desc.replace(/<[^>]*>/g, '').replace(/^--\s*/, '').trim();
  }
}
