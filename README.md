# 🎵 Escalador - Sistema de Escalação Musical

Um sistema desktop para gerenciamento e escalação automática de membros de grupos musicais paroquiais, desenvolvido em Java com interface Swing.

## 📋 Sobre o Projeto

O **Escalador** é uma aplicação que automatiza o processo de criação de escalas musicais para cultos, missas e eventos, considerando a disponibilidade dos membros, suas funções e regras específicas do ministério de música.

### 🎯 Funcionalidades Principais

- **Gerenciamento de Membros**: Cadastro, edição e exclusão de membros com suas respectivas funções
- **Definição de Disponibilidade**: Configuração de dias e horários disponíveis para cada membro
- **Escalação Automática**: Geração inteligente de escalas baseada em algoritmos de justiça e disponibilidade
- **Edição Manual**: Possibilidade de ajustar manualmente as escalas geradas
- **Validação de Regras**: Sistema que impede conflitos (ex: pessoa tocando dois instrumentos)
- **Controle de Rodízio**: Prioriza membros menos escalados para garantir justiça na distribuição

## 🛠️ Tecnologias Utilizadas

- **Java 17**: Linguagem principal
- **Swing**: Interface gráfica
- **FlatLaf**: Look and Feel moderno
- **Maven/Eclipse**: Gerenciamento de dependências e desenvolvimento

## 🏗️ Arquitetura do Projeto

O projeto segue princípios de **Programação Orientada a Objetos** e implementa:

### 📁 Estrutura de Pacotes
```
src/br/com/escalador/
├── controllers/          # Controladores (MVC)
├── exceptions/           # Exceções customizadas
├── models/              # Modelos de dados
├── services/            # Lógica de negócio
├── ui/                  # Interface gráfica
└── utils/               # Utilitários
```

### 🎨 Padrões Implementados

- **MVC (Model-View-Controller)**: Separação clara de responsabilidades
- **Singleton**: Para configurações globais
- **Strategy**: Para diferentes algoritmos de escalação
- **Observer**: Para atualização da interface

### 🔧 Recursos OOP Implementados

- ✅ **Herança**: `Pessoa` herda de `Participante`
- ✅ **Polimorfismo**: Sobrescrita de métodos com `@Override`
- ✅ **Encapsulamento**: Atributos privados com getters/setters
- ✅ **Abstração**: Classes abstratas e interfaces
- ✅ **Composição**: Relacionamentos entre objetos
- ✅ **Tratamento de Exceções**: Exceções customizadas

## 🚀 Como Executar

### Pré-requisitos
- Java 17 ou superior
- IDE Java (Eclipse, IntelliJ, etc.) ou linha de comando

### Passos para Execução

1. **Clone ou baixe o projeto**
   ```bash
   git clone [url-do-repositorio]
   cd escalador
   ```

2. **Certifique-se de que o arquivo `membros.txt` existe**
   - O arquivo deve estar na raiz do projeto
   - Caso não exista, será criado automaticamente

3. **Execute a aplicação**
   ```bash
   # Via linha de comando
   javac -cp "lib/*:src" src/br/com/escalador/EscaladorUI.java
   java -cp "lib/*:src" br.com.escalador.EscaladorUI
   
   # Ou execute diretamente pela IDE
   ```

## 📖 Manual de Uso

### 1. **Gerenciamento de Membros**
- Clique em "Gerenciar Membros" no topo da tela
- Adicione novos membros ou edite existentes
- Configure funções (Cantor, Violão, Teclado, etc.)
- Defina disponibilidade no formato: `Dia-HH:mm` (ex: `Domingo-19:00`)
- Marque se o membro pode "cantar e tocar" simultaneamente

### 2. **Criação de Escalas**
- Selecione o dia da semana e horário do evento
- Configure quantas pessoas são necessárias para cada função
- Clique em "Gerar Escala"
- O sistema criará automaticamente a melhor combinação

### 3. **Edição Manual**
- Na seção "Escala Gerada (Editável)", você pode:
  - Trocar pessoas de função usando os dropdowns
  - O sistema valida automaticamente conflitos
  - Só aparecem opções válidas em cada dropdown

## ⚙️ Regras de Escalação

O sistema implementa as seguintes regras automaticamente:

1. **Disponibilidade**: Só escala pessoas disponíveis no dia/horário
2. **Máximo de Funções**: Pessoas normais só podem ter 1 função
3. **Cantar e Tocar**: Alguns membros podem acumular Cantor + 1 Instrumento
4. **Exclusividade de Instrumentos**: Ninguém pode tocar 2 instrumentos
5. **Justiça na Distribuição**: Prioriza quem foi menos escalado
6. **Validação Dinâmica**: Interface impede seleções inválidas

## 📂 Estrutura de Dados

### Arquivo `membros.txt`
```csv
Nome,Funcoes,Disponibilidade,VezesEscalado,CantaEToca
João Silva,Cantor;Violão,Domingo-19:00;Quarta-19:00,2,S
Maria Santos,Cantor,Domingo-19:00;Domingo-17:00,1,N
```

**Campos**:
- **Nome**: Nome completo do membro
- **Funcoes**: Lista separada por `;` das funções que pode exercer
- **Disponibilidade**: Lista separada por `;` no formato `Dia-Horario`
- **VezesEscalado**: Contador de quantas vezes foi escalado
- **CantaEToca**: `S` se pode cantar e tocar, `N` caso contrário

## 🎨 Interface

A aplicação possui interface moderna com:
- **Dark Theme**: Visual moderno e confortável
- **Layout Responsivo**: Adapta-se a diferentes tamanhos de tela
- **Validação Visual**: Feedback imediato sobre ações
- **Divisão Clara**: Seções bem organizadas para cada funcionalidade

## 🔄 Fluxo de Trabalho Típico

1. **Configuração Inicial**: Cadastre todos os membros com suas funções e disponibilidades
2. **Criação de Evento**: Defina dia, horário e necessidades do culto
3. **Geração Automática**: Deixe o sistema criar a escala inicial
4. **Ajustes Manuais**: Faça alterações necessárias na escala
5. **Finalização**: A escala está pronta para uso

## 🐛 Solução de Problemas

### Problema: "Arquivo não encontrado"
**Solução**: Certifique-se que `membros.txt` está na pasta raiz do projeto

### Problema: "Não consegue selecionar nos dropdowns"
**Solução**: Verifique se há membros cadastrados e se eles têm a função necessária

### Problema: "Escala vazia"
**Solução**: Confirme se os membros têm disponibilidade no dia/horário selecionado

## 👥 Contribuição

Este projeto foi desenvolvido como trabalho acadêmico para a disciplina de **Programação Orientada a Objetos**.

## 📄 Licença

Este projeto é de uso acadêmico e está disponível sob licença educacional.

---

## 🏆 Características Técnicas Avançadas

- **Algoritmo de Justiça**: Distribui escalações de forma equilibrada
- **Validação em Tempo Real**: Interface reativa que previne erros
- **Persistência de Dados**: Salva automaticamente alterações
- **Arquitetura Limpa**: Código bem estruturado e documentado
- **Tratamento de Exceções**: Recuperação elegante de erros

**Versão**: 1.0.0  
**Data**: Agosto 2025
