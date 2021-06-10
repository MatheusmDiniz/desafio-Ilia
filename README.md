# DESAFIO Ilia

Desafio para seleção de Desenvolvedor, o projeto foi desenvolvido com Java usando FrameWork SpringBoot.

Nas próximas seções serão apresentados os requisitos e o procedimento para realizar o setup da aplicação.

## Requisitos

Para subir o ambiente é necessário:

* Java 11
* Maven

## Configuração do Backend

Este projeto foi desenvolvido utilizando a arquitetura de REST.

### Importando Dependências

Para importar as dependencias basta ir no pacote principal e rodar o comando abaixo:

    mvn clean install -U

### API

Aplicação onde se encontra todos os seviços se encontra na rota:

    http://localhost:8080

Para iniciar o a api fora da IDE, basta executar o comando abaixo dentro da raiz do projeto:
 
    mvn spring-boot:run	
	
	
## Configurações dos projetos nas IDEs

Este projeto é um projeto Maven. Com isso, o processo de configuração dele é o padrão de qualquer projeto maven.

Basta importar um novo projeto maven apontando para o pom localizado na raíz do projeto.

Os demais detalhes de configurações fica a critério de cada IDE utilizada.	
