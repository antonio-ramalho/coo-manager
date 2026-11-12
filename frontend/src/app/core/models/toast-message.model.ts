export type ToastType = 'error' | 'warning' | 'success';

export interface ToastMessage {
  id: number;
  message: string;
  type: ToastType;
  priority: number;
}
