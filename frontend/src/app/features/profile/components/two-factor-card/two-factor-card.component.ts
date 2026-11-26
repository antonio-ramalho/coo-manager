import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  LucideAlertTriangle,
  LucideCheckCircle2,
  LucideShield,
  LucideShieldCheck,
  LucideSmartphone,
} from '@lucide/angular';
import { ButtonComponent } from '../../../../shared/components/ui/button/button-component';

@Component({
  selector: 'app-two-factor-card',
  standalone: true,
  imports: [
    CommonModule,
    LucideShield,
    LucideShieldCheck,
    LucideSmartphone,
    LucideCheckCircle2,
    LucideAlertTriangle,
    ButtonComponent,
  ],
  templateUrl: './two-factor-card.component.html',
  styleUrl: './two-factor-card.component.scss',
})
export class TwoFactorCardComponent {
  @Input({ required: true }) twoFactorEnabled = false;

  @Output() startSetup = new EventEmitter<void>();
  @Output() startDisable = new EventEmitter<void>();

  onSetup(): void {
    this.startSetup.emit();
  }

  onDisable(): void {
    this.startDisable.emit();
  }
}
