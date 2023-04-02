# vault-approle

This apps demonstrates how to authenticate to HashiCorp Vault using token authentication method

1. Download Vault from its official website [hashicorp.com](https://developer.hashicorp.com/vault/downloads). Recommend the binary download for all OS

   [Screen Shot 2023-03-10 at 11.19.07 PM.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/c3055488-73cf-48ba-8c3e-2265fbdebb4e/Screen_Shot_2023-03-10_at_11.19.07_PM.png)

   1. Open a terminal
   2. Go to the directory where the binary file is saved
   3. Unzip the downloaded file

      ```bash
      unzip vault_1.13.0_darwin_amd64.zip
      ```

2. Go to the unzipped directory and start Vault server

   1. Run the below command to start a DEV server

      ```bash
      vault server -dev
      ```

   2. Make note of the following two variables on the screen
      1. unseal key
      2. root token

3. Login to Vault

   1. Open a new terminal session and run the below command

      ```bash
      export VAULT_ADDR='[http://127.0.0.1:8200](http://127.0.0.1:8200/)'
      ```

   2. Check vault status with

      ```bash
      vault status
      ```

   3. Login to vault

      ```bash
      vault login "<root token>"
      ```

4. Save secret to Vault

   We're adding four secrets to the HashiCorp Vault under `web-app/dev/database` path.

   - url=url-dev
   - driver=driver
   - username=username-dev
   - password=password-dev

   ```bash
   vault kv put -mount=secret web-app/dev/database url=url-dev driver=driver username=username-dev password=password-dev
   ```

   You can make the **environment** specific. **DON’T FORGET TO ADD THEM TO VAULT**

   ```bash
   vault kv put -mount=secret web-app/uat/database url=url-uat driver=driver username=username-uat password=password-uat

   vault kv put -mount=secret web-app/prod/database url=url-prod driver=driver username=username-prod password=password-prod
   ```

5. Retrieve secret from vault using Terminal

   ```bash
   vault kv get -mount=secret web-app/dev/database
   ```

6. Retrieve secret from Vault using API
   The following is the path that needs to be used in Postman or cURL to retrieve the secret via an API

   ```bash
   secret/data/web-app/dev/database
   ```

   - #### cURL command

   ```bash
   curl --location '[http://localhost:8200/v1/secret/data/web-app/dev/database](http://localhost:8200/v1/secret/data/web-app/dev/database)' \
   --header 'X-Vault-Token: hvs.TTT0xHPlSd2O7JemzRlgsvlh'
   ```

   - #### cURL response

   ```bash
   {
     "request_id": "501d3cca-e0aa-f4de-4252-4ec1a2836ffc",
     "lease_id": "",
     "renewable": false,
     "lease_duration": 0,
     "data": {
       "data": {
         "driver": "driver",
         "password": "password",
         "url": "url",
         "username": "vault-username"
       },
       "metadata": {
         "created_time": "2023-03-11T06:25:27.177431Z",
         "custom_metadata": null,
         "deletion_time": "",
         "destroyed": false,
         "version": 2
       }
     },
     "wrap_info": null,
     "warnings": null,
     "auth": null
   }
   ```

   - #### Postman

Retrieve secret from Spring Boot Application

Maven dependency needed for Java application to communicate with HashiCorp Vault. You can find more info about [Spring Boot Cloud Vault Token authentication](https://docs.spring.io/spring-cloud-vault/docs/current/reference/html/#vault.config.authentication.token) methods as well as other authentication methods.

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-vault-config</artifactId>
</dependency>
```

Spring Boot application properties for HashiCorp Vault. Don't forget to replace the <root token> with your token.

```yaml
server.port: 8080
spring:
  application.name: web-app
  profiles.active: dev
  cloud.vault:
    uri: http://localhost:8200
    authentication: TOKEN
    token: <root token>
  config.import: vault://secret/${spring.application.name}/${spring.profiles.active}/database
```

The last line of the yaml property becomes
`vault://secret/web-app/dev/database` which is where our secret is stored.

Use @Value annotation to access the property in the Vault.

```java
@Value("${url}")
private String username;

@Value("${driver}")
private String username;

@Value("${username}")
private String username;

@Value("${password}")
private String username;
```
