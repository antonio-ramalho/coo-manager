import { Component, Input } from '@angular/core';

export type SpinnerSize = 'sm' | 'md' | 'lg';

/**
 * Dumb Component reutilizável para indicação visual de carregamento assíncrono.
 */
@Component({
  selector: 'app-loading-spinner',
  standalone: true,
  templateUrl: './loading-spinner.component.html',
  styleUrl: './loading-spinner.component.scss',
})
export class LoadingSpinnerComponent {
  @Input() message?: string;
  @Input() size: SpinnerSize = 'md';
}

