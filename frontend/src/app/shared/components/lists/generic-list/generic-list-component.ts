import { Component, EventEmitter, Input, Output } from '@angular/core';
import { PaginationComponent } from '../generic-pagination/pagination-component';
import { SearchBarComponent } from '../search-bar/search-bar-component';
import { NameFormatPipe } from '../../../pipes/name-format-pipe';
import {ISimpleListCard} from '../../../../core/interfaces/ISimpleListCard';
import { GenericCardListComponent } from '../generic-card-list/generic-card-list-component';

@Component({
  selector: 'app-generic-list',
  standalone: true,
  imports: [GenericCardListComponent, PaginationComponent, SearchBarComponent, NameFormatPipe],
  templateUrl: './generic-list-component.html',
  styleUrl: './generic-list-component.scss',
})
export class GenericListComponent {
  @Input() items: ISimpleListCard[] = [];
  @Input() currentPage: number = 1;
  @Input() totalItems: number = 0;
  @Input() altText: string | undefined;

  @Output() viewDetails = new EventEmitter<number>();
  @Output() deactivate = new EventEmitter<number>();
  @Output() activate = new EventEmitter<number>();
  @Output() edit = new EventEmitter<number>();
  @Output() searchChanged = new EventEmitter<string>();
  @Output() pageChanged = new EventEmitter<number>();
}
