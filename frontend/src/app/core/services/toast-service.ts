import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { ToastMessage } from '../interfaces/ToastMessage';

@Injectable({ providedIn: 'root' })
export class ToastService {
  private toastSubject = new Subject<ToastMessage>();
  public toast$ = this.toastSubject.asObservable();

  showError(message: string): void {
    this.toastSubject.next({ message, type: 'error' });
  }

  showWarning(message: string): void {
    this.toastSubject.next({ message, type: 'warning' });
  }

  showSuccess(message: string): void {
    this.toastSubject.next({ message, type: 'success' });
  }
}
