# Plano de Execução Técnico — Módulo de Saídas/Vendas e Dashboard de BI

## 1. Mapeamento e Reuso de Componentes Existentes (Diretriz Obrigatória)

Antes da implementação, foram identificados e mapeados os componentes e utilitários reutilizáveis do projeto:

- **[`SearchableSelectComponent`](file:///c:/Projetos/coopafi/frontend/src/app/shared/components/forms/searchable-select/searchable-select.component.ts)**:
  - *Reuso na Fase 1*: Seleção com busca remota de Clientes (`personId`).
  - *Reuso na Fase 2*: Seleção com busca remota/autocomplete de Lotes (`inputBatchId`), exibindo código do lote, nome do produto e quantidade disponível.
- **[`StepperComponent`](file:///c:/Projetos/coopafi/frontend/src/app/shared/components/layout/stepper/stepper.component.ts)** (`app-stepper-layout`):
  - *Reuso no Formulário*: Navegação guiada pelas fases da venda (Cliente -> Itens -> Revisão).
- **[`PageTitleComponent`](file:///c:/Projetos/coopafi/frontend/src/app/shared/components/layout/page-title/page-title.component.ts)**:
  - *Reuso*: Cabeçalho de página padronizado com botão de retorno.
- **[`InputErrorComponent`](file:///c:/Projetos/coopafi/frontend/src/app/shared/components/forms/input-error/input-error-component.ts)**:
  - *Reuso*: Exibição de erros de validação reativa centralizada.
- **[`ButtonComponent`](file:///c:/Projetos/coopafi/frontend/src/app/shared/components/ui/button/button-component.ts)** (`appButton`):
  - *Reuso*: Botões primários, de superfície e de ações nos formulários e diálogos.
- **[`SubNavComponent`](file:///c:/Projetos/coopafi/frontend/src/app/shared/components/layout/sub-nav/sub-nav.component.ts) & [`SubNavService`](file:///c:/Projetos/coopafi/frontend/src/app/core/services/sub-nav.service.ts)**:
  - *Reuso no Shell*: Alternância dinâmica entre submenus *"Nova Venda"* e *"Dashboard"*.
- **Pipes Utilitários**:
  - [`CpfCnpjPipe`](file:///c:/Projetos/coopafi/frontend/src/app/shared/pipes/cpf-cnpj-pipe.ts) e [`PhonePipe`](file:///c:/Projetos/coopafi/frontend/src/app/shared/pipes/phone-pipe.ts) para visualização e conferência dos dados do cliente.
- **Angular CDK Dialog (`@angular/cdk/dialog`)**:
  - Modal desacoplado para a confirmação final da transação antes do envio HTTP.

---

## 2. Diretrizes Arquiteturais e Padrões Estritos (AGENTS.md)

### Backend (Spring Boot & Java 25):
- **Padrão Strategy para Validações Complexas**:
  - Criação da interface de validação `PurchaseValidationStrategy` e implementações isoladas anotadas com `@Component` (ex: `BatchStockAvailabilityValidator`, `ActiveClientValidator`, `NonExpiredBatchValidator`).
  - Injeção da lista `List<PurchaseValidationStrategy>` no `InputPurchaseServiceImpl` para evitar *Fat Services*.
- **Enums com Comportamento e Polimorfismo**:
  - Utilização de métodos de validação e transição dentro da própria enum (ex: `PaymentStatus.canTransitionTo(...)`, `PaymentStatus.validateCancellation()`), encapsulando regras de negócio no enum.
- **Value Objects**:
  - Encapsulamento estrito de primitivos com validação de invariantes no construtor (`Quantity`, `PriceValue`).
- **Contratos de API e DTOs Imutáveis**:
  - Uso exclusivo de Java `record` para todos os DTOs de requisição e resposta. Entidades de banco nunca devem vazar para os Controllers.
- **Códigos HTTP Rígidos**:
  - Endpoints devem retornar o código HTTP estrito correspondente ao verbo (ex: criação de venda via POST retornando `201 Created` com header `Location`).
- **Tratamento de Exceções**:
  - Consultas com `Optional.orElseThrow()` lançando `DomainException` ou `ResourceNotFoundException`. Proibido retornar `null`.
- **Injeção de Dependências Moderna**:
  - Injeção estrita via construtor, sem uso de `@Autowired` em campos.

### Frontend (Angular 21 Standalone):
- **Divisão Rigorosa Smart vs. Dumb**:
  - **Smart Components** (`SalesShellComponent`, `NewSaleComponent`, `SalesDashboardComponent`): gerenciam injeções de serviços, orquestração de estado reativo, chamadas HTTP e roteamento.
  - **Dumb Components** (`SaleClientStepComponent`, `SaleCartStepComponent`, `SaleConfirmationDialogComponent`, `SalesKpiCardsComponent`, `SalesAbcChartComponent`, `SalesVolumeChartComponent`): focados 100% em UI, recebendo dados via `@Input()` e notificando via `@Output()`.
- **Estrutura de Arquivos Isolada**:
  - Separação mandatória entre arquivos `.html`, `.ts` e `.scss` para cada componente.
- **Tipagem Rigorosa e Enums**:
  - Proibição de uso de `any`. Interfaces TypeScript para todos os payloads de transação e enums para status de pagamento e inventário.
- **Factory Functions para Formulários Reativos**:
  - Construtor do formulário isolado em `forms/sales-form.builder.ts`, exportando função pura `buildSalesForm(fb)`.
- **FormArray e Cálculo Reativo em Tempo Real**:
  - Controle reativo `items` como `FormArray`. Escuta de `valueChanges` combinada com `.reduce()` puro para cálculo do valor financeiro total em tempo real sem efeitos colaterais.
- **Custom Validators**:
  - Validadores customizados para validar itens mínimos, quantidade maior que zero e não ultrapassar o saldo atual do lote.
- **Gestão de Memória**:
  - Uso estrito de `takeUntilDestroyed(this.destroyRef)` ou pipe `async` para garantir descarte automático de inscrições RxJS.
- **Design System e Cores**:
  - Uso estrito das variáveis CSS do tema global (`styles.scss`).

---

## 3. Divisão Técnica em Sprints Controladas

### Sprint 1: Backend — Validações Strategy, Enums Polimórficas, Endpoint e Métricas de BI
**Objetivo**: Implementar o padrão Strategy para validações de compras/vendas, enriquecer a enum de pagamento com regras de transição, expor endpoint REST com código HTTP 201 Created para vendas e disponibilizar as consultas agregadas de BI (Ticket Médio, Risco 90 dias, Volume Mensal e Curva ABC) com testes unitários JUnit.

- **Arquivos a Criar / Alterar**:
  - `backend/.../services/strategy/PurchaseValidationStrategy.java` [NEW] *(Interface Strategy para validações de venda)*
  - `backend/.../services/strategy/impl/BatchStockAvailabilityValidator.java` [NEW] *(Valida se o lote possui saldo disponível)*
  - `backend/.../services/strategy/impl/NonExpiredBatchValidator.java` [NEW] *(Valida se o lote não está expirado)*
  - `backend/.../services/strategy/impl/ActiveClientValidator.java` [NEW] *(Valida se o cliente existe e está ativo)*
  - `backend/.../dto/dashboard/SalesDashboardDto.java` [NEW] *(DTO mestre do painel gerencial de vendas)*
  - `backend/.../dto/dashboard/MonthlySalesDto.java` [NEW] *(Volume financeiro e contagem mensal)*
  - `backend/.../repositories/InputPurchaseRepository.java` [MODIFY] *(Queries agregadas: Ticket Médio e volume por mês)*
  - `backend/.../repositories/InputBatchRepository.java` [MODIFY] *(Consulta de estoque com vencimento <= 90 dias)*
  - `backend/.../enums/PaymentStatus.java` [MODIFY] *(Métodos polimórficos de transição de estado e validação de cancelamento)*
  - `backend/.../services/impl/InputPurchaseServiceImpl.java` [MODIFY] *(Injeção da lista de strategies e validação desacoplada)*
  - `backend/.../services/impl/InventoryDashboardServiceImpl.java` [MODIFY] *(Implementação dos cálculos de BI)*
  - `backend/.../resources/InputPurchaseResource.java` [MODIFY] *(Retorno HTTP 201 Created na criação de compra/venda)*
  - `backend/.../resources/InventoryDashboardResource.java` [MODIFY] *(Endpoint REST `GET /input-batches/dashboard/sales`)*
  - `backend/.../services/InputPurchaseServiceTest.java` [MODIFY] *(Testes unitários cobrindo as strategies e fluxo transacional)*
  - `backend/.../services/InventoryDashboardServiceTest.java` [MODIFY] *(Testes unitários dos cálculos agregados de BI)*

---

### Sprint 2: Frontend — Estrutura Base, Shell de Vendas, Roteamento Lazy e Modelos
**Objetivo**: Configurar o módulo `features/sales`, criar o `SalesShellComponent` com `SubNav` contextual (*"Nova Venda"* e *"Dashboard"*), configurar rotas lazy e tipagens completas sem uso de `any`.

- **Arquivos a Criar / Alterar**:
  - `frontend/.../features/sales/sales.routes.ts` [NEW] *(Roteamento lazy: `nova-venda` e `dashboard`)*
  - `frontend/.../features/sales/pages/sales-shell/sales-shell.component.ts` [NEW] *(Shell com SubNavService e controle de abas)*
  - `frontend/.../features/sales/pages/sales-shell/sales-shell.component.html` [NEW]
  - `frontend/.../features/sales/pages/sales-shell/sales-shell.component.scss` [NEW]
  - `frontend/.../features/sales/models/sales-transaction.model.ts` [NEW] *(Interfaces DTO para envio de vendas e itens)*
  - `frontend/.../features/sales/models/sales-dashboard.model.ts` [NEW] *(Interfaces dos KPIs, Curva ABC e Volume Mensal)*
  - `frontend/.../features/sales/data/sales-api.service.ts` [NEW] *(Acesso HTTP a `/purchase` e `/input-batches/dashboard/sales`)*
  - `frontend/src/app/app.routes.ts` [MODIFY] *(Atualizar rota `/home/vendas` para `loadChildren` de `sales.routes`)*

---

### Sprint 3: Frontend — Formulário de Venda Reativo: Fase 1 (Cliente) e Fase 2 (Carrinho FormArray)
**Objetivo**: Desenvolver o formulário de venda com factory function isolada em `sales-form.builder.ts`, seleção de cliente (apenas `personId` no form), carrinho reativo em `FormArray` com busca restrita a Lotes (`inputBatchId`) e cálculo do total em tempo real com `valueChanges` + `.reduce()`.

- **Arquivos a Criar / Alterar**:
  - `frontend/.../features/sales/forms/sales-form.builder.ts` [NEW] *(Factory function `buildSalesForm()` com `personId` e FormArray `items`)*
  - `frontend/.../features/sales/pages/new-sale/new-sale.component.ts` [NEW] *(Smart Component com Stepper, inscrição reativa via async/takeUntilDestroyed)*
  - `frontend/.../features/sales/pages/new-sale/new-sale.component.html` [NEW]
  - `frontend/.../features/sales/pages/new-sale/new-sale.component.scss` [NEW]
  - `frontend/.../features/sales/components/sale-client-step/sale-client-step.component.ts` [NEW] *(Dumb Component: Busca cliente via SearchableSelect e exibe card visual de conferência com CPF/Telefone via Pipes)*
  - `frontend/.../features/sales/components/sale-client-step/sale-client-step.component.html` [NEW]
  - `frontend/.../features/sales/components/sale-client-step/sale-client-step.component.scss` [NEW]
  - `frontend/.../features/sales/components/sale-cart-step/sale-cart-step.component.ts` [NEW] *(Dumb Component: Tabela de FormArray de itens, busca por lote com autocomplete via SearchableSelect, preço aplicado, quantidade e total em tempo real)*
  - `frontend/.../features/sales/components/sale-cart-step/sale-cart-step.component.html` [NEW]
  - `frontend/.../features/sales/components/sale-cart-step/sale-cart-step.component.scss` [NEW]

---

### Sprint 4: Frontend — Pop-up de Confirmação (CDK Dialog), Envio HTTP e Testes
**Objetivo**: Implementar o Dumb Component Dialog (`@angular/cdk/dialog`), que recebe o `.getRawValue()` e exibe o resumo completo. Após confirmação `true`, o Smart Component dispara o POST para o backend e redireciona com Toast de sucesso.

- **Arquivos a Criar / Alterar**:
  - `frontend/.../features/sales/components/sale-confirmation-dialog/sale-confirmation-dialog.component.ts` [NEW] *(Dumb Dialog: Resumo do cliente, listagem de lotes, quantidades, valores e botões Cancelar / Confirmar Venda)*
  - `frontend/.../features/sales/components/sale-confirmation-dialog/sale-confirmation-dialog.component.html` [NEW]
  - `frontend/.../features/sales/components/sale-confirmation-dialog/sale-confirmation-dialog.component.scss` [NEW]
  - `frontend/.../features/sales/pages/new-sale/new-sale.component.ts` [MODIFY] *(Integração com `Dialog.open()`, envio HTTP POST e navegação)*
  - `frontend/.../features/sales/pages/new-sale/new-sale.component.spec.ts` [NEW] *(Testes unitários: cálculo do total via reduce, validação dos itens do FormArray e fluxo de confirmação)*

---

### Sprint 5: Frontend — Dashboard de BI com `ng2-charts`
**Objetivo**: Implementar a visualização analítica gerencial: Smart Component de Dashboard com cards de KPIs e 2 gráficos interativos com `<canvas baseChart>` do `ng2-charts` (Curva ABC em Rosca/Doughnut e Volume Mensal em Barras).

- **Arquivos a Criar / Alterar**:
  - `frontend/.../features/sales/pages/sales-dashboard/sales-dashboard.component.ts` [NEW] *(Smart Component com chamadas aos serviços de BI e gestão de estado reativo)*
  - `frontend/.../features/sales/pages/sales-dashboard/sales-dashboard.component.html` [NEW]
  - `frontend/.../features/sales/pages/sales-dashboard/sales-dashboard.component.scss` [NEW]
  - `frontend/.../features/sales/components/sales-kpi-cards/sales-kpi-cards.component.ts` [NEW] *(Dumb Component: Cards de Ticket Médio e Taxa de Risco de Vencimento em 90 dias)*
  - `frontend/.../features/sales/components/sales-kpi-cards/sales-kpi-cards.component.html` [NEW]
  - `frontend/.../features/sales/components/sales-kpi-cards/sales-kpi-cards.component.scss` [NEW]
  - `frontend/.../features/sales/components/sales-abc-chart/sales-abc-chart.component.ts` [NEW] *(Dumb Component: Gráfico de Rosca/Doughnut para Curva ABC com baseChart)*
  - `frontend/.../features/sales/components/sales-abc-chart/sales-abc-chart.component.html` [NEW]
  - `frontend/.../features/sales/components/sales-abc-chart/sales-abc-chart.component.scss` [NEW]
  - `frontend/.../features/sales/components/sales-volume-chart/sales-volume-chart.component.ts` [NEW] *(Dumb Component: Gráfico de Barras para volume de vendas nos últimos meses com baseChart)*
  - `frontend/.../features/sales/components/sales-volume-chart/sales-volume-chart.component.html` [NEW]
  - `frontend/.../features/sales/components/sales-volume-chart/sales-volume-chart.component.scss` [NEW]
  - `frontend/.../features/sales/pages/sales-dashboard/sales-dashboard.component.spec.ts` [NEW] *(Testes unitários do dashboard e renderização de dados)*

---

## 4. Protocolo de Aprovação e Pausa Obrigatória

> [!IMPORTANT]
> **Confirmação de Pausa**: Em cumprimento estrito ao **Protocolo de Sprints Controladas**, nenhum código foi iniciado nesta etapa. 
> A execução começará apenas na **Sprint 1** após a sua aprovação explícita.
