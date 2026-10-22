import { Component, EventEmitter, Input, Output } from '@angular/core';
import { LucideArrowLeft } from '@lucide/angular';
import { ButtonComponent } from '../../ui/button/button-component';

/**
 * Dumb component para título de tela, com botão opcional de voltar.
 */
@Component({
  selector: 'app-page-title',
  imports: [LucideArrowLeft, ButtonComponent],
  templateUrl: './page-title.component.html',
})
export class PageTitleComponent {
  @Input({ required: true }) title!: string;
  @Input() showBackButton = false;

  @Output() back = new EventEmitter<void>();
}
