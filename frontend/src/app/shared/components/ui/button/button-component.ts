import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'button[appButton], a[appButton]',
  standalone: true,
  imports: [CommonModule],
  templateUrl: 'button-component.html',
  host: {
    '[class]': 'baseClasses + " " + variantClasses[variant] + " " + sizeClasses[size]',
    '[class.opacity-70]': 'loading || disabled',
    '[class.cursor-not-allowed]': 'loading || disabled',
    '[attr.disabled]': '(loading || disabled) ? true : null',
  },
})
export class ButtonComponent {
  @Input() variant: 'primary' | 'outline' | 'surface' = 'primary';
  @Input() size: 'default' | 'icon' | 'circle' = 'default';

  @Input() loading = false;
  @Input() disabled = false;

  baseClasses =
    'inline-flex items-center justify-center font-medium transition-all duration-200 focus:outline-none flex-shrink-0';

  variantClasses = {
    primary:
      'bg-[var(--primary-base)] hover:bg-[var(--primary-hover)] !text-white shadow-[var(--shadow-btn)] border border-transparent cursor-pointer',

    outline:
      'bg-[var(--bg-surface)] hover:bg-[var(--bg-surface-hover)] text-[var(--text-main)] border border-[var(--border-neutral)] shadow-[var(--shadow-btn)] cursor-pointer',

    surface:
      'bg-[var(--bg-surface)] text-[var(--text-aux)] border border-[var(--border-neutral)] hover:bg-[var(--primary-light)] hover:text-[var(--primary-base)] hover:border-[var(--primary-base)] shadow-sm cursor-pointer',
  };

  sizeClasses = {
    default: 'px-4 py-2 txt-body rounded-[var(--radius-btn)]',
    icon: 'w-9 h-9 p-2 rounded-[var(--radius-btn)]',
    circle: 'w-10 h-10 p-2 rounded-full',
  };
}
