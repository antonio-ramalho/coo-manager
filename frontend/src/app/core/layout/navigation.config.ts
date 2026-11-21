import { NavSection } from '../models/nav-item.model';

export const NAVIGATION_SECTIONS: NavSection[] = [
  {
    title: 'Menu Principal',
    items: [
      { label: 'Dashboard', route: '/home/dashboard', iconName: 'dashboard' },
      { label: 'Logística', route: '/home/logistica', iconName: 'logistics', permission: 'LOGISTICS_READ' },
      { label: 'Financeiro', route: '/home/financeiro', iconName: 'financial', permission: 'FINANCIAL_READ' },
    ],
  },
  {
    title: 'Vendas & Gestão',
    items: [
      { label: 'Clientes', route: '/home/clientes', iconName: 'clients', permission: 'CLIENT_READ' },
      { label: 'Vendas', route: '/home/vendas', iconName: 'sales', permission: 'SALES_READ' },
      { label: 'Insumos', route: '/home/insumos', iconName: 'inputs', permission: 'INPUT_READ' },
    ],
  },
  {
    title: 'Administração',
    items: [
      { label: 'Cooperativa', route: '/home/cooperativa', iconName: 'cooperative', permission: 'COOPERATIVE_READ' },
      { label: 'Funcionários', route: '/home/funcionarios', iconName: 'employees', permission: 'USER_MANAGE' },
    ],
  },
];
