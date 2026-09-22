import { Component, OnInit } from '@angular/core';
import { ToastService } from '../../../core/services/toast-service';
import { ToastMessage } from '../../../core/interfaces/ToastMessage';

@Component({
  selector: 'app-toast',
  standalone: true,
  templateUrl: './toast-component.html',
})
export class ToastComponent implements OnInit {
  toasts: ToastMessage[] = [];

  constructor(private toastService: ToastService) {}

  ngOnInit(): void {
    this.toastService.toast$.subscribe((toast) => {
      this.toasts.push(toast);
      setTimeout(() => {
        this.toasts.shift();
      }, 1000);
    });
  }

  remove(index: number): void {
    this.toasts.splice(index, 1);
  }
}
