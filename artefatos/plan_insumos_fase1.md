# Plano de Execução Técnico - Fase 1: Insumos e Estoque (Catálogo e Inventário)

## 1. Confirmação do Entendimento da Arquitetura Atual

A análise da base de código confirma que a arquitetura do projeto segue padrões rigorosos que serão replicados integralmente no módulo de Insumos:

- **Módulo de Referência (`clients`)**:
  * **Feature-Driven & Lazy Loading**: Roteamento desacoplado em `features/inputs/inputs.routes.ts`, carregado sob demanda via funções no `app.routes.ts`. Separação estrita de Smart Components (`clients-management`) e Dumb/UI Components (`client-details-panel`, `button`), formulários isolados em `forms/client-form.builder.ts` e mappers bidirecionais.
  * **Bloqueio Mobile Gracioso**: Uso de `BreakpointObserver` (`toSignal(this.breakpointObserver.observe('(min-width: 768px)'))`) e diretiva `@if (!isDesktop())` para renderizar `<app-desktop-only-notice>` (sem disparar requisições ou subscrições em dispositivos móveis).
  * **SubNav Contextual**: O Shell gerencia as abas superiores (`Estoque` e `Catálogo`) via `SubNavService`, ocultando-as em telas de cadastro e edição através de `data: { hideSubNav: true }`.
  * **Controle de Altura**: Containers travados com `flex-1 min-h-0 overflow-hidden` para garantir rolagens independentes e evitar quebras de viewport.
  * **Reuso da Lista Genérica**: O componente `<app-generic-list>` (`GenericListComponent`) está pronto para reuso imediato, operando sobre o contrato `ListCardItem` (`id`, `title`, `subtitle`, `isActive`), com busca e paginação acopladas.

- **Domínio e Backend (Spring Boot & Java 25)**:
  * **DDD e Contextos**: Divisão entre `produtcModule` (`Product`, `InputProduct`) e `purchaseModule` (`InputBatch`, `InputPurchase`, `InputPurchaseItem`).
  * **SOLID e BI**: Desacoplamento através de interfaces de serviço, aplicação do padrão Strategy para validações de lotes e desenvolvimento de consultas customizadas de BI (Valor Total em Estoque, Ocupação/Status, Lotes Vencendo e Ranking de Vendas por período).
  * **Testes Unitários Automatizados**: Garantia da cobertura das regras de negócio críticas (estoque, status de pagamento, cancelamento, lote expirado e BI) isoladamente com JUnit 5 e Mockito.

---

## 2. Divisão Técnica em Sprints Controladas (1 a 5)

### Sprint 1: Backend — Refatoração SOLID, BI do Estoque e Testes Unitários de Negócio
**Objetivo**: Desacoplar serviços com interfaces, isolar validações via Strategy, implementar endpoints/consultas de BI e cobrir com testes unitários as regras críticas de compras, lotes e dashboard.

- **Arquivos a Criar**:
  - `backend/src/main/java/com/manager/coopafi/purchaseModule/services/interfaces/InputBatchService.java` [NEW] *(Contrato de interface do serviço de lotes)*
  - `backend/src/main/java/com/manager/coopafi/purchaseModule/services/interfaces/InputPurchaseService.java` [NEW] *(Contrato de interface do serviço de compras)*
  - `backend/src/main/java/com/manager/coopafi/purchaseModule/services/interfaces/InventoryDashboardService.java` [NEW] *(Interface para consultas analíticas de estoque)*
  - `backend/src/main/java/com/manager/coopafi/purchaseModule/services/impl/InventoryDashboardServiceImpl.java` [NEW] *(Implementação de valor total em estoque, status de ocupação, lotes a vencer e ranking)*
  - `backend/src/main/java/com/manager/coopafi/purchaseModule/resources/InventoryDashboardResource.java` [NEW] *(Endpoint REST `GET /input-batch/dashboard`)*
  - `backend/src/main/java/com/manager/coopafi/purchaseModule/dto/dashboard/InventoryDashboardDto.java` [NEW] *(DTO mestre do painel analítico)*
  - `backend/src/main/java/com/manager/coopafi/purchaseModule/dto/dashboard/StockOccupancyDto.java` [NEW] *(Quantidades por status: Em estoque, Baixo, Esgotado)*
  - `backend/src/main/java/com/manager/coopafi/purchaseModule/dto/dashboard/ExpiringBatchDto.java` [NEW] *(Lotes próximos ao vencimento por período)*
  - `backend/src/main/java/com/manager/coopafi/purchaseModule/dto/dashboard/TopSellingProductDto.java` [NEW] *(Ranking dos mais vendidos agrupado por período)*
  - `backend/src/main/java/com/manager/coopafi/purchaseModule/services/strategy/BatchValidationStrategy.java` [NEW] *(Strategy para validações de lotes)*
  - `backend/src/test/java/com/manager/coopafi/purchaseModule/services/InventoryDashboardServiceTest.java` [NEW] *(Testes unitários: cálculo de valor total, contagem de ocupação, filtros de vencimento [3, 7, 30 dias] e ordenação do ranking)*
  - `backend/src/test/java/com/manager/coopafi/purchaseModule/services/InputBatchServiceTest.java` [NEW] *(Testes unitários: inserção com validação de produto ativo, atualização de validade, dedução/adição de quantidade e transição para OUT_OF_STOCK / EXPIRED)*
  - `backend/src/test/java/com/manager/coopafi/purchaseModule/services/InputPurchaseServiceTest.java` [NEW] *(Testes unitários: processamento de compra com baixa de lote, validação de itens mínimos, estorno de estoque no cancelamento e trava de alteração de compras pagas/canceladas)*

- **Arquivos a Alterar / Mover**:
  - `backend/src/main/java/com/manager/coopafi/purchaseModule/repositories/InputBatchRepository.java` [MODIFY] *(Queries otimizadas de soma de valor, ocupação e filtros de validade)*
  - `backend/src/main/java/com/manager/coopafi/purchaseModule/repositories/InputPurchaseRepository.java` [MODIFY] *(Query agregada para ranking de saídas por período)*
  - `backend/src/main/java/com/manager/coopafi/purchaseModule/services/InputBatchService.java` -> renomear/implementar como `InputBatchServiceImpl.java` [MODIFY]
  - `backend/src/main/java/com/manager/coopafi/purchaseModule/services/InputPurchaseService.java` -> renomear/implementar como `InputPurchaseServiceImpl.java` [MODIFY]
  - `backend/src/main/java/com/manager/coopafi/purchaseModule/resources/InputBatchResources.java` [MODIFY] *(Injeção da interface do serviço)*

---

### Sprint 2: Frontend — Estrutura Base, Shell, Roteamento Lazy e Modelos
**Objetivo**: Padronizar o módulo de insumos (`features/inputs`), configurar o `InputsShellComponent` com trava de altura estrita (`flex-1 min-h-0`), abas contextuais na Topbar e camadas de serviço/modelos.

- **Arquivos a Criar**:
  - `frontend/src/app/features/inputs/models/inventory-dashboard.model.ts` [NEW] *(Interfaces tipadas para KPIs, barra de ocupação, lotes vencendo e ranking)*
  - `frontend/src/app/features/inputs/models/input-batch.model.ts` [NEW] *(Interface do lote para a tabela densa)*
  - `frontend/src/app/features/inputs/mappers/input-product.mapper.ts` [NEW] *(Conversor de InputProduct para o ListCardItem do app-generic-list)*
  - `frontend/src/app/features/inputs/data/inventory-api.service.ts` [NEW] *(Consumo HTTP do endpoint de BI/Dashboard)*
  - `frontend/src/app/features/inputs/data/input-batch-api.service.ts` [NEW] *(Consumo HTTP do CRUD de lotes)*

- **Arquivos a Alterar**:
  - `frontend/src/app/features/inputs/models/input-product.model.ts` [MODIFY] *(Tipagem completa dos DTOs de produto)*
  - `frontend/src/app/features/inputs/inputs.routes.ts` [MODIFY] *(Definição das rotas: `estoque`, `catalogo`, `cadastro`, `editar/:id` com `data: { hideSubNav: true }`)*
  - `frontend/src/app/features/inputs/pages/inputs-shell/inputs-shell.component.ts` [MODIFY] *(Registro de abas 'Estoque' e 'Catálogo' com ciclo de vida gerenciado)*
  - `frontend/src/app/features/inputs/pages/inputs-shell/inputs-shell.component.html` [MODIFY] *(Estrutura CSS restritiva `flex flex-col flex-1 h-full min-h-0 w-full overflow-hidden`)*

---

### Sprint 3: Frontend — Catálogo de Insumos (`InputCatalogComponent`)
**Objetivo**: Implementar a visualização de Catálogo espelhando fielmente o layout de referência (Imagem 1: Lista genérica à esquerda, busca, paginação, bloqueio mobile e painel de detalhes/ações à direita).

- **Arquivos a Criar**:
  - `frontend/src/app/features/inputs/components/input-product-details-panel/input-product-details-panel.component.ts` [NEW] *(Painel de detalhes do produto selecionado)*
  - `frontend/src/app/features/inputs/components/input-product-details-panel/input-product-details-panel.component.html` [NEW]
  - `frontend/src/app/features/inputs/components/input-product-details-panel/input-product-details-panel.component.scss` [NEW]

- **Arquivos a Alterar**:
  - `frontend/src/app/features/inputs/pages/input-catalog/input-catalog.component.ts` [MODIFY] *(Smart Component com `isDesktop`, integração reativa com `InputProductApiService`, mapeamento para `ListCardItem` e paginação)*
  - `frontend/src/app/features/inputs/pages/input-catalog/input-catalog.component.html` [MODIFY] *(Bloqueio mobile com `<app-desktop-only-notice>` e split container com `<app-generic-list>` e painel de detalhes)*
  - `frontend/src/app/features/inputs/pages/input-catalog/input-catalog.component.scss` [MODIFY] *(Estilização do layout mestre-detalhe e scrollbars)*

---

### Sprint 4: Frontend — Dashboard & Inventário de Estoque (`InputStockComponent`)
**Objetivo**: Implementar a tela de Inventário conforme a Imagem 2 (CSS Grid puro para KPIs e barra de ocupação, barra de ações com "+ Novo Lote", tabela densa de lotes e Sidebar de widgets com filtros de 3/7/30 dias e ranking).

- **Arquivos a Criar**:
  - `frontend/src/app/features/inputs/components/stock-kpi-cards/stock-kpi-cards.component.ts` [NEW] *(Cards de Valor Total do Estoque e barra de progresso segmentada com legenda)*
  - `frontend/src/app/features/inputs/components/stock-kpi-cards/stock-kpi-cards.component.html` [NEW]
  - `frontend/src/app/features/inputs/components/stock-kpi-cards/stock-kpi-cards.component.scss` [NEW]
  - `frontend/src/app/features/inputs/components/stock-batch-table/stock-batch-table.component.ts` [NEW] *(Tabela densa: NOME, LOTE, CÓDIGO, VALIDADE, QUANTIDADE, STATUS)*
  - `frontend/src/app/features/inputs/components/stock-batch-table/stock-batch-table.component.html` [NEW]
  - `frontend/src/app/features/inputs/components/stock-batch-table/stock-batch-table.component.scss` [NEW]
  - `frontend/src/app/features/inputs/components/stock-sidebar-widgets/stock-sidebar-widgets.component.ts` [NEW] *(Widgets laterais: Lotes vencendo com filtros [3D, 7D, 30D] e Ranking com filtros [7D, 30D, Ano])*
  - `frontend/src/app/features/inputs/components/stock-sidebar-widgets/stock-sidebar-widgets.component.html` [NEW]
  - `frontend/src/app/features/inputs/components/stock-sidebar-widgets/stock-sidebar-widgets.component.scss` [NEW]

- **Arquivos a Alterar**:
  - `frontend/src/app/features/inputs/pages/input-stock/input-stock.component.ts` [MODIFY] *(Smart Component agregador com `isDesktop`, sinais reativos e chamadas a `inventory-api.service` e `input-batch-api.service`)*
  - `frontend/src/app/features/inputs/pages/input-stock/input-stock.component.html` [MODIFY] *(Layout principal com CSS Grid puro separando coluna de estoque e coluna de widgets, bloqueio mobile)*
  - `frontend/src/app/features/inputs/pages/input-stock/input-stock.component.scss` [MODIFY] *(Definição da grade responsiva CSS Grid e travas de rolagem)*

---

### Sprint 5: Frontend — Formulário Reativo Dinâmico de Produto e Lote Inicial (`InputFormComponent`) & Testes de Integração
**Objetivo**: Implementar o cadastro e edição de insumos com Reactive Forms dinâmico (`addControl('initialBatch')` / `removeControl()`) e testes unitários das regras de negócio do formulário e do serviço de produto.

- **Arquivos a Criar**:
  - `frontend/src/app/features/inputs/forms/input-form.builder.ts` [NEW] *(Construtor do formulário base do produto e fábrica dinâmica do sub-form de lote inicial)*
  - `frontend/src/app/features/inputs/forms/input-form.builder.spec.ts` [NEW] *(Testes unitários do formulário: validação condicional de lote inicial, injeção/remoção de controles e validações de tipos)*
  - `backend/src/test/java/com/manager/coopafi/produtcModule/services/InputProductServiceTest.java` [NEW] *(Testes unitários: criação de produto, suporte transacional ao lote inicial acoplado, validação de NCM/Preço e desativação lógica)*

- **Arquivos a Alterar**:
  - `frontend/src/app/features/inputs/pages/input-form/input-form.component.ts` [MODIFY] *(Lógica reativa do checkbox "Cadastrar Lote Inicial", injeção dinâmica via `addControl()`, validação condicional e submissão)*
  - `frontend/src/app/features/inputs/pages/input-form/input-form.component.html` [MODIFY] *(Formulário com campos mestre, checkbox toggle, bloco condicional do lote inicial e botões de ação)*
  - `frontend/src/app/features/inputs/pages/input-form/input-form.component.scss` [MODIFY] *(Estilização harmônica ao padrão do sistema)*
  - `backend/src/main/java/com/manager/coopafi/purchaseModule/dto/inputProduct/InputProductInsertDto.java` [MODIFY] *(Suporte opcional a lote inicial no payload caso a criação seja atômica)*
  - `backend/src/main/java/com/manager/coopafi/produtcModule/services/InputProductService.java` [MODIFY] *(Suporte transacional à gravação do lote inicial associado ao produto recém-criado)*

---

## 3. Protocolo de Aprovação e Pausa Obrigatória

> [!IMPORTANT]
> **Confirmação de Pausa**: Nenhum código de produção foi escrito. A execução respeitará estritamente a sequência das Sprints Controladas.
