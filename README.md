# Desafio Back-end PicPay

## Implementações

Existem duas implementações distintas desde desafio, uma utiliza ideias de [_Fluent Interfaces_](https://github.com/tacsio/simple-picpay/tree/main) e outra o padrão de
[_Service Layer_](https://github.com/tacsio/simple-picpay/tree/service-pattern) que é bastante comum.  

### Implementação Fluent Interfaces
O Quarkus permite a utilização do padrão _Active Record_ através da biblioteca Panache. Desta forma decidi criar uma 
implementação focando na simplicidade e clareza do código, onde a leitura das regras de negócio não passam por indireções
que apenas complicam o entendimento.

Nesta versão, para realizar um pagamento basta utilizar a seguinte construção:
```java
Transaction transaction = payer.pay(value, payee);
```
Se todos os passos e validações ocorrerem com sucesso, o resultado é uma transação finalizada (e dindin transferido entre
carteiras dos usuários).

#### Vantagens
1. Simplicidade de manutenação (o código e lógica de negócio concentrados nas entidades relevantes)
2. Evita modelos anêmicos (a lógica que pertence ao modelo está no modelo)

#### Desvantagens
1. Aspectos de infraestratura no objeto do modelo (Método transacional no modelo)
2. Aumento da complexidade na construção de testes unitários sobre os métodos de negócio do modelo (não será apenas a 
   instanciação do objeto e utilização dos métodos de negócio, será necessário resolver as dependências de infra)


### Implementação Service Pattern
Implementação utilizando uma camada de serviço. 

Nesta versão, para realizar um pagamento basta utilizar a seguinte construção:
```java
Transaction transaction = paymentService.makePayment(form);
```
Adicionei o formulário e criei 2 novas interfaces para garantir que não sejam feitos pagamentos com entidades 'trocadas' (o pagador receber por engano).
A transação neste ponto já está completada e confirmada. Assim como na outra implementação, a notificação é enviada reativamente (utilizando CompletableFutures do Java).

### Vantagens
1. A primeira vista tem a vantagem da 'visibilidade' da transação estar concentrada na camada de serviço. Mesmo que seja possível, porém atualmente não necessário, propagar a transação
2. Facilita o teste de algumas regras de negócio isoladamente, por exemplo, a carteira e usuários (esse ponto é discutível já que se a transação fosse isolada em outro ponto, na versão fluent, teria o mesmo comportamento)

### Desvantagens
1. Possívelmente, dependendo da evolução nas regras, a classe de serviço pode demandar muitas mudanças, já que concentra o fluxo de transferências.


## Objetivo - PicPay Simplificado

Temos 2 tipos de usuários, os comuns e lojistas, ambos têm carteira com dinheiro e realizam transferências entre eles.
Vamos nos atentar **somente** ao fluxo de transferência entre dois usuários.

Requisitos:

- Para ambos tipos de usuário, precisamos do Nome Completo, CPF, e-mail e Senha. CPF/CNPJ e e-mails devem ser únicos no
  sistema. Sendo assim, seu sistema deve permitir apenas um cadastro com o mesmo CPF ou endereço de e-mail.

- Usuários podem enviar dinheiro (efetuar transferência) para lojistas e entre usuários.

- Lojistas **só recebem** transferências, não enviam dinheiro para ninguém.

- Antes de finalizar a transferência, deve-se consultar um serviço autorizador externo, use este mock para
  simular (https://run.mocky.io/v3/8fafdd68-a090-496f-8c9a-3442cf30dae6).

- A operação de transferência deve ser uma transação (ou seja, revertida em qualquer caso de inconsistência) e o
  dinheiro deve voltar para a carteira do usuário que envia.

- No recebimento de pagamento, o usuário ou lojista precisa receber notificação enviada por um serviço de terceiro e
  eventualmente este serviço pode estar indisponível/instável. Use este mock para simular o
  envio (https://run.mocky.io/v3/b19f7b9f-9cbf-4fc6-ad22-dc30601aec04).

- Este serviço deve ser RESTFul.

### Payload

POST /transaction

```json
{
  "value": 100.00,
  "payer": 4,
  "payee": 15
}
```

## Info

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```
./mvnw quarkus:dev
```
