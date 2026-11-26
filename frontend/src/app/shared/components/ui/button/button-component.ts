import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'button[appButton], a[appButton]',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './button-component.html',
  styleUrl: './button-component.scss',
  host: {
    '[class.variant-primary]': "variant === 'primary'",
    '[class.variant-outline]': "variant === 'outline'",
    '[class.variant-surface]': "variant === 'surface'",
    '[class.size-default]': "size === 'default'",
    '[class.size-icon]': "size === 'icon'",
    '[class.size-circle]': "size === 'circle'",
    '[class.is-loading]': 'loading',
    '[class.is-disabled]': 'disabled || loading',
    '[attr.disabled]': '(loading || disabled) ? true : null',
  },
})
export class ButtonComponent {
  @Input() variant: 'primary' | 'outline' | 'surface' = 'primary';
  @Input() size: 'default' | 'icon' | 'circle' = 'default';

  @Input() loading = false;
  @Input() disabled = false;
}
