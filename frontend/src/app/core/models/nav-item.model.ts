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
  permission?: string;
  permissions?: string[];
}

export interface NavSection {
  title: string;
  items: NavItem[];
}
