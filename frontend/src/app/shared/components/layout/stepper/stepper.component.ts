import { Component, EventEmitter, Input, Output } from '@angular/core';
import { NgClass } from '@angular/common';
import { ButtonComponent } from '../../ui/button/button-component';

/**
 * Dumb Component que gerencia a barra de progresso e as ações (anterior, próximo, finalizar) do Stepper.
 */
@Component({
  selector: 'app-stepper-layout',
  imports: [ButtonComponent, NgClass],
  templateUrl: './stepper.component.html',
  styleUrl: './stepper.component.scss',
})
export class StepperComponent {
  @Input({ required: true }) steps!: string[];
  @Input({ required: true }) currentStep!: number;
  @Input() isSubmitting = false;

  @Input() nextDisabled = false;
  @Input() submitDisabled = false;

  @Output() next = new EventEmitter<void>();
  @Output() back = new EventEmitter<void>();
  @Output() cancel = new EventEmitter<void>();
}
