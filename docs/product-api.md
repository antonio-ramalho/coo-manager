# Especificação de API: Entidade `Farmer` (Agricultor)

**Contexto:** Projeto Coopafi (Sistema Logístico Agrícola).
**Arquitetura:** Camadas (Controller, Service, Repository), DTOs como `record`, tratamento de exceções global (Fail-Fast) 
e uso rigoroso de Value Objects (CPF, Email, Phone, Address, Cep, BirthDate) na camada de Domínio.

---

## 1. DTOs (Data Transfer Objects) Compartilhados

### `FarmerDTO` (Saída Completa)
Usado para retornar os dados detalhados (achatados/flattened) do agricultor e da pessoa física associada.
- `id` (Long)
- `name` (String)
- `cpfNumber` (String)
- `date` (String, formato YYYY-MM-DD)
- `phoneNumber` (String)
- `addressEmail` (String)
- `cepNumber` (String)
- `street` (String)
- `neighborhood` (String)
- `city` (String)
- `addressNumber` (String)
- `cafs` (List de objetos contendo `id`, `numeroCaf`, `validade`)
- `certificados` (List de objetos contendo `id`, `nomeCertificado`)

### `FarmerMinDTO` (Saída Resumida)
Usado para listagens rápidas e preenchimento de combobox/selects no Front-end.
- `id` (Long)
- `name` (String)

---

## 2. Endpoints

### 2.1. Cadastrar Novo Agricultor (Orquestração)
- **Rota:** `POST /farmers`
- **Objetivo:** Cadastra a Pessoa Física (`NaturalPerson`), o Agricultor (`Farmer`) e orquestra a criação opcional de novas CAFs e o vínculo com Certificados já existentes. Tudo deve rodar em uma única transação (`@Transactional`).
- **Payload de Entrada (`FarmerInsertDTO` - Todos os base são obrigatórios):**
    - `name` (String)
    - `cpfNumber` (String)
    - `date` (String)
    - `phoneNumber` (String)
    - `addressEmail` (String)
    - `cepNumber`, `street`, `neighborhood`, `city`, `addressNumber` (Strings)
    - `novasCafs` (List<CafInsertDTO> - Opcional. DTO contendo `numero` e `validade`)
    - `certificadosExistentesIds` (List<Long> - Opcional. IDs numéricos p/ vincular)
- **Payload de Saída:** `FarmerDTO`
- **Status de Sucesso:** `201 Created`
- **Regras de Negócio:** - Não pode existir outro Farmer com o mesmo CPF no banco.
    - Se a lista `novasCafs` for enviada, instanciar as CAFs, vincular ao Farmer e salvar.
    - Se a lista `certificadosExistentesIds` for enviada, buscar os certificados no banco e adicionar à lista do Farmer.

### 2.2. Listar Todos os Agricultores
- **Rota:** `GET /farmers`
- **Objetivo:** Retorna uma lista leve com todos os agricultores ativos.
- **Payload de Saída:** Lista de `FarmerMinDTO`
- **Status de Sucesso:** `200 OK`

### 2.3. Buscar Agricultor por ID
- **Rota:** `GET /farmers/{id}`
- **Objetivo:** Retorna a ficha completa de um agricultor específico.
- **Payload de Saída:** `FarmerDTO`
- **Status de Sucesso:** `200 OK`
- **Exceção Esperada:** Se o ID não existir, lançar exceção de `ResourceNotFound` (404).

### 2.4. Atualizar Dados do Agricultor (Update Parcial)
- **Rota:** `PATCH /farmers/{id}`
- **Objetivo:** Atualiza apenas os campos enviados pelo usuário. Campos imutáveis de negócio não podem ser alterados.
- **Payload de Entrada (`FarmerUpdateDTO` - Todos os campos opcionais/nullable):**
    - `phoneNumber` (String)
    - `addressEmail` (String)
    - `cepNumber`, `street`, `neighborhood`, `city`, `addressNumber` (Strings)
    - *(Nota: CPF e Data de Nascimento não entram aqui, pois são imutáveis)*
- **Payload de Saída:** `FarmerDTO` (com os dados atualizados)
- **Status de Sucesso:** `200 OK`
- **Regras de Negócio (Service):**
    - Buscar o agricultor no banco pelo ID.
    - Fazer verificação de nulidade (ex: `if (dto.addressEmail() != null)`) antes de instanciar os Value Objects para atualizar a entidade.

### 2.5. Excluir Agricultor
- **Rota:** `DELETE /farmers/{id}`
- **Objetivo:** Remove o agricultor do sistema.
- **Payload de Entrada:** Nenhum (Apenas o ID na URL).
- **Payload de Saída:** Nenhum.
- **Status de Sucesso:** `204 No Content`
- **Regras de Negócio:** - Antes de excluir, verificar regras de integridade (ex: Se o agricultor tiver contratos ativos, não permitir exclusão e lançar exceção de violação de integridade - 400 Bad Request).