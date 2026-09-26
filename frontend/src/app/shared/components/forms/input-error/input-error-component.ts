import { Component, Input } from '@angular/core';
import { AbstractControl } from '@angular/forms';

@Component({
  selector: 'app-input-error',
  standalone: true,
  templateUrl: './input-error-component.html',
})
export class InputErrorComponent {
  @Input({ required: true }) control!: AbstractControl | null;
}
