import { Component, EventEmitter, Input, Output } from '@angular/core';
import { StatusPipe } from '../../../pipes/status-pipe';
import { CpfCnpjPipe } from '../../../pipes/cpf-cnpj-pipe';
import { ButtonComponent } from '../../ui/button/button-component';
import { LucideMessageCircle, LucideSquarePen } from '@lucide/angular';

@Component({
  selector: 'app-person-details-header',
  imports: [StatusPipe, CpfCnpjPipe, ButtonComponent, LucideMessageCircle, LucideSquarePen],
  templateUrl: './person-details-header.html',
})
export class PersonDetailsHeaderComponent {
  @Input() id!: number;
  @Input() name!: string;
  @Input() documentNumber!: string;
  @Input() status!: string;

  @Output() editClient = new EventEmitter<number>();

  onEditClient(event: Event): void {
    event.stopPropagation();
    this.editClient.emit(this.id);
  }
}
