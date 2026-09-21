import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'button[appButton], a[appButton]',
  standalone: true,
  imports: [CommonModule],
  template: `
    <svg
      *ngIf="loading"
      class="animate-spin -ml-1 mr-2 h-4 w-4 text-current shrink-0"
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
    '[class]': 'baseClasses + " " + variantClasses[variant] + " " + sizeClasses[size]',
    '[class.opacity-70]': 'loading || disabled',
    '[class.cursor-not-allowed]': 'loading || disabled',
    '[attr.disabled]': '(loading || disabled) ? true : null',
  },
})
export class ButtonComponent {
  @Input() variant: 'primary' | 'outline' | 'surface' = 'primary';
  @Input() size: 'default' | 'icon' = 'default';
  @Input() loading = false;
  @Input() disabled = false;

  baseClasses =
    'inline-flex items-center justify-center font-medium transition-all duration-200 focus:outline-none rounded-[var(--radius-btn)] flex-shrink-0';

  variantClasses = {
    primary:
      'bg-[var(--primary-base)] hover:bg-[var(--primary-hover)] !text-white shadow-[var(--shadow-btn)] border border-transparent cursor-pointer',

    outline:
      'bg-[var(--bg-surface)] hover:bg-[var(--bg-surface-hover)] text-[var(--text-main)] border border-[var(--border-neutral)] shadow-[var(--shadow-btn)] cursor-pointer',

    surface:
      'bg-[var(--bg-surface)] text-[var(--text-aux)] border border-[var(--border-neutral)] hover:bg-[var(--primary-light)] hover:text-[var(--primary-base)] hover:border-[var(--primary-base)] shadow-sm cursor-pointer',
  };

  sizeClasses = {
    default: 'px-4 py-2 txt-body',
    icon: 'w-9 h-9 p-2',
  };
}
