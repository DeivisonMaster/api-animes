<h1> API de Gerenciamento de Animes </h1>
<p>Api desenvolvida com Spring boot</p>

<h2>Descrição</h2>
<p>A aplicação é uma API desenvolvida em Java com Spring Boot.</p>

  
<h2>Estilo Arquitetural</h2>
<p>Action Based</p>

<h2>Padrão de Arquitetura</h2>
<p>MVC</p>

<h2>Segurança</h2>
Proteção contra CSRF com gerenciamento de tokens entre cliente x servidor

<br/>

<h2>Testes Realizados - Necessário habilitar banco h2 em application.properties</h2>
<p>Testes de unidade do Controller - Testes de unidade nos Endpoints</p> 
<p>Testes do Repositório - Validação dos métodos base do Spring Data JPA</p>
<p>Testes de Integração - Simulação de client Rest Template solicitando recursos da API</p>

<br/>

Executar o comando: mvn test 

<br/>
Executar o comando: mvn test -Ptestes-integracao
<br/>

ACESSO SWAGGER DOC:
1 http://<dominio-local>:8080/swagger-ui.html
<br/>
Endpoints:
![Screenshot](docs/swagger-img.PNG)

<br/>

  
<h2>Tecnologias Aplicadas</h2>
<p>Java 8</p>
<p>Apache Maven</p>
<p>Spring Boot</p>
<p>Spring IoC</p>
<p>Swagger</p>
<p>Bean Validation</p>
<p>Spring Data JPA</p>
<p>Devtools</p>
<p>Apache Tomcat</p>
<p>MySQL</p>
<p>IDE Spring Tool Suite</p>
<p>DataJpaTest</p>
<p>MockMvc</p>
<p>AssertJ</p>

