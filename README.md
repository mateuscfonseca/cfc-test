# CFC Test
Esse projeto é um crud simples de Produtos com integração com o Kafka para os eventos de crud. Possui um front end para listar, adicionar, editar e deletar produtos.

## Tecnologias
- Java 17
- Maven
- Quarkus
- Vue.js 3
- Node 18
- Typescript
- Docker 
- Kafka 
- SQLServer 2019

## Como rodar o projeto
Na raiz do projeto você encontra um docker-compose.yml que está configurado para subir todos os serviços. Ele vai subir o banco de dados SQLServer, um container para fazer executar o DML no banco, os serviços do Kafka (com o zookeeper), um serviço para criar o topico de produto no kafka, o backend em quarkus, o front end em Vue, e um Nginx para fazer o roteamento das requisições do back e do front. 

Esses são os comandos para rodar o projeto.

Constroi as imagens
```sh
docker compose build

```

Roda os containers
```sh
docker compose up -d
```

### Detalhes
O kafka, o banco de dados usam imagens prontas. O backend, o front end e o nginx tem Dockerfiles especificos para cada um. 

Para o back-end e front-end foi usada a estratégia de multi-stage build do docker. Então a compilação e build é feito dentro do container docker. 

O nginx tem uma configuração customizada para receber requisições na porta 80 e redirecionar para o front ou back end, dependendo do path sendo requisito, /app vai para o  front-end, /api vai para o backend. 

## Desenvolvimento
Para subir o ambiente de desenvolvimento você vai precisar instalar:
- Java 17
- Node 18
- Maven (opcional, o projeto do quarkus disponibiliza um executável do maven)

### Back-end
Na raiz da pasta back-end temos um docker compose para subir uma instância do banco de dados SQLServer, junto com o serviço que faz o DML do database do projeto. A aplicação está configurada para gerar as tabelas, através das classes anotadas com @Entity. 

Para subir  o banco rode o comando do docker compose 

```sh
docker compose up -d
```
Após o banco ter subido, você pode rodar a aplicação quarkus em dev mode.

```sh
mvn quarkus:dev
```
O Quarkus sobe um broker do kafka quando rodamos a aplicação em modo dev ou realizando os testes.

#### Testes
Para rodar os tests utilize o maven

```sh
mvn verify
```

### Front-end
Na pasta de front-end/cfc-test rode o npm install para instalar as dependencias do package.json

```sh
npm install
```
Para subir o front basta rodar o scrip *serve*. Esta configurado para rodar na porta 8082.

No backend foi configurado o CORS no quarkus para o modo dev. Para o build de prod não precisa pois vão estar rodando na mesma URL. 

```sh
npm run serve
```

#### Troubleshooting
Se vc receber esse erro:

```sh
Error: @vitejs/plugin-vue requires vue (>=3.2.13) or @vue/compiler-sfc to be present in the dependency tree.
```

Certifique-se que está usando a versão 18 do node.






