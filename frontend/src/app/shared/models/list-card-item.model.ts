/** Item genérico exibido nos cards das listas laterais (clientes, insumos, ...). */
export interface ListCardItem {
  id: number;
  title: string;
  subtitle: string;
  isActive: boolean;
}
