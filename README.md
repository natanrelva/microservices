# Projeto de Microsserviços

## Exercício 1: Implementação de um Microsserviço com Spring Boot

Descrição:

- Implemente um microsserviço em Spring Boot que gerencie uma lista de produtos. O
microsserviço deve permitir operações CRUD (Create, Read, Update, Delete) através de
uma API RESTful.

## Visão Geral

Este projeto é uma arquitetura baseada em microsserviços projetada para fornecer serviços escaláveis e de fácil manutenção. Cada serviço é projetado para lidar com uma capacidade de negócio específica e pode ser desenvolvido, implantado e escalado de forma independente.

## Serviços

O projeto consiste nos seguintes microsserviços:

- **Serviço de Produtos**: Gerencia uma lista de produtos, permitindo operações CRUD (Create, Read, Update, Delete) através de uma API RESTful.

## Pré-requisitos

- Java 11+
- Maven

## Uso

### Executando Localmente

1. Instale as dependências para o serviço de produtos:

    ```sh
    mvn clean install
    ```

2. Inicie o serviço:

    ```sh
    mvn spring-boot:run
    ```

## API RESTful

O serviço de produtos permite as seguintes operações CRUD:

- **Criar Produto**:

    ```sh
    POST /products
    {
        "name": "Produto 1",
        "price": 100.0
    }
    ```

- **Listar Produtos**:

    ```sh
    GET /products
    ```

- **Atualizar Produto**:

    ```sh
    PUT /products/{id}
    {
        "name": "Produto Atualizado",
        "price": 150.0
    }
    ```

- **Deletar Produto**:

    ```sh
    DELETE /products/{id}
    ```

## Documentação da API

A documentação da API é gerada automaticamente pelo Swagger e pode ser acessada em:

- `http://localhost:8080/swagger-ui.html`

## Testes

1. Execute os testes para o serviço de produtos:

    ```sh
    cd services/product-service
    mvn test
    ```

## Contribuindo

1. Faça um fork do repositório.
2. Crie um novo branch (`git checkout -b feature-branch`).
3. Faça suas alterações.
4. Commite suas alterações (`git commit -m 'Adicione uma nova funcionalidade'`).
5. Faça o push para o branch (`git push origin feature-branch`).
6. Abra um pull request.

## Licença

Este projeto é licenciado sob a Licença MIT.
