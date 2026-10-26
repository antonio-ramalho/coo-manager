import { NavSection } from '../models/nav-item.model';

export const NAVIGATION_SECTIONS: NavSection[] = [
  {
    title: 'Menu Principal',
    items: [
      { label: 'Dashboard', route: '/home/dashboard', iconName: 'dashboard' },
      { label: 'Logística', route: '/home/logistica', iconName: 'logistics' },
      { label: 'Financeiro', route: '/home/financeiro', iconName: 'financial' },
    ],
  },
  {
    title: 'Vendas & Gestão',
    items: [
      { label: 'Clientes', route: '/home/clientes', iconName: 'clients' },
      { label: 'Vendas', route: '/home/vendas', iconName: 'sales' },
      { label: 'Insumos', route: '/home/insumos', iconName: 'inputs' },
    ],
  },
];
