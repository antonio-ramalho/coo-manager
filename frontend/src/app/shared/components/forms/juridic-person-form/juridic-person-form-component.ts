import { Component, Input } from '@angular/core';
import { FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgxMaskDirective } from 'ngx-mask';
import { DateInputComponent } from '../date-input/date-input.component';
import { InputErrorComponent } from '../input-error/input-error-component';

@Component({
  selector: 'app-juridic-person-form',
  imports: [
    DateInputComponent,
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
