# 1. Cartão

## 1.1 Criar um novo cartão (POST /cartoes)
```bash
curl -X POST http://localhost:8080/cartoes -H "Content-Type: application/json" -d "{\"numeroCartao\":1234567890123456, \"dataValidade\":\"2025-12-31\", \"cvv\":123, \"limite\":5000.00, \"saldo\":5000.00, \"estaAtivado\":true}"
```

## 1.2 Buscar um cartão por ID (GET /cartoes/{cartaoId})
```bash
curl -X GET http://localhost:8080/cartoes/1
```

# 2. Cliente

## 2.1 Criar um novo cliente (POST /clientes)
```bash
curl -X POST http://localhost:8080/clientes -H "Content-Type: application/json" -d "{\"nome\":\"João Silva\", \"cpf\":\"123.456.789-00\", \"dataNascimento\":\"1990-01-01\", \"email\":\"joao.silva@email.com\", \"telefone\":\"(11) 99999-9999\", \"endereco\":\"Rua Exemplo, 123\"}"
```

## 2.2 Associar um cartão a um cliente existente (POST /clientes/{clienteId}/cartoes/{cartoesId})
```bash
curl -X POST http://localhost:8080/clientes/1/cartoes/1
```

## 2.3 Buscar um cliente por ID (GET /clientes/{clienteId})
``` bash
curl -X GET http://localhost:8080/clientes/1
```

# 3. Transação

## 3.1 Autorizar uma nova transação (POST /transacoes/autorizar/{cartaoId})
``` bash
curl -X POST http://localhost:8080/transacoes/autorizar/1 -H "Content-Type: application/json" -d "{\"dataTransacao\":\"2024-09-28T15:30:00\", \"valor\":150.00, \"comerciante\":\"Loja X\"}"
```

## 3.2 Buscar/Notificar todas as transações de um cliente (GET /transacoes/cliente/{clienteId})
``` bash
curl -X GET http://localhost:8080/transacoes/cliente/1
```
