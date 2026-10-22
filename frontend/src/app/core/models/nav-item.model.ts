export interface NavItem {
  label: string;
  route: string;
  iconName: 'dashboard' | 'logistics' | 'financial' | 'clients' | 'sales' | 'inputs';
}

export interface NavSection {
  title: string;
  items: NavItem[];
}
