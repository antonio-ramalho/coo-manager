import {
  AfterViewInit,
  Component,
  ElementRef,
  forwardRef,
  Input,
  OnChanges,
  OnDestroy,
  SimpleChanges,
  ViewChild,
} from '@angular/core';
import {
  AbstractControl,
  ControlValueAccessor,
  NG_VALIDATORS,
  NG_VALUE_ACCESSOR,
  ValidationErrors,
  Validator,
} from '@angular/forms';
import { LucideCalendar } from '@lucide/angular';
import flatpickr from 'flatpickr';
import { Portuguese } from 'flatpickr/dist/l10n/pt.js';
import { Instance } from 'flatpickr/dist/types/instance';

/**
 * Componente reutilizável de entrada de data em português (pt-BR) que permite
 * digitação direta com máscara `DD/MM/AAAA` e seleção visual via calendário Flatpickr.
 */
@Component({
  selector: 'app-date-input',
  standalone: true,
  imports: [LucideCalendar],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => DateInputComponent),
      multi: true,
    },
    {
      provide: NG_VALIDATORS,
      useExisting: forwardRef(() => DateInputComponent),
      multi: true,
    },
  ],
  templateUrl: './date-input.component.html',
  styleUrls: ['./date-input.component.scss'],
})
export class DateInputComponent
  implements ControlValueAccessor, Validator, AfterViewInit, OnChanges, OnDestroy
{
  @Input() inputId = '';
  @Input() placeholder = 'DD/MM/AAAA';
  @Input() minDate?: string | Date;
  @Input() isInvalid = false;

  @ViewChild('dateInput', { static: true })
  private dateInputRef!: ElementRef<HTMLInputElement>;

  displayValue = '';
  isDisabled = false;

  private fpInstance: Instance | null = null;
  private hasInvalidTypedDate = false;
  private onChange: (value: string | null) => void = () => {};
  private onTouched: () => void = () => {};
  private onValidatorChange: () => void = () => {};

  ngAfterViewInit(): void {
    this.fpInstance = flatpickr(this.dateInputRef.nativeElement, {
      locale: Portuguese,
      dateFormat: 'd/m/Y',
      allowInput: true,
      clickOpens: false,
      disableMobile: true,
      minDate: this.minDate,
      onChange: (selectedDates) => {
        if (selectedDates.length === 0) {
          this.updateFromEmpty();
          return;
        }
        const isoDate = this.toIsoDate(selectedDates[0]);
        this.displayValue = this.toDisplayDate(selectedDates[0]);
        this.hasInvalidTypedDate = false;
        this.onChange(isoDate);
        this.onValidatorChange();
      },
      onClose: () => {
        this.onTouched();
      },
    });

    if (this.displayValue) {
      const parsed = this.parseDisplayDate(this.displayValue);
      if (parsed) {
        this.fpInstance.setDate(parsed, false);
      }
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['minDate'] && this.fpInstance) {
      this.fpInstance.set('minDate', this.minDate);
    }
  }

  ngOnDestroy(): void {
    this.fpInstance?.destroy();
    this.fpInstance = null;
  }

  writeValue(value: string | null): void {
    if (!value) {
      this.displayValue = '';
      this.hasInvalidTypedDate = false;
      this.dateInputRef.nativeElement.value = '';
      this.fpInstance?.clear(false);
      return;
    }

    const parsed = this.parseIsoDate(value);
    if (!parsed) {
      this.displayValue = value;
      this.hasInvalidTypedDate = true;
      this.dateInputRef.nativeElement.value = value;
      return;
    }

    this.hasInvalidTypedDate = false;
    this.displayValue = this.toDisplayDate(parsed);
    this.dateInputRef.nativeElement.value = this.displayValue;
    this.fpInstance?.setDate(parsed, false);
  }

  registerOnChange(fn: (value: string | null) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    this.isDisabled = isDisabled;
    this.dateInputRef.nativeElement.disabled = isDisabled;
  }

  registerOnValidatorChange(fn: () => void): void {
    this.onValidatorChange = fn;
  }

  validate(_control: AbstractControl): ValidationErrors | null {
    return this.hasInvalidTypedDate ? { invalidDate: true } : null;
  }

  openCalendar(): void {
    if (this.isDisabled || !this.fpInstance) return;
    if (this.fpInstance.isOpen) {
      this.fpInstance.close();
      return;
    }
    this.fpInstance.open();
  }

  onTextInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    const digits = input.value.replace(/\D/g, '').slice(0, 8);
    const formatted = this.formatDigits(digits);

    if (input.value !== formatted) {
      input.value = formatted;
    }
    this.displayValue = formatted;

    if (digits.length === 0) {
      this.updateFromEmpty();
      return;
    }

    if (digits.length < 8) {
      this.hasInvalidTypedDate = true;
      this.onChange(null);
      this.onValidatorChange();
      return;
    }

    const parsed = this.parseDisplayDate(formatted);
    if (!parsed) {
      this.hasInvalidTypedDate = true;
      this.onChange(null);
      this.onValidatorChange();
      return;
    }

    this.hasInvalidTypedDate = false;
    this.fpInstance?.setDate(parsed, false);
    this.onChange(this.toIsoDate(parsed));
    this.onValidatorChange();
  }

  onBlur(): void {
    if (!this.fpInstance?.isOpen) {
      this.onTouched();
    }
  }

  private updateFromEmpty(): void {
    this.displayValue = '';
    this.hasInvalidTypedDate = false;
    this.fpInstance?.clear(false);
    this.onChange(null);
    this.onValidatorChange();
  }

  private formatDigits(digits: string): string {
    if (digits.length <= 2) return digits;
    if (digits.length <= 4) return `${digits.slice(0, 2)}/${digits.slice(2)}`;
    return `${digits.slice(0, 2)}/${digits.slice(2, 4)}/${digits.slice(4)}`;
  }

  private parseDisplayDate(value: string): Date | null {
    const match = /^(\d{2})\/(\d{2})\/(\d{4})$/.exec(value);
    if (!match) return null;

    const day = Number(match[1]);
    const month = Number(match[2]);
    const year = Number(match[3]);
    if (year < 1900 || year > 2100 || month < 1 || month > 12 || day < 1 || day > 31) {
      return null;
    }

    const date = new Date(year, month - 1, day);
    const isExactCalendarMatch =
      date.getFullYear() === year &&
      date.getMonth() === month - 1 &&
      date.getDate() === day;

    return isExactCalendarMatch ? date : null;
  }

  private parseIsoDate(value: string): Date | null {
    const isoPart = value.split('T')[0];
    const match = /^(\d{4})-(\d{2})-(\d{2})$/.exec(isoPart);
    if (!match) return null;

    const year = Number(match[1]);
    const month = Number(match[2]);
    const day = Number(match[3]);
    const date = new Date(year, month - 1, day);

    const isExactCalendarMatch =
      date.getFullYear() === year &&
      date.getMonth() === month - 1 &&
      date.getDate() === day;

    return isExactCalendarMatch ? date : null;
  }

  private toDisplayDate(date: Date): string {
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const year = date.getFullYear();
    return `${day}/${month}/${year}`;
  }

  private toIsoDate(date: Date): string {
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const year = date.getFullYear();
    return `${year}-${month}-${day}`;
  }
}

