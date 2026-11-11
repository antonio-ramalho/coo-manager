import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { ToastMessage, ToastType } from '../models/toast-message.model';

const TOAST_PRIORITY_MAP: Record<ToastType, number> = {
  error: 3,
  warning: 2,
  success: 1,
};

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
    const normalized = message.trim();
    if (!normalized) {
      return;
    }

    this.toastSubject.next({
      id: ++this.nextId,
      message: normalized,
      type,
      priority: TOAST_PRIORITY_MAP[type],
    });
  }
}
