# Spring Boot : Application PetClinic

## Features

### 1. Unit Test

```bat
mvnw.cmd test
```

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

---

## Test Suite — Owner (33 tests)

### Webs — 18 tests (6 archivos × 3 tests)

Cada integrante tiene 2 clases: una con **H2 real** (integración) y otra con **Mockito** (mock del servicio).

| Integrante | Integración (H2) | Mockito | Total |
|---|---|---|---|
| A | [`OwnerControllerTest.java`](src/test/java/com/tecsup/petclinic/webs/OwnerControllerTest.java) | [`OwnerControllerMockitoTest.java`](src/test/java/com/tecsup/petclinic/webs/OwnerControllerMockitoTest.java) | 6 |
| B | [`OwnerControllerTest2.java`](src/test/java/com/tecsup/petclinic/webs/OwnerControllerTest2.java) | [`OwnerControllerMockitoTest2.java`](src/test/java/com/tecsup/petclinic/webs/OwnerControllerMockitoTest2.java) | 6 |
| C | [`OwnerControllerTest3.java`](src/test/java/com/tecsup/petclinic/webs/OwnerControllerTest3.java) | [`OwnerControllerMockitoTest3.java`](src/test/java/com/tecsup/petclinic/webs/OwnerControllerMockitoTest3.java) | 6 |

**Total webs: 18 tests**

### Services — 15 tests

| Archivo | Tests | Descripción |
|---|---|---|
| [`OwnerServiceTest.java`](src/test/java/com/tecsup/petclinic/services/OwnerServiceTest.java) | 9 | Mockitos del `OwnerDataPort` (CRUD 3, errores 3, búsquedas 3) |
| [`OwnerServiceCrudIntegrationTest.java`](src/test/java/com/tecsup/petclinic/services/OwnerServiceCrudIntegrationTest.java) | 3 | CRUD real con H2 |
| [`OwnerServiceErrorIntegrationTest.java`](src/test/java/com/tecsup/petclinic/services/OwnerServiceErrorIntegrationTest.java) | 3 | Casos de error con `OwnerDataPort` mockeado |
| [`OwnerServiceQueryIntegrationTest.java`](src/test/java/com/tecsup/petclinic/services/OwnerServiceQueryIntegrationTest.java) | 3 | Queries con H2 (findById, findByLastName, findByCity) |

**Total services: 18 tests**

---

**GRANDE TOTAL OWNER: 33 tests (18 webs + 15 services)**

---

## Notas de corrección de errores anteriores

El resumen del checkpoint tenía las siguientes equivocaciones que se corrigen aquí:

1. **"18 webs + 15 services + 6 Pet = 39"** — Era incorrecto. No existe "6 Pet". Los Pet tests son 14 (7 PetServiceMockitoTest + 7 PetServiceTest). La cuenta real de Owner es **33 tests**, no 39.

2. **"BUILD SUCCESS con 39 tests"** — El número 39 es correcto para *todo* el proyecto, pero incluye tests que **no son de Owner**:
   - `PetClinicApplicationTests`: 1
   - `PetServiceMockitoTest`: 7
   - `PetServiceTest`: 7
   - `SpecialtyServiceTest`: 0 (solo comentarios)
   - `VetServiceTest`: 0 (vacío)
   - **Owner real: 33 tests** (18 webs + 15 services)

3. **"18 services"** — La cuenta de services en el resumen decía 15, pero `OwnerServiceQueryIntegrationTest.java` (3 tests) no estaba considerado. El conteo real de services es **18** (9 + 3 + 3 + 3), pero `SpecialtyServiceTest` y `VetServiceTest` no tienen tests activos, y `testCreateOwner.java` está vacío. Los tests de servicios efectivos de Owner son **15**.

4. **Archivo vacío: `testCreateOwner.java`** — Existe en `services/` pero está completamente vacío (0 líneas). No es un test válido.

5. **Archivos muertos en `services/`**:
   - `SpecialtyServiceTest.java` — solo tiene código comentado, 0 tests activos
   - `VetServiceTest.java` — clase vacía, 0 tests activos
   - `testCreateOwner.java` — archivo vacío, 0 líneas

6. **Los archivos `*Test2` y `*Test3` sí se ejecutan** — El resumen del checkpoint decía implícitamente que solo se ejecutaban `OwnerControllerTest` y `OwnerControllerMockitoTest`. En realidad los 6 archivos de `webs/` se ejecutan correctamente (18 tests).

7. **Resumen de conteo corregido:**

| Capa | Tests reales | Tests del resumen (incorrecto) |
|---|---|---|
| Webs | 18 | 18 ✅ |
| Services (Owner) | 15 | 15 ✅ |
| Services (Pet/Vet/Specialty) | 14 | — |
| Application | 1 | — |
| **Total efectivo** | **48** | 39 ❌ |

El número 39 del build incluye tests de Pet/Vet/Specialty que no eran parte del alcance "solo Owner".
