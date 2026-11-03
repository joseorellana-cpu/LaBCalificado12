# Spring Boot : Application PetClinic

## Features  

### 1.  Unit Test  --> Tag v1.0.0

mvn test -Dspring.profiles.active=h2

### 2. Ejecutar la aplicación (prueba manual)

```bat
mvnw.cmd spring-boot:run
```

Con el perfil `h2` activo por defecto (base de datos en memoria, datos de `schema.sql` y `data.sql`).

| Recurso | URL |
| --- | --- |
| Panel de prueba (Owners / Pets) | http://localhost:8080/ |
| API raíz (Spring Data REST) | http://localhost:8080/api |
| Owners | http://localhost:8080/api/owners |
| Pets | http://localhost:8080/api/pets |
| Consola H2 | http://localhost:8080/h2 |

Credenciales H2: JDBC URL `jdbc:h2:mem:testdb`, usuario `sa`, contraseña `sa`.



### Integrante A - CRUD basico de OwnerService (pruebas de integracion)

Clase: src/test/java/com/tecsup/petclinic/services/OwnerServiceCrudIntegrationTest.java

- testCreateOwner - crear dueno correctamente.
- testUpdateOwner - actualizar datos de un dueno.
- testDeleteOwner - eliminar dueno.

Conflictos resueltos: (completar)
