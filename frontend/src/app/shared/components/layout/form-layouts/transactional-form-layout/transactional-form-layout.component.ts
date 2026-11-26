import { Component, EventEmitter, Input, Output, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BreakpointObserver } from '@angular/cdk/layout';
import { toSignal } from '@angular/core/rxjs-interop';
import { map } from 'rxjs';
import { PageTitleComponent } from '../../page-title/page-title.component';
import { DesktopOnlyNoticeComponent } from '../../../feedback/desktop-only-notice/desktop-only-notice.component';

/**
 * Dumb Component de Layout para fluxos de transação complexos (como Nova Venda / Saída de Estoque).
 * Centraliza o cabeçalho padronizado e a responsividade desktop-only.
 */
@Component({
  selector: 'app-transactional-form-layout',
  standalone: true,
  imports: [
    CommonModule,
    PageTitleComponent,
    DesktopOnlyNoticeComponent,
  ],
  templateUrl: './transactional-form-layout.component.html',
  styleUrls: ['./transactional-form-layout.component.scss'],
})
export class TransactionalFormLayoutComponent {
  private readonly breakpointObserver = inject(BreakpointObserver);

  readonly isDesktop = toSignal(
    this.breakpointObserver.observe('(min-width: 768px)').pipe(map((result) => result.matches)),
    { initialValue: true },
  );

  @Input({ required: true }) title!: string;
  @Input() featureName?: string;

  @Output() cancel = new EventEmitter<void>();
}
