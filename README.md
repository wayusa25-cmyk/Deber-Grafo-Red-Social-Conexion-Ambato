# Conexión Ambato · APE 4, Fase 2

Programa en Java de la red social **Conexión Ambato**. Representa a las personas y sus relaciones con un grafo dinámico de listas de adyacencia, y permite consultarlas y modificarlas desde un menú de consola.

Estudiante: Romo Nuñez Joseph Alejandro. Asignatura: Estructura de Datos.

## Qué hace el programa

Cada persona es un usuario de la red. Entre dos personas puede haber dos clases de relación:

- **Amistad.** Es recíproca. Si Andrés es amigo de Camila, Camila también es amiga de Andrés. Se guardan dos aristas, una en cada sentido.
- **Seguimiento.** Es dirigido. Diego puede seguir a Sofía sin que Sofía siga a Diego. Se guarda una sola arista, desde quien sigue hacia el seguido.

Al iniciar aparece un menú. La opción 1 carga el caso de la Fase 1 y el resto opera sobre esa red:

| Opción | Acción |
|---|---|
| 1 | Carga seis usuarios y cinco amistades |
| 2 | Registra una persona |
| 3 | Elimina una persona y todas las relaciones que la mencionan |
| 4 | Agrega una amistad |
| 5 | Crea un seguimiento |
| 6 | Elimina una amistad |
| 7 | Deja de seguir |
| 8 | Lista amigos, seguidos y seguidores |
| 9 | Cuenta los amigos de una persona |
| 10 | Muestra la red completa |
| 11 | Ejecuta la secuencia de comprobación de la Tabla 3 |
| 0 | Sale |

En las opciones 3 a 9 se puede escribir el código (`A04` o `a04`) o el nombre (`Andrés` o `Andres`). En la opción 8, Enter sin texto lista a todas las personas. Si alguien no tiene relaciones de ese tipo, el programa dice `Ninguno`. Si la persona no existe, lo indica en lugar de dejar el resultado vacío.

La red inicial es esta:

| Código | Usuario | Amigos | Grado |
|---|---|---|---|
| A01 | Sofía | Mateo, Andrés | 2 |
| A02 | Mateo | Sofía, Valeria | 2 |
| A03 | Valeria | Mateo, Andrés | 2 |
| A04 | Andrés | Valeria, Sofía, Camila | 3 |
| A05 | Camila | Andrés | 1 |
| A06 | Diego | Ninguno | 0 |

Las cinco amistades son (A01, A02), (A02, A03), (A03, A04), (A04, A01) y (A04, A05). Internamente quedan diez aristas, dos por cada amistad.

## Qué se tomó en cuenta

El caso es la gestión de relaciones entre personas en una red social. El grafo se eligió porque permite abstraer esa realidad: las personas son vértices y las relaciones son aristas, y una misma red puede mezclar amistades recíprocas con seguimientos dirigidos.

La estructura concreta es una **multilista de nodos propios**:

- `Persona` guarda el código y el nombre.
- `Vertice` guarda a la persona, la cabeza de sus aristas y el enlace al siguiente usuario.
- `Arista` guarda el destino, el tipo (`AMISTAD` o `SEGUIR`) y el enlace a la siguiente arista.

No se usan `HashMap`, `HashSet`, `ArrayList` ni `LinkedList`. La búsqueda recorre la lista de vértices y la revisión de duplicados recorre la lista de aristas del origen. Los auxiliares `existe` y `quitarHacia` también recorren esas listas: el primero evita repetir una relación del mismo tipo y el segundo, al eliminar una persona, retira las aristas que otros tienen hacia ella. Hay que recorrer toda la red porque esas aristas no viven en el vértice eliminado, sino en las listas de quienes lo apuntan.

Otras decisiones del modelo:

- El código se normaliza a minúsculas. `A01` y `a01` son la misma cuenta.
- No se acepta un código vacío, repetido, con espacios o con comas.
- No se permiten lazos ni dos aristas iguales entre el mismo par y del mismo tipo.
- Los usuarios se insertan al inicio. Al cargar A01…A06, la cabeza queda Diego, Camila, Andrés, Valeria, Mateo, Sofía.
- Las aristas se agregan al final de la lista del origen. Así `listarAmigos(A04)` conserva el orden de inserción: Valeria, Sofía y Camila.
- `listarSeguidos` recorre solo las aristas de esa persona. `listarSeguidores` recorre toda la red, porque el seguimiento está guardado en quien sigue. Por eso listar seguidores cuesta más.

La lista de adyacencia se usó en lugar de una matriz porque la red es dispersa: la mayoría de las personas no se relacionan entre sí, y cada arista necesita recordar su tipo. Una matriz convendría si casi todos estuvieran conectados o si se consultara con mucha frecuencia la existencia de una relación entre cualquier par. La estructura se eligió según las condiciones de este caso, no como una solución única para cualquier problema.


## Estructura

| Archivo | Contenido |
|---|---|
| `src/modelo/TipoRelacion.java` | `AMISTAD` y `SEGUIR` |
| `src/modelo/Persona.java` | Código y nombre |
| `src/modelo/Vertice.java` | Persona, lista de aristas y siguiente vértice |
| `src/modelo/Arista.java` | Destino, tipo y siguiente arista |
| `src/estructuras/ConexionAmbato.java` | Grafo y todas las operaciones |
| `src/app/Main.java` | Menú, carga inicial y secuencia de comprobación |
| `src/pruebas/PruebasFase2.java` | Pruebas de la Tabla 3, validaciones y búsqueda por nombre |
