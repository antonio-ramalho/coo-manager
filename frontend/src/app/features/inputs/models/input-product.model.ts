// TODO: preencher os campos conforme os DTOs de insumo do backend quando a feature for implementada.
// Até lá, os tipos são opacos (Record vazio) para não aceitar `any` silenciosamente.

/** Insumo completo. */
export type InputProduct = Record<string, never>;

/** Resumo de insumo para listagem paginada. */
export type InputProductSummary = Record<string, never>;

/** Corpo de cadastro de insumo. */
export type InputProductInsert = Record<string, never>;

/** Corpo de atualização de insumo. */
export type InputProductUpdate = Record<string, never>;