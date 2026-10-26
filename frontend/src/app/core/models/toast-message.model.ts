export type ToastType = 'error' | 'warning' | 'success';

/** Notificação efêmera exibida no canto da tela. */
export interface ToastMessage {
  id: number;
  message: string;
  type: ToastType;
}
