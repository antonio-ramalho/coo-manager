import { Component, DestroyRef, OnInit, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ToastService } from '../../services/toast.service';
import { ToastMessage } from '../../models/toast-message.model';

const TOAST_DURATION_MS = 3500;
const MAX_VISIBLE_TOASTS = 3;

@Component({
  selector: 'app-toast',
  standalone: true,
  imports: [],
  templateUrl: './toast.component.html',
})
export class ToastComponent implements OnInit {
  private readonly toastService = inject(ToastService);
  private readonly destroyRef = inject(DestroyRef);
  private readonly timers = new Map<number, ReturnType<typeof setTimeout>>();

  readonly toasts = signal<ToastMessage[]>([]);

  ngOnInit(): void {
    this.toastService.toast$.pipe(takeUntilDestroyed(this.destroyRef)).subscribe((toast) => {
      this.enqueueToast(toast);
    });

    this.destroyRef.onDestroy(() => {
      this.timers.forEach((timer) => clearTimeout(timer));
      this.timers.clear();
    });
  }

  remove(id: number): void {
    const timer = this.timers.get(id);
    if (timer) {
      clearTimeout(timer);
      this.timers.delete(id);
    }
    this.toasts.update((current) => current.filter((toast) => toast.id !== id));
  }

  private enqueueToast(incoming: ToastMessage): void {
    const currentList = this.toasts();
    const isDuplicate = currentList.some(
      (existing) => existing.type === incoming.type && existing.message === incoming.message
    );
    if (isDuplicate) {
      return;
    }

    const sorted = [...currentList, incoming]
      .sort((a, b) => b.priority - a.priority || b.id - a.id)
      .slice(0, MAX_VISIBLE_TOASTS);

    const visibleIds = new Set(sorted.map((item) => item.id));
    currentList.forEach((existing) => {
      if (!visibleIds.has(existing.id)) {
        const timer = this.timers.get(existing.id);
        if (timer) {
          clearTimeout(timer);
          this.timers.delete(existing.id);
        }
      }
    });

    this.toasts.set(sorted);

    if (visibleIds.has(incoming.id)) {
      const timer = setTimeout(() => this.remove(incoming.id), TOAST_DURATION_MS);
      this.timers.set(incoming.id, timer);
    }
  }
}
