import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CustomValidators } from '../../../shared/validators/custom-validators';


export function buildProductForm(fb: FormBuilder): FormGroup {
  return fb.group({
    productName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
    productCode: ['', [Validators.required, Validators.maxLength(50)]],
    measureUnit: ['', [Validators.required]],
    price: ['', [Validators.required, CustomValidators.positiveNumber]],
    ncm: ['', [Validators.required, CustomValidators.ncm]],
  });
}
