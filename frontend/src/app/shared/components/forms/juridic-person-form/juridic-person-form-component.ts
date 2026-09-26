import { Component, Input } from '@angular/core';
import { FlatpickrDirective } from 'angularx-flatpickr';
import { FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgxMaskDirective } from 'ngx-mask';
import { InputErrorComponent } from '../input-error/input-error-component';

@Component({
  selector: 'app-juridic-person-form',
  imports: [
    FlatpickrDirective,
    FormsModule,
    NgxMaskDirective,
    ReactiveFormsModule,
    InputErrorComponent,
  ],
  templateUrl: './juridic-person-form-component.html',
})
export class JuridicPersonFormComponent {
  @Input({ required: true }) group!: FormGroup;
}
