import { Component, EventEmitter, Input, Output, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BreakpointObserver } from '@angular/cdk/layout';
import { toSignal } from '@angular/core/rxjs-interop';
import { map } from 'rxjs';
import { PageTitleComponent } from '../../page-title/page-title.component';
import { DesktopOnlyNoticeComponent } from '../../../feedback/desktop-only-notice/desktop-only-notice.component';

/**
 * Dumb Component de Layout para formulários padrão do sistema (como cadastros de Clientes, Insumos e Lotes).
 * Centraliza a responsividade desktop-only, cabeçalho de página com botão de voltar e container padronizado.
 */
@Component({
  selector: 'app-standard-form-layout',
  standalone: true,
  imports: [
    CommonModule,
    PageTitleComponent,
    DesktopOnlyNoticeComponent,
  ],
  templateUrl: './standard-form-layout.component.html',
  styleUrls: ['./standard-form-layout.component.scss'],
})
export class StandardFormLayoutComponent {
  private readonly breakpointObserver = inject(BreakpointObserver);

  readonly isDesktop = toSignal(
    this.breakpointObserver.observe('(min-width: 768px)').pipe(map((result) => result.matches)),
    { initialValue: true },
  );

  @Input({ required: true }) title!: string;
  @Input() featureName?: string;
  @Input() showBackButton = true;

  @Output() cancel = new EventEmitter<void>();
}
