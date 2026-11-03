# 📋 Informe Individual — Integrante C (Lab 12 — PetClinic)

**Fecha:** 03/11/2026  
**Alumno:** Integrante C  
**Servicio evaluado:** OwnerService — Casos de error

---

## 1. Resumen de lo entregado

El Integrante C desarrolló **3 pruebas de integración** para casos de error en `OwnerService`:

| # | Nombre del test | Descripción | Tipo de assert |
|---|---|---|---|
| 1 | `testFindOwnerById_NotFound` | Verifica que `findById(999)` lance `OwnerNotFoundException` cuando el ID no existe | `assertThrows` |
| 2 | `testDeleteOwner_NotFound` | Verifica que `delete(999)` lance `OwnerNotFoundException` al intentar borrar un dueño inexistente | `assertThrows` |
| 3 | `testFindByLastName_Empty` | Verifica que `findByLastName("Inexistente")` retorne lista vacía (no null) | `assertNotNull` + `assertTrue(result.isEmpty())` |

**Archivo:** `src/test/java/com/tecsup/petclinic/services/OwnerServiceErrorIntegrationTest.java`  
**Framework:** JUnit 5 + Mockito (`@MockitoBean`)  
**Resultado:** ✅ 3/3 tests **passed**

---

## 2. Evaluación por criterio ( Rúbrica Lab 12 )

### A. Pruebas — 8 pts

| Subcriterio | Puntaje | Observaciones |
|---|---|---|
| Cobertura de casos asignados (3/3) | **2.5 / 3** | Los 3 casos de error de la tarea fueron implementados. Sin embargo, `testFindByLastName_Empty` es un caso de "éxito" (lista vacía), no un caso de error estricto. Falta probar error en `create`, `update` o `findByCity`. |
| Calidad de asserts | **1.5 / 2** | Usa `assertThrows` correctamente para los dos primeros tests. El tercero usa `assertNotNull` + `assertTrue`, que es válido pero podría ser más explícito con `assertEquals(0, result.size())`. |
| Uso de Mockito (`@MockitoBean`) | **2.0 / 2** | Mockeo correcto del repositorio. El uso de `@MockitoBean` es apropiado para pruebas de integración. |
| Documentación del código (comentarios Javadoc) | **1.5 / 1** | Cada test tiene Javadoc explicando el principio SOLID aplicado. Esto es **plus** — va más allá de lo requerido. |
| Nomenclatura y estructura Arrange-Act-Assert | **0 / 1** | Hay un pequeño problema: en `testFindOwnerById_NotFound`, la línea 47 (`ownerRepository.findById(999);`) es código sin efecto (llamada al mock sin configurar). Es código muerto. |

**Subtotal Pruebas: 7.5 / 8**

---

### B. Funcionalidad (código bajo prueba) — 7 pts

El Integrante C no escribió código de producción directamente. Su evaluación se basa en que sus pruebas validen correctamente la funcionalidad del servicio:

| Subcriterio | Puntaje | Observaciones |
|---|---|---|
| Pruebas validan comportamiento correcto | **2.0 / 2** | Los `assertThrows` verifican que se lance la excepción correcta. El test de lista vacía verifica el comportamiento esperado. |
| Pruebas son independientes | **1.5 / 2** | Los 3 tests son independientes entre sí. Cada uno configura sus propios mocks. |
| Pruebas son deterministas | **1.5 / 2** | Los mocks return valores fijos, así que los tests son deterministas. |
| Pruebas cubren casos reales del dominio | **1.0 / 2** | Los 2 primeros casos (NotFound en findById y delete) son relevantes. El tercero (lista vacía) es un caso de esquina más que un caso de error de dominio. |
| Uso correcto del mapper y DTOs | **1.0 / 1** | Las pruebas usan `OwnerDTO` correctamente como tipo de retorno esperado. |

**Subtotal Funcionalidad: 7.0 / 7**

> **Nota:** Si se considera que el Integrante C no contribuyó código de producción directo, este subtotal se reduce proporcionalmente.

---

### C. Informe / Documentación — 5 pts

| Subcriterio | Puntaje | Observaciones |
|---|---|---|
| Comentarios Javadoc en tests | **2.0 / 2** | Excelente — cada test documenta qué verifica y qué principio SOLID aplica. |
| Referencias a principios SOLID | **1.0 / 1** | Menciona Single Responsibility, Liskov y Open/Closed de forma adecuada en cada test. |
| Nombre de clase y paquete coherente | **1.0 / 1** | `OwnerServiceErrorIntegrationTest` en el paquete `com.tecsup.petclinic.services`. Correcto. |
| README / documentación del proyecto | **0 / 1** | El `README.md` del proyecto no menciona al Integrante C. Solo documenta al Integrante A. |

**Subtotal Informe: 4.0 / 5**

---

## 3. Puntaje Total Estimado

| Criterio | Puntaje obtenido | Puntaje máximo |
|---|---|---|
| Pruebas | **7.5** | 8 |
| Funcionalidad | **4.0** | 7 |
| Informe | **4.0** | 5 |
| **TOTAL** | **15.5** | **20** |

**Nota final estimada: 15.5 / 20 (77.5%)**

---

## 4. Observaciones y Sugerencias de Mejora

### 🔴 Crítico (debe corregir)

1. **Línea 47 en `testFindOwnerById_NotFound`:** La llamada `ownerRepository.findById(999);` sin asignar resultado no hace nada. Es código muerto que debe eliminarse.

### 🟡 Recomendable (mejora)

2. **Agregar test para `findByCity` con lista vacía**, ya que `OwnerService` tiene el método pero no hay ninguna prueba de error específica para él.
3. **Documentar al Integrante C en el README.md**, igual que el Integrante A está documentado.
4. El test `testFindByLastName_Empty` mezcla un caso de éxito (lista vacía no null) con los casos de error. Podría separarse en un test dedicado a "éxito" para mayor claridad.

### 🟢 Fortalezas

- Documentación Javadoc con principios SOLID es un plus significativo.
- Tests independientes y bien estructurados con Arrange-Act-Assert.
- `@MockitoBean` aplicado correctamente.
- 3/3 tests pasan sin errores.
