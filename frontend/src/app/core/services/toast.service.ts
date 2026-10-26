import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { ToastMessage, ToastType } from '../models/toast-message.model';

/** Barramento global de notificações; o `ToastComponent` é o único consumidor. */
@Injectable({ providedIn: 'root' })
export class ToastService {
  private readonly toastSubject = new Subject<ToastMessage>();
  private nextId = 0;

  readonly toast$ = this.toastSubject.asObservable();

  showError(message: string): void {
    this.emit(message, 'error');
  }

  showWarning(message: string): void {
    this.emit(message, 'warning');
  }

  showSuccess(message: string): void {
    this.emit(message, 'success');
  }

  private emit(message: string, type: ToastType): void {
    this.toastSubject.next({ id: ++this.nextId, message, type });
  }
}
