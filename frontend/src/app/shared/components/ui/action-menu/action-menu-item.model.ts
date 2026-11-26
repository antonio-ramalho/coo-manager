import { Permission } from '../../../enums/permission.enum';

export interface ActionMenuItem {
  label: string;
  icon?: any;
  action?: (context?: any) => void;
  danger?: boolean;
  success?: boolean;
  disabled?: boolean;
  permission?: Permission | string;
  dividerAbove?: boolean;
}
