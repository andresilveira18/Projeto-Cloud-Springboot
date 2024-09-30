# Projeto Cloud AV1 - Sistema de Transação

## Descrição

Este projeto faz parte da avaliação AV1 da disciplina Projeto Cloud e foi desenvolvido por André Silveira e Daniel Gripa. O mini-mundo aborda um sistema de transação em que um cliente pode realizar operações utilizando um ou mais cartões. O relacionamento entre cliente, cartão e transação é representado pelo diagrama disponível no arquivo `EER-Mysql.png` na raiz do projeto.

## Arquitetura e Funcionalidades

O projeto é baseado em uma aplicação Java com Spring Boot, conectada a um banco de dados MySQL. A aplicação possui endpoints RESTful para gerenciar clientes, cartões, e transações.

## Diagrama EER

O arquivo `EER-Mysql.png` na raiz do projeto descreve a estrutura de relacionamento entre as tabelas `Cliente`, `Cartão`, e `Transação`, detalhando suas associações e cardinalidades.

## Configuração do Ambiente

### Requisitos

- **Java 11 ou superior**
- **MySQL** 
- **Maven** (para gerenciamento de dependências)

### Passos para Instalação

1. Clone este repositório:
    ```bash
    git clone https://github.com/andresilveira18/Projeto-Cloud-Springboot.git
    ```

2. Crie um arquivo `.env` na raiz do projeto, preenchendo os dados de conexão do MySQL conforme o template `template_arquivo_dotenv.txt`.

3. Construa o projeto com o Maven:
    ```bash
    mvn clean install
    ```

4. Execute a aplicação:
    ```bash
    mvn spring-boot:run
    ```

## Banco de Dados

A aplicação utiliza o MySQL como banco de dados. Certifique-se de ter um banco de dados configurado e os dados de conexão corretos no arquivo `.env`.

## Documentação da API

O projeto possui integração com o Swagger para facilitar a documentação e o teste dos endpoints.

Após iniciar a aplicação, você pode acessar a documentação da API pelo seguinte link:

[Swagger UI](http://localhost:8080/swagger-ui.html)

## Estrutura do Projeto

- **`src/main/java`**: Contém a implementação principal do projeto.
- **`src/main/resources`**: Contém arquivos de configuração e propriedades da aplicação.
- **`EER-Mysql.png`**: Diagrama de entidade-relacionamento que descreve a modelagem do banco de dados.
- **`template_arquivo_dotenv.txt`**: Template para criação do arquivo `.env` com informações de configuração do banco de dados.

## Contribuidores

- **André Silveira**
- **Daniel Gripa**
