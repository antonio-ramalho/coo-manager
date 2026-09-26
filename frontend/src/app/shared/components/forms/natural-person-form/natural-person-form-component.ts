import { Component, Input } from '@angular/core';
import { FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgxMaskDirective } from 'ngx-mask';
import { FlatpickrDirective } from 'angularx-flatpickr';
import { Gender } from '../../../enums/gender';
import { GenderPipe } from '../../../pipes/gender-pipe';
import { InputErrorComponent } from '../input-error/input-error-component';

@Component({
  selector: 'app-natural-person-form-component',
  imports: [
    FormsModule,
    ReactiveFormsModule,
    NgxMaskDirective,
    FlatpickrDirective,
    GenderPipe,
    InputErrorComponent,
  ],
  templateUrl: './natural-person-form-component.html',
})
export class NaturalPersonFormComponent {
  @Input({ required: true }) group!: FormGroup;

  genderOptions = Object.values(Gender);
}
