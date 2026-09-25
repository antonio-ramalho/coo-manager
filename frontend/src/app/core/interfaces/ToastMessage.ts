export interface ToastMessage {
  id: string | number;
  message: string;
  type: 'error' | 'warning' | 'success';
}
