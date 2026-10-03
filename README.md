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
SolicitacaoEmprestimo
Emprestimo
ItemEmprestimo
Reserva
Devolucao
ProblemaDevolucao
AvaliacaoProprietario
AvaliacaoTomador
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
| proprietarioId | Identificador | Sim | Usuário que empresta esta cópia |
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

# 11. Solicitação e empréstimo entre pessoas

O fluxo tem três registros distintos: **solicitação → empréstimo → devolução**. `SolicitacaoEmprestimo` registra um pedido e a decisão do proprietário. `Emprestimo` registra o acordo aceito e passa a controlar a posse e o prazo depois que os itens são entregues. `Devolucao` registra o recebimento dos itens. A solicitação pode ser recusada ou cancelada sem criar um empréstimo.

O empréstimo é um acordo entre **duas pessoas**: o proprietário do exemplar (quem empresta) e o tomador (quem recebe). Um empréstimo pode conter vários exemplares, mas todos devem pertencer ao mesmo proprietário. Itens de proprietários diferentes exigem solicitações e empréstimos separados.

## Entidade `SolicitacaoEmprestimo`

Essa classe é útil porque o pedido existe antes de o proprietário responder e pode terminar recusado ou cancelado. Não deve ser confundida com `Reserva`: reserva mantém o usuário em uma fila por um jogo ainda indisponível; solicitação pede exemplares concretos a um proprietário. Quando uma oferta da fila for aceita, ela pode originar uma solicitação para o exemplar oferecido.

| Atributo | Tipo conceitual | Obrigatório | Descrição |
|---|---|---:|---|
| id | Identificador | Sim | Identificador único do pedido |
| tomadorId | Identificador | Sim | Usuário que pediu os exemplares |
| proprietarioId | Identificador | Sim | Usuário que decide se empresta |
| exemplarIds | Coleção de identificadores | Sim | Um ou mais exemplares do mesmo proprietário |
| reservaId | Identificador | Não | Reserva que originou a solicitação, se houver |
| dataSolicitacao | Data/Hora | Sim | Quando o pedido foi enviado |
| dataLimiteResposta | Data/Hora | Não | Prazo para responder, se o fluxo usar expiração |
| dataResposta | Data/Hora | Não | Quando o proprietário aceitou ou recusou |
| status | Enum | Sim | Situação do pedido |
| motivoRecusa | Texto | Não | Motivo informado ao recusar |
| emprestimoId | Identificador | Não | Empréstimo criado após o aceite |
| observacoes | Texto | Não | Mensagem ou acordo proposto |

Status:

```text
PENDENTE   aguardando decisão do proprietário
ACEITA     proprietário concordou; entrega ainda precisa ser confirmada
RECUSADA   proprietário não aceitou
CANCELADA  tomador retirou o pedido antes da entrega
EXPIRADA  pedido expirou sem resposta, se houver prazo de resposta
```

Um pedido aceito não significa que os itens já foram entregues. O `Emprestimo` resultante fica `AGUARDANDO_ENTREGA`; só a confirmação da entrega inicia o prazo. A solicitação permanece como histórico da decisão e referencia o empréstimo criado.

## Entidade `Emprestimo`

Representa o acordo aceito entre o proprietário e o tomador. Deve guardar referências estáveis (`UsuarioInfo` ou IDs) às duas pessoas e os itens aceitos. O empréstimo começa na entrega física confirmada, não quando o pedido é enviado ou aceito.

## Prazo padrão

O prazo padrão é de **7 dias corridos**, contados a partir da confirmação da entrega. O vencimento é calculado uma vez, nessa confirmação, e fica registrado no empréstimo. Uma futura configuração poderá mudar o prazo para novos empréstimos; não deve alterar retroativamente os já iniciados. Uma prorrogação, se vier a ser implementada, precisa ser aceita pelo proprietário antes do vencimento e atualizar o prazo registrado. Enquanto isso, não há prorrogação automática.

Todos os itens de um empréstimo vencem na mesma data. Isso deixa a regra simples e previsível. A devolução, porém, é controlada item a item: o tomador pode devolver parte dos itens antes do vencimento, e os demais continuam pendentes sob o mesmo prazo. O empréstimo só fica finalizado quando todos os itens forem devolvidos ou tiverem uma resolução registrada e todas as ocorrências estiverem encerradas.

Use `Instant` para os momentos de entrega e devolução e para o vencimento, armazenando os instantes em UTC e apresentando-os no fuso local. Assim, mudanças de fuso ou horário de verão não mudam o prazo calculado.

### Atributos de `Emprestimo`

| Atributo | Tipo conceitual | Obrigatório | Descrição |
|---|---|---:|---|
| id | Identificador | Sim | Identificador único |
| proprietarioId | Identificador | Sim | Pessoa que empresta; deve ser proprietária de todos os exemplares do empréstimo |
| tomadorId | Identificador | Sim | Pessoa que recebe os exemplares |
| solicitacaoId | Identificador | Sim | Pedido que originou o acordo |
| dataAceite | Data/Hora | Sim | Quando o proprietário aceitou a solicitação |
| dataEmprestimo | Data/Hora | Não | Confirmação da entrega; ausente enquanto aguardando entrega |
| dataPrevistaDevolucao | Data/Hora | Não | Vencimento calculado na entrega; ausente antes dela |
| status | Enum | Sim | Estado do fluxo |
| itens | Coleção de `ItemEmprestimo` | Sim | Exemplares incluídos no acordo |
| observacoes | Texto | Não | Acordos ou informações gerais |

`dataDevolucao` não deve ser um único campo no empréstimo: devoluções podem ocorrer em momentos diferentes para cada item. O histórico fica nos itens e nos eventos `Devolucao`.

## Status de `Emprestimo`

```text
AGUARDANDO_ENTREGA  solicitação aceita, aguardando entrega física
ATIVO               entrega confirmada e há itens pendentes dentro do prazo
ATRASADO             há ao menos um item pendente após o vencimento
AGUARDANDO_RESOLUCAO todos os itens foram recebidos, mas há problema ainda sem resolução
FINALIZADO           todos os itens foram devolvidos/resolvidos e todas as ocorrências foram encerradas
CANCELADO            acordo cancelado antes da entrega
```

`ATRASADO` pode ser calculado a partir do vencimento e dos itens pendentes, em vez de persistido, para evitar um status desatualizado. `AGUARDANDO_RESOLUCAO` aplica-se quando os itens já foram recebidos, mas há problema aberto ou contestado. O atraso de um item não apaga nem encerra o empréstimo.

### Relacionamentos

```text
Usuario (tomador) 1 ───── N SolicitacaoEmprestimo N ───── 1 Usuario (proprietário)
SolicitacaoEmprestimo 1 ───── 0..1 Emprestimo
Usuario (proprietário) 1 ───── N Emprestimo N ───── 1 Usuario (tomador)
Emprestimo 1 ───── N ItemEmprestimo
```

---

# 12. Entidade ItemEmprestimo

Representa um exemplar específico dentro de um acordo. O prazo é comum ao empréstimo, mas situação e devolução são acompanhadas individualmente. Isso permite receber dois jogos hoje e o terceiro depois sem perder o histórico.

## Atributos

| Atributo | Tipo conceitual | Obrigatório | Descrição |
|---|---|---:|---|
| id | Identificador | Sim | Identificador único do item |
| emprestimoId | Identificador | Sim | Empréstimo ao qual pertence |
| exemplarId | Identificador | Sim | Cópia física emprestada |
| estadoNaEntrega | Enum | Sim | Estado de conservação registrado antes da entrega |
| estadoNaDevolucao | Enum | Não | Estado observado no recebimento |
| componentesEntregues | Coleção de textos/IDs | Sim | Peças e acessórios conferidos na entrega; serve de referência para a devolução |
| componentesRecebidos | Coleção de textos/IDs | Não | Peças e acessórios conferidos no recebimento |
| dataEfetivaDevolucao | Data/Hora | Não | Quando o proprietário recebeu o item |
| status | Enum | Sim | Situação individual do item |
| observacaoEntrega | Texto | Não | Condição, acessórios ou ressalvas na entrega |
| observacaoDevolucao | Texto | Não | Problemas, peças faltantes ou divergências constatadas |
| resolucao | Texto | Não | Acordo ou desfecho de extravio/dano, se houver |

Status possíveis:

```text
PENDENTE      ainda está com o tomador; pode estar em dia ou atrasado
DEVOLVIDO     recebido pelo proprietário
EXTRAVIADO    perda confirmada e registrada, aguardando ou após resolução
```

Não marque como extraviado apenas porque venceu: nesse momento o item continua `PENDENTE` e aparece como atrasado. `EXTRAVIADO` exige confirmação/registro, preservando a diferença entre atraso e perda. Um item devolvido com dano continua `DEVOLVIDO`; o dano é registrado pelo estado e pela observação, e o exemplar pode ser encaminhado para manutenção. Para jogos com muitas peças, compare `componentesEntregues` e `componentesRecebidos`; as peças ausentes dão origem a um `ProblemaDevolucao`.

Um exemplar pode participar de vários itens ao longo do tempo, mas nunca de dois empréstimos ativos ao mesmo tempo.

Exemplo: João recebe de Maria dois exemplares no dia 1º. Ambos vencem no dia 8. João devolve um no dia 6 e o outro no dia 10: o primeiro item é concluído no dia 6; o empréstimo permanece atrasado até a devolução do segundo.

---

# 13. Entidade Reserva

Representa o interesse de um usuário em pegar emprestado um jogo quando não houver exemplar disponível para iniciar um empréstimo. A reserva é feita para o **Jogo**, não para uma cópia física; quando surgir uma possibilidade, o proprietário e o usuário ainda precisam confirmar o empréstimo entre si. A reserva não cria por si só um empréstimo nem transfere a posse do exemplar.

## Atributos

| Atributo | Tipo | Obrigatório | Descrição |
|---|---|---:|---|
| id | Identificador | Sim | Identificador único |
| usuarioId | Identificador | Sim | Usuário que realizou a reserva |
| jogoId | Identificador | Sim | Jogo reservado |
| exemplarOfertadoId | Identificador | Não | Cópia específica oferecida ao usuário |
| solicitacaoId | Identificador | Não | Solicitação criada a partir da oferta aceita |
| emprestimoId | Identificador | Não | Empréstimo que resultou da solicitação aceita |
| dataReserva | Data/Hora | Sim | Momento da reserva |
| status | Enum | Sim | Situação |
| posicao | Inteiro calculado | Não | Posição atual na fila; deve ser recalculada pela ordem da fila, não tratada como identidade permanente |
| dataOferta | Data/Hora | Não | Quando uma oportunidade foi comunicada ao usuário |
| dataExpiracaoOferta | Data/Hora | Não | Limite para aceitar a oportunidade |
| dataAtendimento | Data/Hora | Não | Quando a entrega confirmada inicia o empréstimo |
| dataCancelamento | Data/Hora | Não | Momento do cancelamento |
| observacoes | Texto | Não | Observações |

## Status

```text
ATIVA               aguardando na fila
OFERTA_PENDENTE     oportunidade comunicada, aguardando resposta
ATENDIDA            entrega confirmada e empréstimo correspondente iniciado
CANCELADA           cancelada pelo usuário ou pela equipe
EXPIRADA             oportunidade não aceita no prazo
```

### Relacionamentos

```text
Usuario 1 ───── N Reserva
Jogo 1 ───── N Reserva
```

Uma oferta deve informar qual exemplar/proprietário está disponível. Durante a janela da oferta, o exemplar fica `RESERVADO` para aquela pessoa e não pode ser oferecido a outra. Sugestão para o projeto: o primeiro usuário da fila recebe **48 horas** para responder. Se aceitar, cria-se uma `SolicitacaoEmprestimo` vinculada à reserva; após o aceite do proprietário, cria-se o empréstimo. A reserva passa a `ATENDIDA` somente quando a entrega confirmada inicia o empréstimo e seu prazo de 7 dias. Se não aceitar em 48 horas, a oferta expira e passa para a próxima pessoa; se não houver mais reservas, o exemplar volta a `DISPONIVEL`. Uma reserva expirada pode ser recriada pelo usuário se ainda tiver interesse.

---

# 14. Devolução e avaliação

## Devolução parcial ou completa

`Devolucao` representa um recebimento feito pelo proprietário. Um evento pode registrar um ou mais itens recebidos na mesma ocasião, e um mesmo empréstimo pode ter vários eventos de devolução. Para cada item recebido, registra-se a data efetiva, a condição constatada e eventuais observações. Isso é importante porque cada exemplar pode voltar em uma condição diferente.

### Atributos de `Devolucao`

| Atributo | Tipo conceitual | Obrigatório | Descrição |
|---|---|---:|---|
| id | Identificador | Sim | Identificador do evento de recebimento |
| emprestimoId | Identificador | Sim | Empréstimo ao qual a devolução pertence |
| recebidoPorId | Identificador | Sim | Proprietário que confirma o recebimento |
| dataHora | Data/Hora | Sim | Momento do recebimento |
| itens | Coleção de itens devolvidos | Sim | Um ou mais `ItemEmprestimo` recebidos nesse evento |
| observacoes | Texto | Não | Observações gerais do recebimento |

O detalhe de condição e dano pertence também ao `ItemEmprestimo`, para que o estado final de cada exemplar continue consultável. `Devolucao` não deve guardar notas de avaliação: receber um item e avaliar a outra pessoa são ações distintas.

## Entidade `ProblemaDevolucao`

Registra um problema encontrado na devolução de um item, como peça faltante, dano, acessório ausente ou divergência entre as condições registradas na entrega e no recebimento. O registro preserva o relato e o acordo sem decidir automaticamente que uma pessoa é culpada nem aplicar uma cobrança automática.

| Atributo | Tipo conceitual | Obrigatório | Descrição |
|---|---|---:|---|
| id | Identificador | Sim | Identificador da ocorrência |
| itemEmprestimoId | Identificador | Sim | Item específico relacionado ao problema |
| devolucaoId | Identificador | Sim | Evento em que o problema foi observado |
| relatadoPorId | Identificador | Sim | Participante que registrou o problema |
| tipo | Enum | Sim | `PECA_FALTANTE`, `DANO`, `ACESSORIO_FALTANTE` ou `OUTRO` |
| componentesFaltantes | Coleção de textos/IDs | Não | Peças ou acessórios ausentes, comparados à lista da entrega |
| descricao | Texto | Sim | O que foi observado; pode incluir evidências/referências a imagens |
| dataRegistro | Data/Hora | Sim | Quando a ocorrência foi registrada |
| respostaTomador | Texto | Não | Resposta ou contestação do tomador |
| status | Enum | Sim | `ABERTO`, `CONTESTADO` ou `RESOLVIDO` |
| resolucao | Texto | Não | Acordo alcançado ou decisão de mediação |
| resolvidoEm | Data/Hora | Não | Quando as partes ou a mediação encerraram a ocorrência |

Uma ocorrência aberta deve ser comunicada ao tomador, que pode responder e anexar contexto. As pessoas podem combinar, por exemplo, a devolução da peça, a reposição por peça equivalente, o reparo do dano ou outra solução adequada. Se discordarem, a ocorrência permanece `CONTESTADO` até uma mediação definida pela comunidade. Registre a solução; não invente uma multa automática. O exemplar pode ficar `MANUTENCAO` ou `INDISPONIVEL` enquanto o dano afetar seu uso seguro ou completo.

O empréstimo não fica `FINALIZADO` enquanto houver ocorrência sem resolução. Depois que todas as peças voltarem ou a solução acordada for cumprida e registrada, marque a ocorrência como `RESOLVIDO`; o empréstimo poderá ser finalizado quando todos os seus itens e ocorrências estiverem resolvidos.

## Fluxo completo: do pedido às avaliações

1. **Solicitação:** o tomador escolhe um ou mais exemplares disponíveis do mesmo proprietário e envia `SolicitacaoEmprestimo` em `PENDENTE`. Se a origem for uma reserva, o pedido referencia `reservaId` e o exemplar ofertado.
2. **Decisão:** o proprietário aceita ou recusa. Na recusa, informa opcionalmente o motivo e o pedido termina sem criar empréstimo. No aceite, cria-se `Emprestimo` em `AGUARDANDO_ENTREGA` e a solicitação guarda seu `emprestimoId`.
3. **Entrega:** proprietário e tomador conferem condição física, peças e acessórios. Registra-se a condição e `componentesEntregues` de cada `ItemEmprestimo`; confirmada a entrega, o empréstimo passa a `ATIVO`, grava `dataEmprestimo` e calcula `dataPrevistaDevolucao` para sete dias corridos depois. Os exemplares passam a `EMPRESTADO`.
4. **Uso e prazo:** itens podem voltar separadamente, mas todos têm o mesmo vencimento. Item ainda pendente depois do vencimento fica atrasado e impede o tomador de iniciar novo empréstimo. Atraso não marca automaticamente o jogo como perdido.
5. **Devolução e conferência:** o proprietário registra cada recebimento em `Devolucao`, confere condição e lista `componentesRecebidos` do item. Se tudo estiver presente e em condição aceitável, o item fica `DEVOLVIDO`. Se houver dano ou peça faltante, o item ainda fica registrado como recebido, e cria-se `ProblemaDevolucao` para documentar o ocorrido.
6. **Resposta e solução do problema:** o tomador recebe o relato e pode responder ou contestar. As partes combinam a devolução da peça, reposição equivalente, reparo ou outra solução aceita; divergências seguem para mediação da comunidade. Registra-se o resultado em `resolucao`. Não há cobrança ou penalidade automática. Enquanto não houver solução, a ocorrência continua aberta/contestada e o empréstimo fica `AGUARDANDO_RESOLUCAO` depois que os itens tiverem sido recebidos. O exemplar pode ficar fora de circulação.
7. **Finalização:** o empréstimo passa a `FINALIZADO` quando cada item foi devolvido ou teve extravio formalmente resolvido e não há `ProblemaDevolucao` aberto ou contestado. A finalização preserva o histórico, inclusive danos e soluções.
8. **Avaliações recíprocas:** depois da finalização, cada pessoa pode avaliar a outra separadamente. O proprietário avalia o tomador em `AvaliacaoProprietario`; o tomador avalia o proprietário em `AvaliacaoTomador`. Cada avaliação tem nota de 1 a 5 e comentário opcional sobre a experiência ou os problemas ocorridos. Cada participante pode enviar uma avaliação por empréstimo; uma avaliação não depende da outra e não altera o status final.

### Exemplo: peça de jogo perdida

Na entrega, proprietário e tomador registram que o jogo contém 100 peças, listando ou identificando as peças conferidas. Na devolução, faltam duas peças. O proprietário registra o recebimento e abre `ProblemaDevolucao` com as duas peças ausentes e uma descrição. O tomador pode confirmar o ocorrido ou contestar, por exemplo, informando que as peças já faltavam na entrega. As partes conferem o registro inicial e combinam como resolver: devolver as peças, conseguir reposições compatíveis, reparar/substituir o exemplar ou aceitar outra solução. A decisão e seu cumprimento ficam registrados. Até a solução, a ocorrência fica aberta ou contestada, o empréstimo não finaliza e o exemplar pode ficar indisponível. Resolvido o problema, finaliza-se o empréstimo e ambos podem registrar suas notas e comentários.

### Atraso, extravio e dano

- Se o prazo vencer e um item ainda não tiver sido recebido, ele permanece `PENDENTE`, passa a constar como atrasado e o empréstimo fica `ATRASADO` enquanto houver item pendente vencido. O sistema pode notificar as duas pessoas. O tomador não pode iniciar novos empréstimos enquanto tiver item atrasado ou extravio sem resolução.
- Atraso não equivale automaticamente a perda. Se as partes confirmarem que o item não será devolvido, registra-se `EXTRAVIADO` e a resolução acordada em `resolucao`; não se apaga o item nem seu histórico.
- Se o item for devolvido com problema, registra-se `estadoNaDevolucao`, `componentesRecebidos`, `observacaoDevolucao` e a data. O proprietário pode alterar o estado atual do `Exemplar` e colocá-lo em manutenção. Não se deve sobrescrever os dados da entrega, que são a condição de referência.
- Quando todos os itens forem devolvidos ou tiverem resolução registrada e todas as ocorrências estiverem encerradas, o empréstimo passa a `FINALIZADO`. Um exemplar com dano pode continuar indisponível para novos empréstimos até ser reparado.

## Avaliações recíprocas

O sistema deve permitir que as duas pessoas avaliem a experiência, mas sem misturar a avaliação com a devolução física. Após o empréstimo ser finalizado, cada participante pode enviar uma avaliação por empréstimo. As avaliações são independentes e opcionais; a falta de uma delas não impede o encerramento. Uma avaliação não deve ser editada depois de publicada, salvo fluxo administrativo explícito.

Para evitar ambiguidade nos nomes atuais:

- `AvaliacaoProprietario`: avaliação escrita pelo proprietário sobre o tomador;
- `AvaliacaoTomador`: avaliação escrita pelo tomador sobre o proprietário.

As duas classes podem compartilhar os mesmos campos, mas são registros separados para deixar explícita a direção da avaliação.

### Atributos de `AvaliacaoProprietario`

Escrita pelo proprietário para avaliar o tomador:

| Atributo | Tipo conceitual | Obrigatório | Descrição |
|---|---|---:|---|
| id | Identificador | Sim | Identificador da avaliação |
| emprestimoId | Identificador | Sim | Empréstimo que está sendo avaliado |
| avaliadorId | Identificador | Sim | Proprietário registrado no empréstimo |
| avaliadoId | Identificador | Sim | Tomador registrado no empréstimo |
| nota | Inteiro de 1 a 5 | Sim | Nota da experiência |
| comentario | Texto | Não | Comentário sobre a experiência, com limite de tamanho |
| dataAvaliacao | Data/Hora | Sim | Quando foi publicada |

### Atributos de `AvaliacaoTomador`

Escrita pelo tomador para avaliar o proprietário:

| Atributo | Tipo conceitual | Obrigatório | Descrição |
|---|---|---:|---|
| id | Identificador | Sim | Identificador da avaliação |
| emprestimoId | Identificador | Sim | Empréstimo que está sendo avaliado |
| avaliadorId | Identificador | Sim | Tomador registrado no empréstimo |
| avaliadoId | Identificador | Sim | Proprietário registrado no empréstimo |
| nota | Inteiro de 1 a 5 | Sim | Nota da experiência |
| comentario | Texto | Não | Comentário sobre a experiência, com limite de tamanho |
| dataAvaliacao | Data/Hora | Sim | Quando foi publicada |

O sistema deve permitir no máximo uma avaliação de cada direção por empréstimo, confirmar que as pessoas são as partes do acordo e só aceitar publicação depois da finalização. Comentários podem mencionar problemas já registrados, mas o relato factual, as respostas e a solução pertencem a `ProblemaDevolucao`, não à avaliação.

---

# 15. Modelo de Relacionamentos Completo

Relacionamentos principais:

```text
Jogo N ───── N Categoria
Jogo 1 ───── N Exemplar
Exemplar 1 ───── N ItemEmprestimo (histórico; no máximo um ativo)
Emprestimo 1 ───── N ItemEmprestimo
Usuario (proprietário) 1 ───── N Emprestimo N ───── 1 Usuario (tomador)
SolicitacaoEmprestimo 1 ───── 0..1 Emprestimo
Emprestimo 1 ───── N Devolucao
ItemEmprestimo 1 ───── N ProblemaDevolucao
Emprestimo 1 ───── 0..1 AvaliacaoProprietario
Emprestimo 1 ───── 0..1 AvaliacaoTomador
Usuario 1 ───── N Reserva N ───── 1 Jogo
```

---

# 16. Regras de Negócio

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

Um empréstimo será considerado atrasado quando a data atual ultrapassar seu vencimento e existir item `PENDENTE`.

Um usuário com ao menos um item atrasado ou um extravio ainda sem resolução não poderá iniciar novos empréstimos.

---

## RN05 — Prazo

O prazo padrão é de **7 dias corridos**, contado da confirmação da entrega. Todos os itens do mesmo empréstimo compartilham esse vencimento; cada item pode ser devolvido separadamente. O vencimento gravado não muda retroativamente quando a configuração mudar.

---

## RN06 — Disponibilidade

Somente exemplares com status `DISPONIVEL` poderão ser emprestados.

---

## RN07 — Exclusividade

Um exemplar não poderá estar associado a mais de um empréstimo ativo.

---

## RN08 — Empréstimo

Somente uma solicitação aceita pode originar um empréstimo. Depois do aceite, o empréstimo aguarda a entrega; ao confirmar a entrega, inicia-se o prazo e registra-se a posse:

```text
Exemplar DISPONIVEL → empréstimo confirmado → Exemplar EMPRESTADO
```

Um empréstimo agrupa somente exemplares do mesmo proprietário. Itens de proprietários diferentes exigem acordos separados. O tomador e o proprietário devem estar ativos.

---

## RN09 — Devolução

Cada exemplar pode ser devolvido individualmente. Ao receber um item:

```text
Exemplar EMPRESTADO → recebimento confirmado → disponível, reservado ou em manutenção
```

Registra-se um evento `Devolucao`, a data efetiva, condição e peças recebidas de cada item. O empréstimo só finaliza depois que todos os itens forem devolvidos ou extraviados com resolução registrada e todas as ocorrências estiverem resolvidas.

---

## RN10 — Reserva

Um usuário poderá reservar um jogo quando não houver exemplar disponível. Reserva é interesse em uma cópia, não empréstimo nem garantia de entrega; a pessoa proprietária e o tomador ainda confirmam o acordo.

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
Exemplar fica disponível
    ↓
Existe reserva ativa para o jogo?
    ↓
   SIM → marcar exemplar como RESERVADO e oferecer ao primeiro da fila por 48 horas
    ↓
Usuário aceita → criar solicitação
    ↓
Proprietário aceita → empréstimo AGUARDANDO_ENTREGA
    ↓
Entrega confirmada → iniciar empréstimo e marcar reserva como atendida
```

Se a oferta não for aceita em 48 horas, expira e passa para a próxima reserva. Sem outras reservas, o exemplar volta a `DISPONIVEL`. A fila é cronológica. O prazo de empréstimo começa apenas na entrega confirmada, não na criação da reserva, da solicitação nem na oferta.

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

Na retirada e no recebimento registra-se o estado de conservação separadamente. Se um item voltar danificado, registra-se o dano na devolução, atualiza-se o estado atual do exemplar e ele pode ser encaminhado para manutenção. Nunca substituir o estado observado na entrega.

---

## RN17 — Histórico

Empréstimos finalizados deverão permanecer armazenados para fins de histórico.

O sistema não deverá simplesmente apagar um empréstimo após sua devolução.

---

## RN18 — Exclusão lógica

Quando apropriado, registros importantes deverão ser inativados em vez de fisicamente removidos.

Isso é especialmente importante para preservar histórico.

---

## RN19 — Extravio

Um item vencido e não recebido continua pendente e atrasado; não é automaticamente considerado perdido. Só passa a `EXTRAVIADO` quando a perda for confirmada e registrada, junto de uma resolução acordada pelas pessoas envolvidas. Um extravio sem resolução mantém o empréstimo aberto e bloqueia novos empréstimos para o tomador. O histórico do item e do empréstimo é mantido.

---

## RN20 — Avaliações

Depois que o empréstimo for finalizado, proprietário e tomador podem avaliar um ao outro, cada um uma vez por empréstimo, com nota de 1 a 5 e comentário opcional. A avaliação é opcional e não altera o status do empréstimo. As avaliações devem registrar autor, destinatário e data.

---

## RN21 — Solicitação

O pedido deve ser registrado antes da decisão do proprietário. Somente o proprietário do exemplar pode aceitá-lo ou recusá-lo. Uma recusa ou cancelamento antes da entrega não cria um empréstimo ativo. Solicitações aceitas são preservadas como histórico e vinculadas ao empréstimo resultante.

---

## RN22 — Peças faltantes e danos

Na entrega, registrar condição e peças/acessórios conferidos por exemplar. Na devolução, registrar a condição observada e as peças recebidas. Diferenças devem criar `ProblemaDevolucao`, notificar o tomador e permitir resposta/contestação. Até acordo ou mediação registrado, o empréstimo não pode ser finalizado e o exemplar pode ficar indisponível. Não aplicar cobrança automática: registrar o reparo, reposição ou outro acordo aceito.

---

# 17. Casos de Uso

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
Criar solicitação de empréstimo
Aceitar ou recusar solicitação
Cancelar solicitação antes da entrega
Confirmar entrega e iniciar prazo do empréstimo
Consultar empréstimo
Listar empréstimos ativos
Listar empréstimos atrasados
Consultar histórico
Registrar devolução parcial ou completa
Registrar dano ou extravio e resolução
Finalizar empréstimo quando todos os itens estiverem resolvidos
Avaliar proprietário ou tomador após finalização
```

## Devoluções e problemas

```text
Registrar recebimento total ou parcial
Conferir peças e acessórios devolvidos
Relatar dano ou peça faltante
Responder ou contestar uma ocorrência
Registrar acordo ou resultado de mediação
Colocar exemplar danificado em manutenção
Consultar histórico de ocorrências do exemplar e do empréstimo
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
Responder a uma oferta de exemplar
```

---

# 18. Consultas e Informações do Sistema

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

# 19. Camada Domain

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
│   ├── SolicitacaoEmprestimo
│   ├── Emprestimo
│   ├── ItemEmprestimo
│   ├── Devolucao
│   ├── ProblemaDevolucao
│   ├── AvaliacaoProprietario
│   ├── AvaliacaoTomador
│   └── StatusEmprestimo
│
└── reserva
    ├── Reserva
    └── StatusReserva
```

O domínio deverá evitar dependências de infraestrutura.

---

# 20. Camada Application

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
SolicitarEmprestimo

1. Buscar tomador, proprietário e exemplares solicitados
2. Validar usuários, limite e pendências do tomador
3. Validar que os exemplares estão disponíveis e pertencem ao mesmo proprietário
4. Criar `SolicitacaoEmprestimo` em `PENDENTE`
5. Notificar o proprietário
6. Se recusada, preservar a decisão e encerrar sem empréstimo
7. Se aceita, criar `Emprestimo` em `AGUARDANDO_ENTREGA`
8. Na confirmação da entrega, registrar condição e peças, iniciar o prazo e atualizar os exemplares
```

---

# 21. Camada Data

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

# 22. Camada Presentation

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

# 23. Exemplo de Fluxo Completo

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

João envia uma solicitação ao proprietário do exemplar.

O fluxo deverá ser:

```text
Compose UI
    ↓
ViewModel
    ↓
SolicitarEmprestimo
    ↓
Validar tomador, proprietário e exemplar
    ↓
Criar SolicitacaoEmprestimo (PENDENTE)
    ↓
Proprietário aceita (ou recusa)
    ↓
Criar Emprestimo (AGUARDANDO_ENTREGA)
    ↓
Confirmar entrega e registrar peças/estado
    ↓
Iniciar prazo de 7 dias
    ↓
Alterar Exemplar para EMPRESTADO
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

Na devolução, o proprietário confere cada exemplar e seus componentes. Uma peça faltante gera `ProblemaDevolucao`; o tomador responde e as partes registram o acordo ou a mediação. Resolvidos todos os itens e problemas, o empréstimo fica `FINALIZADO`. Então cada usuário pode registrar sua avaliação recíproca, com nota e comentário opcional.

---

# 24. Testes

Deverão ser desenvolvidos testes automatizados para as principais regras de negócio.

No mínimo, deverão ser testados cenários como:

### Empréstimo

```text
Solicitação pendente não cria empréstimo ativo.
Somente o proprietário pode aceitar ou recusar sua solicitação.
Solicitação recusada não altera o status do exemplar para emprestado.
Aceite cria empréstimo aguardando entrega; a entrega inicia os 7 dias.
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
Item de um empréstimo ativo pode ser devolvido parcialmente.
Item já devolvido não pode ser devolvido duas vezes.
Devolução registra a condição de cada item sem apagar a condição da entrega.
Devolução com dano pode encaminhar o exemplar para manutenção.
Diferença entre peças entregues e recebidas cria ocorrência por item.
Tomador pode responder ou contestar uma ocorrência.
Empréstimo com ocorrência aberta ou contestada não pode ser finalizado.
Empréstimo só finaliza quando todos os itens forem devolvidos/resolvidos e os problemas encerrados.
Item vencido não recebido continua pendente e marca o empréstimo como atrasado.
Extravio confirmado fica registrado e exige resolução.
Devolução pode gerar uma oferta ao primeiro usuário da fila de reserva.
```

### Reserva

```text
Usuário pode reservar jogo indisponível.
Usuário não pode criar reserva duplicada.
Reserva pode ser cancelada.
Reservas são atendidas por ordem cronológica.
Oferta de reserva expira depois de 48 horas sem resposta.
Prazo do empréstimo começa na confirmação da entrega, não na reserva.
```

### Avaliações

```text
Cada participante pode avaliar o outro uma vez após o empréstimo ser finalizado.
Não se pode avaliar alguém que não participou do empréstimo.
Avaliação exige nota de 1 a 5 e registra autor, destinatário e data.
Avaliação é opcional e não impede a finalização do empréstimo.
Avaliação de proprietário avalia o tomador; avaliação de tomador avalia o proprietário.
Problemas factuais são registrados em ocorrência, não apenas no comentário da avaliação.
```

Os testes de domínio e aplicação deverão ser capazes de executar sem depender da interface gráfica.

---

# 25. Requisitos de Qualidade

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

# 26. Possíveis Evoluções

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

# 27. Resultado Esperado

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
