import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Location, TitleCasePipe } from '@angular/common';
import { ButtonComponent } from '../../shared/components/ui/button/button-component';
import { LucideArrowLeft } from '@lucide/angular';
import { ClientFormComponent } from '../client/components/client-form/client-form-component';

@Component({
  selector: 'app-universal-form-page-component',
  standalone: true,
  imports: [TitleCasePipe, ButtonComponent, LucideArrowLeft, ClientFormComponent],
  templateUrl: './universal-form-component.html',
  styleUrl: './universal-form-component.scss',
})
export class UniversalFormComponent implements OnInit {
  formType: string = '';

  route = inject(ActivatedRoute);
  router = inject(Router);
  location = inject(Location);

  ngOnInit() {
    this.route.data.subscribe((data) => {
      this.formType = data['type'];
    });
  }

  goBack() {
    this.location.back();
  }
}
