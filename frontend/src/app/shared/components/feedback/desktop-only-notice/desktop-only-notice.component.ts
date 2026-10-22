import { Component, Input } from '@angular/core';
import { LucideMonitor } from '@lucide/angular';

/**
 * Dumb component para degradação graciosa em telas de alta densidade de dados.
 * Exibido no mobile para informar amigavelmente que o recurso requer uma tela maior.
 */
@Component({
  selector: 'app-desktop-only-notice',
  imports: [LucideMonitor],
  templateUrl: './desktop-only-notice.component.html',
})
export class DesktopOnlyNoticeComponent {
  @Input({ required: true }) featureName!: string;
}
