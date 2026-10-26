import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CustomValidators } from '../../../shared/validators/custom-validators';

export function buildBatchForm(fb: FormBuilder, isStandalone: boolean = false): FormGroup {
  return fb.group({
    inputProductId: [null, isStandalone ? [Validators.required] : []],
    batchCode: ['', [Validators.required, Validators.maxLength(50)]],
    quantityValue: ['', [Validators.required, CustomValidators.positiveNumber]],
    expirationDate: ['', [Validators.required, CustomValidators.futureDate]],
  });
}
