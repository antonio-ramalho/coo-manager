import { Component, Input, Output, EventEmitter } from '@angular/core';
import { ButtonComponent } from '../../components/ui/button/button-component';
import { NgClass } from '@angular/common';

@Component({
  selector: 'app-stepper-layout',
  standalone: true,
  templateUrl: './stepper-layout-component.html',
  styleUrl: './stepper-layout-component.scss',
  imports: [ButtonComponent, NgClass],
})
export class StepperLayoutComponent {
  @Input({ required: true }) steps!: string[];
  @Input({ required: true }) currentStep!: number;
  @Input() isSubmitting!: boolean;

  @Input() nextDisabled: boolean = false;
  @Input() submitDisabled: boolean = false;

  @Output() next = new EventEmitter<void>();
  @Output() back = new EventEmitter<void>();
  @Output() cancel = new EventEmitter<void>();
}
