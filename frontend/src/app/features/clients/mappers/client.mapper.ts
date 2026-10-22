import { formatDocument } from '../../../shared/utils/document-format';
import { ListCardItem } from '../../../shared/models/list-card-item.model';
import { Status } from '../../../shared/enums/status';
import { Client, ClientDetails, ClientSummary } from '../models/client.model';
import { getPersonTypeRule } from '../forms/person-type-rules';

/** Mapeia um resumo de cliente retornado pela API para o modelo de card da lista. */
export function toListCardItem(client: ClientSummary): ListCardItem {
  return {
    id: client.id,
    title: client.legalName,
    subtitle: formatDocument(client.documentNumber),
    isActive: client.status === Status.ACTIVE,
  };
}

/** Mapeia a entidade completa de cliente para a visualização dos detalhes na UI. */
export function toClientDetails(client: Client): ClientDetails {
  const rule = getPersonTypeRule(client.personType);
  const typeDetails = rule.toDetails(client);

  return {
    id: client.id,
    legalName: client.legalName,
    status: client.status,
    personType: client.personType,
    phone: client.phone,
    email: client.email,
    address: client.address,
    attachments: [],
    documentLabel: rule.documentLabel,
    dateLabel: rule.dateLabel,
    documentNumber: typeDetails.documentNumber,
    dateValue: typeDetails.dateValue,
    gender: typeDetails.gender,
  };
}
