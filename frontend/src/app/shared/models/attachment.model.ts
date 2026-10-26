/** Arquivo anexado a um cadastro (documentos, comprovantes etc.). */
export interface Attachment {
  id: number;
  url: string;
  documentName: string;
  /** Tamanho já formatado para exibição (ex.: "1,2 MB"). */
  size: string;
}
