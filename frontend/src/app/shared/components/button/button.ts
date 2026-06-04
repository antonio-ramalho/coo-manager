import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'button[appButton]',
  standalone: true,
  imports: [CommonModule],
  template: `
    <svg
      *ngIf="loading"
      class="animate-spin -ml-1 mr-2 h-4 w-4 text-current"
      xmlns="http://www.w3.org/2000/svg"
      fill="none"
      viewBox="0 0 24 24"
    >
      <circle
        class="opacity-25"
        cx="12"
        cy="12"
        r="10"
        stroke="currentColor"
        stroke-width="4"
      ></circle>
      <path
        class="opacity-75"
        fill="currentColor"
        d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
      ></path>
    </svg>
    <ng-content></ng-content>
  `,
  host: {
    '[class]': 'baseClasses + " " + variantClasses[variant]',
    '[class.opacity-70]': 'loading || disabled',
    '[class.cursor-not-allowed]': 'loading || disabled',
    '[attr.disabled]': '(loading || disabled) ? true : null',
  },
})
export class ButtonComponent {
  @Input() variant: 'primary' | 'secondary' = 'primary';
  @Input() loading = false;
  @Input() disabled = false;

  baseClasses =
    'inline-flex items-center justify-center font-medium transition-colors duration-200 focus:outline-none text-sm';

  variantClasses = {
    primary:
      'bg-[#2E7D32] hover:bg-[#1B5E20] text-white rounded-[8px] px-4 py-2 shadow-[var(--shadow-btn)]',
    secondary:
      'bg-white hover:bg-gray-50 text-gray-700 border border-gray-300 rounded-[8px] px-4 py-2 shadow-[var(--shadow-btn)]',
  };
}
