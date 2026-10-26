# Contexto do Projeto (Coopafi)
O sistema é um software SaaS multi-tenant voltado para o gerenciamento de cooperativas de agricultura familiar. Ele atende a múltiplas cooperativas simultaneamente.
Principais domínios e contextos: 
- Venda de insumos.
- Gestão de editais e cadastros governamentais.
- Gestão de agricultores, certificados orgânicos e CAFs (Cadastro Nacional da Agricultura Familiar).
- Gerenciamento logístico.
- Cadastro de clientes e cooperados.

# Stack Tecnológica e Ambientes
- Backend: Java 25 com Spring Boot.
- Frontend: Angular 21 (Use APENAS Standalone Components, sem NgModules) com Reactive Forms e Angular CDK.
- Banco de Dados: MySQL (Ambiente Local/Dev) e PostgreSQL via Supabase (Ambiente Nuvem/Prod).
- Regra de Ambiente: Mantenha sempre `spring.profiles.active=dev` localmente. Antes de preparar o código para commit/push, valide se a infraestrutura está adaptada para `prod` se solicitado.

# Arquitetura e Padrões (Backend)
- Domain-Driven Design (DDD): Organize os pacotes por contexto/feature (ex: `contract`, `insumos`), não por camada técnica.
- Camadas: Siga o fluxo Controller -> Service -> Repository. 
- Contratos de API: SEMPRE use DTOs para requisições e respostas. Entidades de banco de dados nunca devem vazar para o Controller.
- Validações Básicas: Use Value Objects para encapsular lógicas e validações de tipos primitivos no momento da instanciação.
- Regras de Negócio (Evite Fat Services): Use o padrão Strategy para validações complexas. Crie interfaces de validação (ex: `ContractValidator`) e injete listas de implementações (`@Component`) na Service principal.
- Buscas no Banco: Use a API de Optional com `.orElseThrow()` e lance exceções de negócio customizadas. Evite retornar null.
- Código Limpo: Aplique os princípios do SOLID e Object Calisthenics (ex: evite else, mantenha baixo acoplamento).
- Regras e polimorfismo na enum: Sempre que possível e adequado, use a enum juntamente com o poliofismo para determinadas regras serem validades dentro da própria enum. 

# Arquitetura e Padrões (Frontend)
- Componentização: Separe estritamente em Smart Components (lidam com serviços e estado) e Dumb Components (focados apenas em UI e @Input/@Output).
- Estrutura de Arquivos: Separação clara entre `.html`, `.ts` e `.scss` para cada componente.
- Ecossistema: Utilize Interfaces (para tipagem rigorosa), Enums, Custom Pipes, Validators customizados, Interceptors (para chamadas HTTP) e Router Guards.
- Padronização: Mantenha um sistema de nomenclatura claro, descritivo e coeso (ex: `feature-name.component.ts`).

# Idioma e Documentação
- Todo o código-fonte (variáveis, classes, métodos) DEVE ser escrito em Inglês.
- Toda a documentação (Readme), comentários de código e Javadocs DEVEM ser escritos em Português (PT-BR).
- Documentação de Domínio: Antes de iniciar uma tarefa em um contexto específico, verifique se existe documentação na pasta `/documentacao` na raiz do projeto. Caso não exista documentação para o contexto solicitado, proceda normalmente.

# Regras de Execução e Permissões do Agente
- Planejamento Prévio: Antes de escrever o código, descreva brevemente o plano de ação e quais padrões serão aplicados.
- Testes: Você PODE e DEVE rodar testes unitários e de integração automaticamente ao criar novas regras de negócio.
- Dependências: Você NÃO PODE instalar novos pacotes via `npm install` ou modificar o `pom.xml` sem pedir autorização explícita ao usuário.
- Refatoração: Sempre revise as quebras de dependência antes de modificar ou apagar a assinatura de métodos ou construtores existentes.
- Você deve consultar as cores do arquivo `styles.scss` para manter a consistência visual do projeto.

# PROTOCOLO DE SPRINTS E EXECUÇÃO (OBRIGATÓRIO)
- Você está estritamente proibido de executar refatorações em massa ou implementar planos completos de forma autônoma e contínua. Todo o trabalho deve seguir um fluxo de "Sprints Controladas".

- Fatiamento: Ao receber um plano de ação, você deve dividi-lo em Fases ou Sprints (ex: Fase 1, Fase 2).

- Execução Isolada: Execute APENAS a fase atual solicitada pelo usuário.

- Pausa Absoluta (Hard Stop): Ao finalizar os arquivos da fase atual, você deve PARAR IMEDIATAMENTE. Não inicie a próxima fase, não tente antecipar código e não altere arquivos de contextos futuros.

- Solicitação de Aprovação: Encerre sua resposta informando o que foi feito na sprint atual e peça explicitamente: "Por favor, teste a aplicação. Digite 'Aprovado' para seguirmos para a Fase X, ou indique os ajustes visuais/lógicos necessários nesta etapa".

- Correção de Rota: Se o usuário reportar um erro (ex: quebra de layout, erro de roteamento, falha de tipagem), você deve focar 100% em corrigir o problema do escopo atual antes de avançar no plano original.