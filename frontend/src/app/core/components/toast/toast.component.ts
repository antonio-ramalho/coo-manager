import { Component, DestroyRef, inject, OnInit, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ToastService } from '../../services/toast.service';
import { ToastMessage } from '../../models/toast-message.model';

const TOAST_DURATION_MS = 3000;

/** Container global de notificações (Smart: consome o `ToastService`). Renderizado uma única vez em `app.html`. */
@Component({
  selector: 'app-toast',
  templateUrl: './toast.component.html',
})
export class ToastComponent implements OnInit {
  private readonly toastService = inject(ToastService);
  private readonly destroyRef = inject(DestroyRef);

  readonly toasts = signal<ToastMessage[]>([]);

  ngOnInit(): void {
    this.toastService.toast$.pipe(takeUntilDestroyed(this.destroyRef)).subscribe((toast) => {
      this.toasts.update((current) => [...current, toast]);
      setTimeout(() => this.remove(toast.id), TOAST_DURATION_MS);
    });
  }

  remove(id: number): void {
    this.toasts.update((current) => current.filter((toast) => toast.id !== id));
  }
}
