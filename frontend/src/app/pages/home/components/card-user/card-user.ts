import {Component, Input} from '@angular/core';
import {CardBoxComponent} from '../../../../shared/components/card-box/card-box';

@Component({
  selector: 'app-card-user',
  imports: [CardBoxComponent],
  templateUrl: './card-user.html',
  styleUrl: './card-user.css',
})
export class CardUserComponent {
  @Input() dados!: { nome: string; cargo: string; fotoUrl: string; };
}
