# 🪪 JWTBankSecurity
JWTBankSecurity é uma API REST em Java com Spring Boot que demonstra a implementação de autenticação usando JSON Web Tokens (JWT)

![Status](https://img.shields.io/badge/Status-Concluído-blue)

Este projeto foi desenvolvido para aprender e aplicar conceitos de segurança aplicados a APIs, incluindo:

✔ Geração de token JWT

✔ Validação de token

✔ Filtro de segurança para proteger rotas

✔ Integração com Spring Security

---

### 🧠 Sobre o projeto

JWT (JSON Web Token) é um padrão utilizado para autenticação stateless em aplicações web.*
Este projeto exemplifica como criar e validar tokens JWT em um backend Spring Boot, incluindo um filtro de segurança que intercepta requisições e autentica o usuário com base no token

⚙️ Dependencias usadas ( https://start.spring.io/ ): 

- PostgreSQL
- Spring Data JPA
- Spring Web
- Spring Security                      
- Spring Flyway | Controle de versão do banco de dados
- Validation | Validação de dados de entrada
- Lombok | Reduz código repetitivo (boilerplate)
- JWT :

```pom.xml
<dependency>
    <groupId>com.auth0</groupId>
    <artifactId>java-jwt</artifactId>
    <version>4.4.0</version>
</dependency>
```

---

### 🚀 Funcionalidades

🔑 Geração de Token (JWT)

- O projeto possui uma classe de configuração (TokenConfig) que:
- Gera um token JWT contendo claims como userId e email
- Assina o token com uma chave secreta
- Valida o token

🔒 Filtro de Segurança

A classe SecurityFilter estende OncePerRequestFilter para:


✔ Interceptar todas as requisições HTTP

✔ Extrair o token JWT do header Authorization

✔ Validar o token e autenticar o usuário caso seja válido

✔ Continuar o fluxo da requisição para o controller


### ✅Como executar e testar

1. **Clone o repositório:**

   ```bash
   git clone "https://github.com/Jullya-Nigro07/JWTBankSecurity.git"
    ```

2. **Configure o PostgreSQL**


- Utilize o PostgreSQL versão 18
- Crie um banco de dados com o nome de sua preferência:

   ```sql
   CREATE DATABASE my_users;
   ```

3. **Abra o projeto na IDE**


- Abra no IntelliJ IDEA (ou outra IDE compatível com Java 21)


4. **Configure o banco de dados**


- No arquivo application.properties (ou application.yml), ajuste as credenciais:


   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/NOME_DO_BANCO
   spring.datasource.username=postgres
   spring.datasource.password=SUA_SENHA
   ```

5. **Execute a aplicação**


- Rode a classe principal JWTBankSecurityApplication
- Aguarde o build finalizar

6. **Teste as rotas no Postman, Insomnia ou via HTTP.Request do Intelliji**

JWTBankSecurity --> 🌐request.http

---

### 📁 Estrutura do projeto

        src/
        └── main/
           └── java/
             └── dio.web.JWTBankSecurity/
                ├── config/ 
                │     ├── AuthConfig.java
                │     ├── JWTUserData.java
                │     ├── SecurityConfig.java
                │     ├── SecurityFilter.java
                │     └── TokenConfig.java
                │
                ├── controller/
                │     ├── AuthController.java 
                │     └── HomeController.java
                │
                ├── dto/
                │      ├── request/
                │           ├── LoginRequest.java
                │           └── RegisterUserRequest.java
                │      └── response/
                │           ├── LoginResponse.java
                │           └── RegisterUserRequest.java
                │
                ├── entity/
                │      └──  User.java
                │
                ├── repositoty/
                │      └── UserRepository.java
                │            
                └── JWTBankSecurityApplication.java
           └── resources/
                ├── db.migracion/     
                       └── V1__create_table_user.sql
---
