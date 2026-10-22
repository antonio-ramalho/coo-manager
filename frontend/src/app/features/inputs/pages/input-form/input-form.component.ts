import { Component, inject } from '@angular/core';
import { Location } from '@angular/common';
import { PageTitleComponent } from '../../../../shared/components/layout/page-title/page-title.component';

/**
 * Placeholder amigável para o formulário de cadastro/edição de insumos.
 * Mantém o mesmo comportamento que anteriormente era roteado para o UniversalForm.
 */
@Component({
  selector: 'app-input-form',
  imports: [PageTitleComponent],
  templateUrl: './input-form.component.html',
})
export class InputFormComponent {
  private readonly location = inject(Location);

  goBack(): void {
    this.location.back();
  }
}
