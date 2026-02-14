# Stock Control - Sistema de Controle de Estoque

Back-end de um sistema de controle de estoque industrial desenvolvido com Quarkus. Gerencia produtos, matérias-primas e suas associações, além de sugerir produção com base no estoque disponível priorizando produtos de maior valor.

## Stack

- Java 17
- Quarkus 3.17.5
- PostgreSQL 16
- Maven
- Docker Compose

## Como rodar

### Pré-requisitos

- Java 17+
- Maven 3.8+
- Docker e Docker Compose

### Subir o banco de dados

```bash
docker compose up -d
```

### Rodar a aplicação

```bash
mvn quarkus:dev
```

A API estará disponível em `http://localhost:8080`.

### Rodar os testes

```bash
mvn test
```

Os testes usam H2 em memória, não precisam do PostgreSQL rodando.

## Endpoints da API

### Products - `/api/products`

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/products` | Listar todos os produtos |
| GET | `/api/products/{id}` | Buscar produto por ID |
| POST | `/api/products` | Criar produto |
| PUT | `/api/products/{id}` | Atualizar produto |
| DELETE | `/api/products/{id}` | Deletar produto |

```json
{
  "name": "Mesa de escritório",
  "value": 450.00
}
```

### Raw Materials - `/api/raw-materials`

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/raw-materials` | Listar todas as matérias-primas |
| GET | `/api/raw-materials/{id}` | Buscar matéria-prima por ID |
| POST | `/api/raw-materials` | Criar matéria-prima |
| PUT | `/api/raw-materials/{id}` | Atualizar matéria-prima |
| DELETE | `/api/raw-materials/{id}` | Deletar matéria-prima |

```json
{
  "name": "Madeira",
  "stockQuantity": 500.00
}
```

### Product-RawMaterial - `/api/product-raw-materials`

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/product-raw-materials` | Listar todas as associações |
| GET | `/api/product-raw-materials/product/{productId}` | Listar por produto |
| POST | `/api/product-raw-materials` | Criar associação |
| PUT | `/api/product-raw-materials/{id}` | Atualizar associação |
| DELETE | `/api/product-raw-materials/{id}` | Deletar associação |

```json
{
  "productId": 1,
  "rawMaterialId": 1,
  "quantity": 10.00
}
```

### Production Suggestions - `/api/production-suggestions`

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/production-suggestions` | Obter sugestão de produção |

Retorna a lista de produtos sugeridos para produção com as quantidades e o valor total.

**Exemplo de resposta:**

```json
{
  "suggestedProducts": [
    {
      "productId": 1,
      "productName": "Mesa de escritório",
      "productValue": 450.00,
      "quantity": 3,
      "subtotal": 1350.00
    },
    {
      "productId": 2,
      "productName": "Cadeira",
      "productValue": 150.00,
      "quantity": 5,
      "subtotal": 750.00
    }
  ],
  "totalValue": 2100.00
}
```

## Algoritmo de Sugestão de Produção

Utiliza uma abordagem **greedy** (gulosa):

1. Ordena todos os produtos por valor decrescente
2. Para cada produto (do mais caro ao mais barato):
   - Calcula quantas unidades podem ser produzidas com o estoque atual
   - Se possível produzir ao menos 1 unidade, registra a quantidade e desconta as matérias-primas do estoque em memória
3. Retorna a lista de produtos sugeridos com quantidades e valor total

## Estrutura do Projeto

```
src/main/java/com/autoflex/stockcontrol/
├── entity/          # Entidades JPA (Product, RawMaterial, ProductRawMaterial)
├── repository/      # Repositórios Panache
├── dto/             # Objetos de transferência de dados
├── service/         # Lógica de negócio
└── resource/        # Endpoints REST
```
