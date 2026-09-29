# Jogaí
## Aplicação para Gerenciamento e Empréstimo de Jogos de Tabuleiro

# 1. Contextualização

Uma comunidade de jogadores de jogos de tabuleiro possui uma coleção compartilhada de jogos que podem ser emprestados por seus membros.

À medida que a coleção cresce, torna-se necessário controlar quais jogos fazem parte da coleção, quantos exemplares físicos existem de cada jogo, quais usuários estão utilizando cada exemplar, quais empréstimos estão atrasados e quais jogos possuem usuários aguardando sua disponibilidade.

O objetivo deste projeto é desenvolver um **Sistema de Gerenciamento e Empréstimo de Jogos de Tabuleiro**, permitindo administrar todo o ciclo de vida dos jogos dentro da comunidade:

```text
Cadastro do jogo
       ↓
Cadastro de exemplares
       ↓
Disponibilização
       ↓
Empréstimo
       ↓
Utilização
       ↓
Devolução
       ↓
Nova disponibilização
```

O sistema também deverá permitir que usuários reservem jogos indisponíveis e acompanhem sua posição na fila de reservas.

O projeto deverá ser desenvolvido com foco em **Arquitetura de Software**, e não apenas na implementação das funcionalidades.

---

# 2. Tecnologias

A aplicação será composta por módulos desenvolvidos utilizando diferentes tecnologias.

## 2.1 Módulos de domínio, aplicação e dados

Os módulos responsáveis pelo domínio, casos de uso, regras de negócio e persistência deverão ser desenvolvidos em:

**Java**

---

## 2.2 Interface

A interface da aplicação deverá ser desenvolvida em:

**Kotlin + Compose Multiplatform**

A interface será uma aplicação multiplataforma, podendo contemplar, conforme o escopo definido pela equipe:

- Desktop;
- Mobile;
- Web.

O desenvolvimento poderá inicialmente priorizar Desktop, mas a arquitetura deverá evitar acoplamento desnecessário entre a lógica da aplicação e a plataforma de apresentação.

---

## 2.3 Persistência

Os dados da aplicação deverão ser persistidos utilizando serviços do:

**Firebase**

O Firebase deverá ser tratado como uma tecnologia de infraestrutura.

As regras de negócio e entidades do domínio não deverão depender diretamente de APIs específicas do Firebase.

---

# 3. Objetivos Arquiteturais

O projeto deverá demonstrar a aplicação dos seguintes conceitos:

- modularização;
- separação de responsabilidades;
- encapsulamento;
- coesão;
- baixo acoplamento;
- inversão de dependência;
- abstração;
- Repository Pattern;
- Service/Application Layer;
- separação entre domínio e infraestrutura;
- separação entre domínio e interface;
- testabilidade;
- interoperabilidade entre Java e Kotlin.

---

# 4. Arquitetura Geral

A aplicação deverá possuir, no mínimo, os seguintes módulos:

```text
boardgame-lending
│
├── domain
├── application
├── data
└── presentation
```

A arquitetura conceitual será:

```text
┌─────────────────────────────────────────┐
│              PRESENTATION               │
│                                         │
│ Kotlin + Compose Multiplatform          │
│ Screens / ViewModels / UI State         │
└────────────────────┬────────────────────┘
                     │
                     ↓
┌─────────────────────────────────────────┐
│              APPLICATION                │
│                                         │
│ Use Cases / Services                    │
│ Orquestração da aplicação               │
└────────────────────┬────────────────────┘
                     │
                     ↓
┌─────────────────────────────────────────┐
│                 DOMAIN                  │
│                                         │
│ Entities / Value Objects / Rules        │
│ Repository Contracts                    │
└────────────────────┬────────────────────┘
                     ↑
                     │
┌────────────────────┴────────────────────┐
│                  DATA                   │
│                                         │
│ Firebase / Repositories / Mappers       │
└─────────────────────────────────────────┘
```

A direção conceitual das dependências deverá ser:

```text
Presentation → Application → Domain

Data → Domain
```

O módulo `domain` não deverá depender de:

- Compose;
- Firebase;
- detalhes de persistência;
- componentes de interface.

---

# 5. Conceitos Fundamentais do Sistema

Um ponto fundamental deste projeto é diferenciar **Jogo** de **Exemplar**.

## Jogo

Representa o produto/conceito do jogo.

Exemplo:

```text
Catan
```

## Exemplar

Representa uma cópia física específica daquele jogo.

Exemplo:

```text
Catan #001
Catan #002
Catan #003
```

Portanto:

```text
Jogo
  │
  ├── Exemplar #001
  ├── Exemplar #002
  └── Exemplar #003
```

Dois exemplares do mesmo jogo possuem o mesmo conjunto de características gerais, mas são unidades físicas diferentes e podem possuir estados diferentes.

---

# 6. Entidades do Sistema

O sistema deverá possuir, no mínimo, as seguintes entidades:

```text
Usuario
Jogo
Categoria
Exemplar
Emprestimo
ItemEmprestimo
Reserva
```

Também poderão ser utilizados **Value Objects**, enums e outras estruturas auxiliares quando forem apropriados.

---

# 7. Entidade Usuario

Representa uma pessoa cadastrada no sistema.

Um usuário pode realizar empréstimos e reservas.

## Atributos

| Atributo | Tipo conceitual | Obrigatório | Descrição |
|---|---|---:|---|
| id | Identificador | Sim | Identificador único |
| nome | Texto | Sim | Nome completo |
| email | Texto | Sim | E-mail do usuário |
| telefone | Texto | Não | Telefone |
| dataCadastro | Data/Hora | Sim | Momento do cadastro |
| status | Enum | Sim | Situação do usuário |

## Status

```text
ATIVO
BLOQUEADO
INATIVO
```

### Regras

Um usuário:

- deve possuir nome;
- deve possuir e-mail válido;
- deve possuir e-mail único;
- somente pode realizar empréstimos quando estiver `ATIVO`;
- não pode realizar novos empréstimos quando estiver `BLOQUEADO`;
- não pode realizar operações quando estiver `INATIVO`, exceto consultas permitidas;
- pode possuir vários empréstimos ao longo do tempo;
- pode possuir várias reservas.

### Relacionamentos

```text
Usuario 1 ───── N Emprestimo
Usuario 1 ───── N Reserva
```

---

# 8. Entidade Categoria

Representa uma classificação de jogos.

Exemplos:

```text
Estratégia
Família
Cooperativo
Party Game
Cartas
RPG
Dedução
Infantil
Guerra
Eurogame
```

## Atributos

| Atributo | Tipo | Obrigatório |
|---|---|---:|
| id | Identificador | Sim |
| nome | Texto | Sim |
| descricao | Texto | Não |

O nome deverá ser único.

---

# 9. Entidade Jogo

Representa um jogo de tabuleiro existente na coleção.

## Atributos

| Atributo | Tipo | Obrigatório | Descrição |
|---|---|---:|---|
| id | Identificador | Sim | Identificador único |
| nome | Texto | Sim | Nome do jogo |
| descricao | Texto | Sim | Descrição |
| numeroMinimoJogadores | Inteiro | Sim | Menor quantidade de jogadores |
| numeroMaximoJogadores | Inteiro | Sim | Maior quantidade de jogadores |
| idadeMinima | Inteiro | Sim | Idade mínima recomendada |
| duracaoMinima | Inteiro | Sim | Duração mínima aproximada em minutos |
| duracaoMaxima | Inteiro | Sim | Duração máxima aproximada em minutos |
| anoLancamento | Inteiro | Não | Ano de lançamento |
| editora | Texto | Não | Editora |
| dataCadastro | Data/Hora | Sim | Data de inclusão no sistema |
| ativo | Boolean | Sim | Indica se o jogo está ativo |

## Relacionamentos

Um jogo:

- possui uma ou mais categorias;
- possui zero ou vários exemplares;
- pode possuir várias reservas associadas;
- pode aparecer em vários empréstimos através de seus exemplares.

```text
Jogo N ───── N Categoria
Jogo 1 ───── N Exemplar
Jogo 1 ───── N Reserva
```

### Importante

Um `Jogo` não é emprestado diretamente.

Quem é efetivamente emprestado é um `Exemplar`.

---

# 10. Entidade Exemplar

Representa uma unidade física específica de um jogo.

## Atributos

| Atributo | Tipo | Obrigatório | Descrição |
|---|---|---:|---|
| id | Identificador | Sim | Identificador único |
| codigo | Texto | Sim | Código único do exemplar |
| jogoId | Identificador | Sim | Jogo ao qual pertence |
| dataCadastro | Data/Hora | Sim | Data de cadastro |
| estadoConservacao | Enum | Sim | Estado físico |
| status | Enum | Sim | Situação operacional |
| observacoes | Texto | Não | Observações |

## Estado de conservação

```text
NOVO
EXCELENTE
BOM
REGULAR
DANIFICADO
```

## Status

```text
DISPONIVEL
EMPRESTADO
RESERVADO
MANUTENCAO
INDISPONIVEL
```

### Diferença entre estado e status

O estado representa a **condição física**:

```text
BOM
```

O status representa a **situação operacional**:

```text
EMPRESTADO
```

Assim, um exemplar pode estar:

```text
Estado: BOM
Status: EMPRESTADO
```

### Relacionamento

```text
Jogo 1 ───── N Exemplar
```

Um exemplar pertence a exatamente um jogo.

---

# 11. Entidade Emprestimo

Representa uma operação de empréstimo realizada por um usuário.

Um empréstimo pode conter um ou mais exemplares.

## Atributos

| Atributo | Tipo | Obrigatório | Descrição |
|---|---|---:|---|
| id | Identificador | Sim | Identificador único |
| usuarioId | Identificador | Sim | Usuário responsável |
| dataEmprestimo | Data/Hora | Sim | Momento do empréstimo |
| dataPrevistaDevolucao | Data/Hora | Sim | Prazo para devolução |
| dataDevolucao | Data/Hora | Não | Momento da conclusão |
| status | Enum | Sim | Situação |
| observacoes | Texto | Não | Observações |

## Status

```text
ATIVO
ATRASADO
FINALIZADO
CANCELADO
```

### Relacionamentos

```text
Usuario 1 ───── N Emprestimo
Emprestimo 1 ───── N ItemEmprestimo
```

---

# 12. Entidade ItemEmprestimo

Representa a associação entre um empréstimo e um exemplar específico.

Essa entidade é necessária porque um empréstimo pode conter vários exemplares.

## Atributos

| Atributo | Tipo | Obrigatório |
|---|---|---:|
| id | Identificador | Sim |
| emprestimoId | Identificador | Sim |
| exemplarId | Identificador | Sim |

Relacionamentos:

```text
Emprestimo 1 ───── N ItemEmprestimo
Exemplar 1 ───── N ItemEmprestimo
```

Um exemplar poderá aparecer em vários itens ao longo de sua história, mas **não poderá aparecer em dois empréstimos ativos simultaneamente**.

Exemplo:

```text
Empréstimo #100
Usuário: João

Itens:

Item #1 → Catan #001
Item #2 → Azul #002
Item #3 → Dixit #001
```

---

# 13. Entidade Reserva

Representa o interesse de um usuário em utilizar determinado jogo quando não houver exemplares disponíveis.

A reserva deverá ser feita para o **Jogo**, e não para um exemplar específico.

Isso é importante porque qualquer exemplar disponível daquele jogo poderá atender a reserva.

## Atributos

| Atributo | Tipo | Obrigatório | Descrição |
|---|---|---:|---|
| id | Identificador | Sim | Identificador único |
| usuarioId | Identificador | Sim | Usuário que realizou a reserva |
| jogoId | Identificador | Sim | Jogo reservado |
| dataReserva | Data/Hora | Sim | Momento da reserva |
| status | Enum | Sim | Situação |
| dataAtendimento | Data/Hora | Não | Momento em que foi atendida |
| dataCancelamento | Data/Hora | Não | Momento do cancelamento |
| observacoes | Texto | Não | Observações |

## Status

```text
ATIVA
ATENDIDA
CANCELADA
EXPIRADA
```

### Relacionamentos

```text
Usuario 1 ───── N Reserva
Jogo 1 ───── N Reserva
```

---

# 14. Modelo de Relacionamentos Completo

O modelo conceitual do sistema deverá ser semelhante a:

```text
                         ┌──────────────┐
                         │   Categoria  │
                         └──────┬───────┘
                                │
                              N │
                                │ N
                         ┌──────┴───────┐
                         │     Jogo      │
                         └──────┬───────┘
                              1 │
                                │
                              N │
                         ┌──────┴───────┐
                         │   Exemplar   │
                         └──────┬───────┘
                                │
                              1 │
                                │ N
                    ┌───────────┴───────────┐
                    │    ItemEmprestimo     │
                    └───────────┬───────────┘
                              N │
                                │ 1
                         ┌──────┴───────┐
                         │  Emprestimo  │
                         └──────┬───────┘
                              N │
                                │ 1
                         ┌──────┴───────┐
                         │    Usuario   │
                         └──────┬───────┘
                              1 │
                                │ N
                         ┌──────┴───────┐
                         │    Reserva   │
                         └──────┬───────┘
                              N │
                                │ 1
                         ┌──────┴───────┐
                         │     Jogo     │
                         └──────────────┘
```

De forma simplificada:

```text
Usuario
 ├── Emprestimos
 │      └── Itens
 │             └── Exemplar
 │                    └── Jogo
 │
 └── Reservas
        └── Jogo
               └── Categorias
```

---

# 15. Regras de Negócio

## RN01 — Cadastro de usuário

O e-mail de um usuário deverá ser único.

---

## RN02 — Status do usuário

Somente usuários `ATIVOS` poderão realizar empréstimos.

Usuários `BLOQUEADOS` ou `INATIVOS` não poderão realizar novos empréstimos.

---

## RN03 — Limite de empréstimos

Um usuário poderá possuir no máximo **3 exemplares emprestados simultaneamente**.

O limite deverá ser considerado sobre exemplares, e não sobre empréstimos.

Exemplo:

```text
Empréstimo #1 → 2 jogos
Empréstimo #2 → 1 jogo

Total = 3 exemplares
```

O usuário atingiu seu limite.

---

## RN04 — Empréstimos atrasados

Um empréstimo ativo será considerado atrasado quando a data atual ultrapassar sua data prevista de devolução.

Um usuário com pelo menos um empréstimo atrasado não poderá realizar novos empréstimos.

---

## RN05 — Prazo

O prazo padrão de empréstimo será de **7 dias**.

A regra deverá ser implementada de forma que o prazo possa ser alterado futuramente sem modificar diversas partes do sistema.

---

## RN06 — Disponibilidade

Somente exemplares com status `DISPONIVEL` poderão ser emprestados.

---

## RN07 — Exclusividade

Um exemplar não poderá estar associado a mais de um empréstimo ativo.

---

## RN08 — Empréstimo

Ao realizar um empréstimo:

```text
Exemplar DISPONIVEL
        ↓
Exemplar EMPRESTADO
```

---

## RN09 — Devolução

Ao devolver um exemplar:

```text
Exemplar EMPRESTADO
        ↓
Exemplar DISPONIVEL
```

Entretanto, se existir uma reserva ativa para o jogo, o exemplar deverá ser direcionado para atendimento da reserva.

---

## RN10 — Reserva

Um usuário poderá reservar um jogo quando não existir exemplar disponível.

---

## RN11 — Reserva duplicada

Um usuário não poderá possuir mais de uma reserva ativa para o mesmo jogo.

---

## RN12 — Fila de reservas

As reservas deverão ser atendidas pela ordem de criação.

Exemplo:

```text
10:00 → João
11:30 → Maria
14:00 → Pedro
```

A ordem será:

```text
1º João
2º Maria
3º Pedro
```

---

## RN13 — Atendimento de reserva

Quando um exemplar de um jogo for devolvido e existir uma reserva ativa:

```text
Devolução
    ↓
Existe reserva?
    ↓
   SIM
    ↓
Selecionar primeira reserva
    ↓
Atender reserva
    ↓
Disponibilizar exemplar ao usuário
```

A implementação poderá escolher como representar o período em que o exemplar fica reservado para o usuário, desde que o comportamento seja consistente com o domínio.

---

## RN14 — Cancelamento de reserva

Uma reserva ativa poderá ser cancelada pelo usuário.

Uma reserva cancelada não poderá voltar a ser ativa.

---

## RN15 — Integridade dos exemplares

Um exemplar em:

```text
MANUTENCAO
```

ou:

```text
INDISPONIVEL
```

não poderá ser emprestado.

---

## RN16 — Danos

Durante uma devolução, o estado de conservação do exemplar deverá poder ser atualizado.

Caso o exemplar esteja danificado, poderá ser encaminhado para manutenção.

---

## RN17 — Histórico

Empréstimos finalizados deverão permanecer armazenados para fins de histórico.

O sistema não deverá simplesmente apagar um empréstimo após sua devolução.

---

## RN18 — Exclusão lógica

Quando apropriado, registros importantes deverão ser inativados em vez de fisicamente removidos.

Isso é especialmente importante para preservar histórico.

---

# 16. Casos de Uso

O sistema deverá implementar, no mínimo, os seguintes casos de uso.

## Usuários

```text
Cadastrar usuário
Consultar usuário
Listar usuários
Atualizar usuário
Bloquear usuário
Desbloquear usuário
Inativar usuário
Consultar histórico do usuário
```

---

## Jogos

```text
Cadastrar jogo
Consultar jogo
Listar jogos
Atualizar jogo
Inativar jogo
Pesquisar jogo por nome
Filtrar jogos por categoria
Consultar disponibilidade
```

---

## Categorias

```text
Cadastrar categoria
Consultar categoria
Listar categorias
Atualizar categoria
Inativar categoria
```

---

## Exemplares

```text
Cadastrar exemplar
Consultar exemplar
Listar exemplares de um jogo
Atualizar estado de conservação
Colocar exemplar em manutenção
Retirar exemplar da manutenção
Consultar status
```

---

## Empréstimos

```text
Realizar empréstimo
Consultar empréstimo
Listar empréstimos ativos
Listar empréstimos atrasados
Consultar histórico
Devolver exemplar
Finalizar empréstimo
```

---

## Reservas

```text
Criar reserva
Cancelar reserva
Consultar reserva
Listar reservas do usuário
Listar reservas do jogo
Consultar posição na fila
Atender reserva
```

---

# 17. Consultas e Informações do Sistema

A aplicação deverá disponibilizar informações úteis para o usuário.

## Dashboard

Uma tela inicial poderá apresentar:

```text
Total de jogos
Total de exemplares
Exemplares disponíveis
Exemplares emprestados
Empréstimos atrasados
Reservas ativas
```

---

## Detalhes de um jogo

Ao consultar um jogo, o usuário deverá conseguir visualizar:

```text
Nome
Descrição
Categorias
Número de jogadores
Idade mínima
Duração
Editora
Exemplares
Quantidade disponível
Quantidade emprestada
Quantidade em manutenção
Fila de reservas
```

---

## Detalhes de um usuário

Deverão ser apresentadas informações como:

```text
Nome
E-mail
Status
Data de cadastro

Empréstimos atuais
Empréstimos atrasados
Histórico
Reservas ativas
```

---

# 18. Camada Domain

O módulo `domain` representa o núcleo conceitual do sistema.

Ele deverá conter:

```text
Entidades
Value Objects
Enums
Regras fundamentais
Interfaces de Repository
```

Exemplo:

```text
domain
│
├── usuario
│   ├── Usuario
│   └── StatusUsuario
│
├── jogo
│   ├── Jogo
│   ├── Categoria
│   ├── Exemplar
│   ├── StatusExemplar
│   └── EstadoConservacao
│
├── emprestimo
│   ├── Emprestimo
│   ├── ItemEmprestimo
│   └── StatusEmprestimo
│
└── reserva
    ├── Reserva
    └── StatusReserva
```

O domínio deverá evitar dependências de infraestrutura.

---

# 19. Camada Application

A camada `application` deverá implementar os casos de uso.

Exemplo:

```text
application
│
├── usuario
├── jogo
├── exemplar
├── emprestimo
└── reserva
```

Cada caso de uso deverá coordenar as operações necessárias.

Por exemplo:

```text
RealizarEmprestimo

1. Buscar usuário
2. Validar usuário
3. Verificar empréstimos atrasados
4. Verificar limite
5. Buscar exemplares
6. Validar disponibilidade
7. Criar empréstimo
8. Alterar estado dos exemplares
9. Persistir
```

---

# 20. Camada Data

A camada `data` será responsável pela infraestrutura de persistência.

Ela poderá conter:

```text
data
│
├── firebase
├── repository
├── mapper
└── datasource
```

O acesso ao Firebase deverá ficar restrito a essa camada ou às abstrações de infraestrutura apropriadas.

---

# 21. Camada Presentation

A camada de apresentação será desenvolvida em Kotlin utilizando Compose Multiplatform.

Uma possível estrutura:

```text
presentation
│
├── navigation
├── screens
│   ├── home
│   ├── usuarios
│   ├── jogos
│   ├── exemplares
│   ├── emprestimos
│   └── reservas
│
├── components
├── viewmodel
└── state
```

A apresentação não deverá implementar as regras de negócio.

Por exemplo, a seguinte decisão não deverá ser tomada diretamente pela tela:

```text
"Este usuário possui empréstimo atrasado,
portanto não pode realizar o empréstimo."
```

A tela deverá solicitar a operação ao caso de uso, e apresentar o resultado.

---

# 22. Exemplo de Fluxo Completo

Considere o seguinte cenário:

```text
Usuário:
João

Jogo:
Catan

Exemplar:
Catan #001
```

O exemplar está:

```text
DISPONIVEL
```

João solicita um empréstimo.

O fluxo deverá ser:

```text
Compose UI
    ↓
ViewModel
    ↓
RealizarEmprestimo
    ↓
Validar usuário
    ↓
Verificar limite
    ↓
Verificar atrasos
    ↓
Verificar exemplar
    ↓
Criar Emprestimo
    ↓
Criar ItemEmprestimo
    ↓
Alterar Exemplar
    ↓
Repository
    ↓
Firebase
```

Após a operação:

```text
Exemplar:
EMPRESTADO

Empréstimo:
ATIVO
```

---

# 23. Testes

Deverão ser desenvolvidos testes automatizados para as principais regras de negócio.

No mínimo, deverão ser testados cenários como:

### Empréstimo

```text
Usuário ativo consegue emprestar.
Usuário bloqueado não consegue emprestar.
Usuário inativo não consegue emprestar.
Usuário com empréstimo atrasado não consegue emprestar.
Usuário que atingiu o limite não consegue emprestar.
Exemplar indisponível não pode ser emprestado.
Exemplar emprestado não pode ser emprestado novamente.
```

### Devolução

```text
Exemplar emprestado pode ser devolvido.
Exemplar disponível não pode ser devolvido.
Exemplar danificado pode ser encaminhado para manutenção.
Devolução pode ativar o atendimento de uma reserva.
```

### Reserva

```text
Usuário pode reservar jogo indisponível.
Usuário não pode criar reserva duplicada.
Reserva pode ser cancelada.
Reservas são atendidas por ordem cronológica.
```

Os testes de domínio e aplicação deverão ser capazes de executar sem depender da interface gráfica.

---

# 24. Requisitos de Qualidade

Além dos requisitos funcionais, a aplicação deverá observar os seguintes requisitos arquiteturais.

### RQ01 — Modularidade

Os módulos deverão possuir responsabilidades bem definidas.

### RQ02 — Baixo acoplamento

Alterações na interface não deverão exigir alterações nas regras de negócio.

### RQ03 — Independência de persistência

O domínio não deverá conhecer detalhes do Firebase.

### RQ04 — Testabilidade

As principais regras de negócio deverão poder ser testadas isoladamente.

### RQ05 — Manutenibilidade

A estrutura deverá permitir a inclusão de novas funcionalidades sem modificar desnecessariamente componentes não relacionados.

### RQ06 — Multiplataforma

A arquitetura deverá permitir a evolução da camada de apresentação para diferentes plataformas suportadas pelo Compose Multiplatform.

---

# 25. Possíveis Evoluções

Após a implementação dos requisitos obrigatórios, poderão ser adicionadas funcionalidades como:

- sistema de multas;
- notificações de atraso;
- notificações de reserva disponível;
- avaliações dos jogos;
- ranking dos jogos mais emprestados;
- jogos favoritos;
- recomendações;
- histórico de alterações;
- usuários administradores;
- diferentes níveis de permissão;
- estatísticas de utilização;
- integração com catálogo externo de jogos;
- QR Code para identificação dos exemplares;
- sistema de pontuação dos usuários.

Essas funcionalidades deverão ser utilizadas, quando aplicável, para demonstrar a capacidade da arquitetura de evoluir sem grandes alterações nos módulos existentes.

---

# 26. Resultado Esperado

Ao final do projeto, deverá existir uma aplicação funcional capaz de administrar uma coleção de jogos de tabuleiro e controlar todo o processo de empréstimo e reserva.

Mais importante do que a quantidade de telas ou funcionalidades implementadas será a qualidade da arquitetura.

A equipe deverá ser capaz de demonstrar:

```text
Como o domínio foi modelado?
Onde estão as regras de negócio?
Como os módulos se comunicam?
Como a persistência foi abstraída?
Como o Firebase foi isolado?
Como a interface se comunica com os casos de uso?
Como a arquitetura permite testes?
Como a aplicação poderia evoluir?
O que aconteceria se o Firebase fosse substituído?
O que aconteceria se a interface Desktop fosse substituída por uma interface Mobile?
```

O projeto deverá demonstrar que uma aplicação de software pode ser organizada de forma que **seus conceitos e regras fundamentais permaneçam independentes das tecnologias utilizadas para interface e persistência**.

A arquitetura deverá ser considerada parte fundamental do projeto, e não apenas uma organização de diretórios.
