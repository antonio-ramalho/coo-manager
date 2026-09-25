import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { ToastService } from '../../../../core/services/toast-service';
import { ToastMessage } from '../../../../core/interfaces/ToastMessage';

@Component({
  selector: 'app-toast',
  standalone: true,
  templateUrl: './toast-component.html',
})
export class ToastComponent implements OnInit {
  toasts: ToastMessage[] = [];

  toastService = inject(ToastService);
  cdr = inject(ChangeDetectorRef);

  ngOnInit(): void {
    this.toastService.toast$.subscribe((toast) => {
      const toastId = toast.id || Math.random().toString(36).substring(2, 9);
      const newToast = { ...toast, id: toastId };

      this.toasts.push(newToast);
      this.cdr.detectChanges();

      setTimeout(() => {
        this.remove(newToast.id!);
      }, 3000);
    });
  }

  remove(id: number | string): void {
    this.toasts = this.toasts.filter((t) => t.id !== id);
    this.cdr.detectChanges();
  }
}
