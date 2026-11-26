import { NavSection } from '../models/nav-item.model';
import { Permission } from '../../shared/enums/permission.enum';

export const NAVIGATION_SECTIONS: NavSection[] = [
  {
    title: 'Menu Principal',
    items: [
      { label: 'Dashboard', route: '/home/dashboard', iconName: 'dashboard' },
      { label: 'Logística', route: '/home/logistica', iconName: 'logistics', permission: Permission.LOGISTICS_READ },
      { label: 'Financeiro', route: '/home/financeiro', iconName: 'financial', permission: Permission.FINANCIAL_READ },
    ],
  },
  {
    title: 'Vendas & Gestão',
    items: [
      { label: 'Clientes', route: '/home/clientes', iconName: 'clients', permission: Permission.CLIENT_READ },
      { label: 'Vendas', route: '/home/vendas', iconName: 'sales', permission: Permission.SALES_READ },
      { label: 'Insumos', route: '/home/insumos', iconName: 'inputs', permission: Permission.INPUT_READ },
    ],
  },
  {
    title: 'Administração',
    items: [
      { label: 'Cooperativa', route: '/home/cooperativa', iconName: 'cooperative', permission: Permission.COOPERATIVE_READ },
      { label: 'Funcionários', route: '/home/funcionarios', iconName: 'employees', permission: Permission.USER_MANAGE },
    ],
  },
];
