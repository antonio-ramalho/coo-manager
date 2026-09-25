import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { ToastMessage } from '../interfaces/ToastMessage';

@Injectable({ providedIn: 'root' })
export class ToastService {
  private toastSubject = new Subject<ToastMessage>();
  public toast$ = this.toastSubject.asObservable();

  showError(message: string): void {
    this.toastSubject.next({ id: Date.now(), message, type: 'error' });
  }

  showWarning(message: string): void {
    this.toastSubject.next({ id: Date.now() + Math.random(), message, type: 'warning' });
  }

  showSuccess(message: string): void {
    this.toastSubject.next({ id: Date.now(), message, type: 'success' });
  }
}
