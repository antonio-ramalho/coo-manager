import {Component} from '@angular/core';
import {CardBoxComponent} from '../../../../shared/components/card-box/card-box';
import {LucideArrowDown} from '@lucide/angular';

@Component({
  selector: 'app-dashboard',
  imports: [
    CardBoxComponent,
    LucideArrowDown
  ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
  providers: []
})
export class DashboardComponent {}
