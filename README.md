# Sistema de Gerenciamento Logístico

Este projeto nasceu de uma necessidade real identificada no setor administrativo e operacional de uma cooperativa de Agricultura Familiar. O objetivo é centralizar e automatizar a gestão de dados que, anteriormente, eram registrados de forma descentralizada em diversas planilhas, garantindo integridade e rastreabilidade às operações.

## O Problema

Atualmente, a gestão de entrada e saída de produtos agrícolas, bem como o controle de insumos, sofre com a falta de padronização e o risco de perda de informações devido ao uso excessivo de planilhas manuais. Este sistema visa consolidar essas operações em uma API robusta e escalável.

## Funcionalidades

- Gestão de Colheita: Registro de entrada e saída de produtos agrícolas por agricultor.
- Controle de Saldos: Monitoramento de saldo por projeto, por agricultor e por tipo de produto.
- Gestão de Insumos: Controle de estoque e venda de insumos diretamente para os cooperados.
- Relatórios: Geração de dados consolidados para prestação de contas e tomada de decisão.

## Principais Tecnologias e Conceitos

- Backend: Java 17+ com Spring Boot.
- Persistência: Spring Data JPA e Hibernate para mapeamento objeto-relacional (ORM) e gestão de relacionamentos complexos.
- Arquitetura: Camadas bem definidas (Resources, Services, Repositories, Domain) para garantir a separação de responsabilidades.
- Segurança e Performance: Implementação de Nested DTOs (DTOs Aninhados) para otimizar o tráfego de dados e proteger as entidades de domínio.
- Programação Orientada a Objetos.

## Engenharia de Software

- DDD (Domain-Driven Design): Construção de um Domínio Rico utilizando Value Objects e Entities para garantir que as regras de negócio estejam centralizadas e protegidas.

- Fail-Fast: Validação rigorosa de estados logo na entrada dos dados, evitando o processamento de informações inválidas.

- Qualidade de Código: Aplicação de princípios SOLID e técnicas de Object Calisthenics para manter o código limpo, coeso e de fácil manutenção.

- Testes Unitários: Implementação de testes automatizados utilizando JUnit 5 e Mockito para validar a precisão dos cálculos e garantir a integridade das regras de negócio em cada componente do sistema.

