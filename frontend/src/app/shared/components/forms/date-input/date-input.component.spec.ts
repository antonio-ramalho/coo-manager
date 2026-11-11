import { Component } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormControl, ReactiveFormsModule, Validators } from '@angular/forms';
import { beforeEach, describe, expect, it } from 'vitest';
import { DateInputComponent } from './date-input.component';

@Component({
  standalone: true,
  imports: [ReactiveFormsModule, DateInputComponent],
  template: `<app-date-input inputId="testDate" [formControl]="control"></app-date-input>`,
})
class TestHostComponent {
  control = new FormControl<string | null>(null, [Validators.required]);
}

describe('DateInputComponent', () => {
  let fixture: ComponentFixture<TestHostComponent>;
  let host: TestHostComponent;
  let inputEl: HTMLInputElement;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TestHostComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(TestHostComponent);
    host = fixture.componentInstance;
    fixture.detectChanges();
    inputEl = fixture.nativeElement.querySelector('input') as HTMLInputElement;
  });

  it('should display ISO value formatted as DD/MM/AAAA', () => {
    host.control.setValue('1990-08-15');
    fixture.detectChanges();

    expect(inputEl.value).toBe('15/08/1990');
    expect(host.control.valid).toBe(true);
  });

  it('should mask typed digits into DD/MM/AAAA and emit ISO date', () => {
    inputEl.value = '15081990';
    inputEl.dispatchEvent(new Event('input'));
    fixture.detectChanges();

    expect(inputEl.value).toBe('15/08/1990');
    expect(host.control.value).toBe('1990-08-15');
    expect(host.control.valid).toBe(true);
  });

  it('should mark control with invalidDate when typed calendar date does not exist', () => {
    inputEl.value = '31021990';
    inputEl.dispatchEvent(new Event('input'));
    fixture.detectChanges();

    expect(inputEl.value).toBe('31/02/1990');
    expect(host.control.value).toBeNull();
    expect(host.control.hasError('invalidDate')).toBe(true);
  });

  it('should mark control with invalidDate when partially typed and clear error when emptied', () => {
    inputEl.value = '1508';
    inputEl.dispatchEvent(new Event('input'));
    fixture.detectChanges();

    expect(inputEl.value).toBe('15/08');
    expect(host.control.hasError('invalidDate')).toBe(true);

    inputEl.value = '';
    inputEl.dispatchEvent(new Event('input'));
    fixture.detectChanges();

    expect(host.control.hasError('invalidDate')).toBe(false);
    expect(host.control.hasError('required')).toBe(true);
  });
});

