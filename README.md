# AV3 - Sistema AutoManager (AutoBots)

## 🏫 Informações da Disciplina
- **Curso:** Desenvolvimento de Software Multiplataforma (Fatec) – 3º Semestre  
- **Disciplina:** Desenvolvimento WEB 3  
- **Professor:** Dr. Eng. Gerson Penha  
- **Aluno:** Joao Siqueira  
- **Atividade:** AV3  

---

## 🏗️ Contextualização

O projeto **AutoManager** é a evolução do sistema AutoBots (AV2). Nesta etapa, a aplicação deixou de ser um simples cadastro de clientes para se tornar um sistema de gestão empresarial completo para oficinas e centros automotivos. 

O foco da **AV3** foi a expansão do domínio para suportar a gestão de unidades comerciais (empresas), múltiplos perfis de acesso, controlo de frota, inventário de mercadorias e a realização de vendas complexas, mantendo o rigor técnico dos níveis de maturidade REST.

---

## 🚀 Evolução e Funcionalidades (AV3)

Nesta versão, foram implementados e corrigidos os seguintes requisitos:

- ✅ **Gestão de Empresas:** CRUD completo para unidades comerciais com suporte a endereços e telefones.
- ✅ **Perfis de Utilizador:** Implementação de `Enums` para distinguir entre `CLIENTE`, `FUNCIONARIO` e `FORNECEDOR`.
- ✅ **Associação Dinâmica:** Funcionalidade para associar/desassociar utilizadores a empresas, garantindo que a remoção do vínculo não exclua o utilizador do banco de dados (`orphanRemoval = false`).
- ✅ **Gestão de Inventário e Serviços:** Cadastro de Mercadorias (com controlo de disponibilidade) e Serviços de manutenção.
- ✅ **Motor de Vendas:** Registo de vendas vinculando Cliente, Funcionário, Veículo, múltiplas Mercadorias e diversos Serviços.
- ✅ **Polimorfismo de Credenciais:** Sistema de acesso flexível permitindo Login/Senha ou Código de Barras (Crachá).
- ✅ **HATEOAS Consolidado:** Links navegáveis em todos os recursos, incluindo sub-recursos de utilizadores e empresas.

---

## 🧰 Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **Spring HATEOAS** (Nível 3 de Richardson)
- **Lombok** (Produtividade e código limpo)
- **H2 Database** (Banco de dados em memória para testes rápidos)

📌 **Carga de Dados:** A aplicação utiliza a classe `AutomanagerApplication` para popular o banco de dados automaticamente ao iniciar, facilitando a validação imediata das rotas.

---

## 🏛️ Arquitetura do Projeto

A estrutura de pacotes foi desenhada para garantir a separação total de responsabilidades:

- **`com.autobots.automanager.controle`**: Controllers REST que gerem o fluxo de entrada/saída e HATEOAS.
- **`com.autobots.automanager.servico`**: Camada de negócio onde residem as validações de unicidade e lógica de persistência.
- **`com.autobots.automanager.entidade`**: Modelos JPA com mapeamentos complexos (`Inheritance`, `@ManyToMany`, `@OneToMany`).
- **`com.autobots.automanager.dto`**: Objetos de transferência para evitar a exposição direta das entidades e proteger dados sensíveis.
- **`com.autobots.automanager.mapeador`**: Componentes responsáveis pela conversão entre Entidades e DTOs.
- **`com.autobots.automanager.hateaos`**: Assemblers que injetam a lógica de hipermédia (links) nas respostas.
- **`com.autobots.automanager.excecao`**: Handler global que captura erros e devolve Status Codes HTTP apropriados.

---

## 🔗 Richardson Maturity Model (RMM)

O sistema cumpre integralmente os 4 níveis do modelo:
- **Nível 0:** Protocolo HTTP.
- **Nível 1:** Recursos bem definidos (`/api/veiculos`, `/api/vendas`).
- **Nível 2:** Uso semântico de verbos (GET, POST, PUT, DELETE) e Status Codes (201 Created, 204 No Content, 409 Conflict).
- **Nível 3:** Hipermédia (HATEOAS) fornecendo links para ações subsequentes no JSON de resposta.

---

## ▶️ Como Executar e Testar

1. Clone o repositório.
2. Certifique-se de que tem o **Java 17** instalado.
3. Execute via Maven: `./mvnw spring-boot:run` ou pela sua IDE.
4. O servidor subirá em `http://localhost:8080`.

### Testes das APIs
Na raiz do projeto, existe um ficheiro com exemplos de requisições prontos para uso:
- Utilize o ficheiro de requisições fornecido (pode ser aberto com a extensão **REST Client** do VS Code ou importado no **Insomnia/Postman**).
- **Importante:** Siga a ordem lógica de criação (Empresa -> Utilizador -> Veículo -> Venda) para evitar erros de integridade.

---
