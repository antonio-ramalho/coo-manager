import { Component, Input } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NgxMaskDirective } from 'ngx-mask';
import { InputErrorComponent } from '../input-error/input-error-component';

@Component({
  selector: 'app-person-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, NgxMaskDirective, InputErrorComponent],
  templateUrl: './person-form-component.html',
})
export class PersonForm {
  @Input({ required: true }) group!: FormGroup;
}
