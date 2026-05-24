# Sistema AutoManager (AutoBots) - AV4

## 🏫 Informações da Disciplina
- **Instituição:** Fatec - Faculdade de Tecnologia
- **Curso:** Desenvolvimento de Software Multiplataforma (DSM) – 3º Semestre  
- **Disciplina:** Desenvolvimento WEB III  
- **Professor:** Dr. Eng. Gerson Penha  
- **Aluno:** Joao Siqueira  
- **Atividade:** AV3 e AV4  

---

## 🏗️ Contextualização

O projeto **AutoManager** é a evolução do sistema AutoBots. A aplicação deixou de ser um simples cadastro de clientes para se tornar um sistema de gestão empresarial completo para oficinas e centros automotivos (AV3), culminando na implementação de uma arquitetura de segurança robusta de nível internacional (AV4).

Atendendo às exigências do grupo de investidores (padrão Toyota), o sistema agora garante que todas as operações sejam protegidas, autenticadas e restritas com base em perfis de acesso estritos.

---

## 🔒 Atualização Segurança - Autenticação e Autorização (AV4)

Para viabilizar o aporte dos investidores, foi implementado o processo de autenticação e autorização via **JSON Web Token (JWT)** usando o **Spring Security**.

O sistema agora obedece rigorosamente à matriz de responsabilidades e permissões exigida pelo padrão Toyota:

| Perfil | Nível de Autorização (Regras de Negócio) |
| :--- | :--- |
| **Administrador** | Acesso total. Autorização para fazer todas as operações de CRUD na aplicação, incluindo adicionar ou remover usuários administradores. |
| **Gerente** | Operações de CRUD sobre usuários dos perfis *gerente*, *vendedor* e *cliente*. CRUD completo sobre *serviços*, *vendas* e *mercadorias*. |
| **Vendedor** | Operações de CRUD exclusivas sobre usuários do perfil *cliente*. Acesso de leitura a informações sobre *serviços* e *mercadorias*. Permissão para criar vendas próprias e ler suas informações (Ownership). |
| **Cliente** | Acesso restrito (Ownership). Lê apenas informações sobre o seu próprio cadastro e de vendas das quais foi o consumidor. |

---

## 🚀 Evolução de Domínio e Funcionalidades (AV3)

Além da segurança, o sistema consolidou os seguintes requisitos de domínio:

- ✅ **Gestão de Empresas:** CRUD completo para unidades comerciais com suporte a endereços e telefones.
- ✅ **Associação Dinâmica:** Funcionalidade para associar/desassociar usuários a empresas de forma segura (`orphanRemoval = false`).
- ✅ **Gestão de Inventário e Serviços:** Cadastro de Mercadorias e Serviços de manutenção.
- ✅ **Motor de Vendas:** Registro de vendas vinculando Cliente, Vendedor, Empresa, Veículo, Mercadorias e Serviços.
- ✅ **Polimorfismo de Credenciais:** Sistema de acesso permitindo Login/Senha ou Código de Barras (Crachá).
- ✅ **HATEOAS Consolidado:** Links navegáveis em todos os recursos, incluindo sub-recursos.

---

## 🧰 Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3.x**
- **Spring Security & JWT** (Autenticação Stateless)
- **Spring Data JPA**
- **Spring HATEOAS** (Nível 3 de Richardson)
- **Docker** (Containerização e Multi-stage build)
- **H2 Database** (Banco de dados em memória para testes)
- **Lombok** (Produtividade e código limpo)

---

## 🏛️ Arquitetura do Projeto e RMM

O sistema cumpre integralmente os **4 níveis do Modelo de Maturidade de Richardson (RMM)**:
1. **Recursos bem definidos:** `/api/veiculos`, `/api/vendas`, `/api/usuarios`, etc.
2. **Uso semântico de verbos e Status Codes HTTP:** GET, POST, PUT, DELETE (200, 201, 204, 403, etc).
3. **Hipermídia (HATEOAS):** Links de navegação em todas as respostas.

A estrutura de pacotes separa claramente as responsabilidades entre: `controle` (Endpoints e Segurança), `servico` (Regras de negócio e Validações de Ownership), `entidade` (Modelos JPA), `dto` (Transferência de dados) e `hateaos` (Construção de Links).

---

## ▶️ Como Executar com Docker

A forma mais recomendada e segura de rodar a aplicação é utilizando o Docker, o que elimina qualquer problema de dependências ou versão de Java na máquina local.

### ⚠️ Pré-requisito
Você precisa ter o Docker instalado e rodando no seu computador. Caso não tenha, faça o download abaixo:
- [Baixar e instalar o Docker Desktop](https://www.docker.com/products/docker-desktop/)

---

**Passo 1: Construir a imagem da API**  
Na raiz do projeto (onde está o `Dockerfile`), execute:
```bash
cd automanager
docker build -t automanager-api .
```

**Passo 2: Subir o container** 
Após a compilação (build) finalizar, inicie o servidor rodando:
```bash
docker run --rm -p 8080:8080 --name automanager-app automanager-api
```

A API estará disponível e pronta para receber requisições em `http://localhost:8080`.

## 🔑 Como Testar a Autenticação (Logins)
Para consumir as rotas da API, você precisa gerar um token JWT. O banco de dados é populado automaticamente ao iniciar a aplicação com os seguintes usuários de teste.

Envie uma requisição POST para http://localhost:8080/auth/login (ou endpoint correspondente) com o corpo em JSON:
Envie uma requisição `POST` para `http://localhost:8080/auth/login`

1. Login como ADMIN 
```bash
JSON
{ 
  "nomeUsuario": "admin", 
  "senha": "123456" 
}
```

2. Login como GERENTE 
```bash
JSON
{ 
  "nomeUsuario": "gerente", 
  "senha": "123456" 
}
```

3. Login como VENDEDOR 
```bash
JSON
{ 
  "nomeUsuario": "vendedor", 
  "senha": "123456" 
}
```

4. Login como CLIENTE 
```bash
JSON
{ 
  "nomeUsuario": "cliente", 
  "senha": "123456" 
}
```
