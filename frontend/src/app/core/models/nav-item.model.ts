import { Permission } from '../../shared/enums/permission.enum';

export interface NavItem {
  label: string;
  route: string;
  iconName:
  | 'dashboard'
  | 'logistics'
  | 'financial'
  | 'clients'
  | 'sales'
  | 'inputs'
  | 'cooperative'
  | 'employees';
  permission?: Permission | string;
  permissions?: (Permission | string)[];
}

export interface NavSection {
  title: string;
  items: NavItem[];
}
