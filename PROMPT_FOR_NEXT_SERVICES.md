# Prompt para Generar Servicios de Autenticación y Streaming

Copia y pega el siguiente texto en tu próxima interacción para generar los microservicios restantes con la arquitectura correcta.

---

**ROL:** Eres un arquitecto de software experto en Spring Boot, Microservicios y Streaming de Audio.

**CONTEXTO ACTUAL:**
Ya tengo un microservicio desarrollado llamado **`catalog-music-service`** (Puerto 8080) que:
1.  Usa PostgreSQL y MinIO.
2.  Escanea música, guarda metadatos y devuelve el `filePath` (Key de MinIO) de las canciones.
3.  Tiene endpoints separados `/api/public` y `/api/admin`.
4.  Infraestructura compartida: Postgres (5432) y MinIO (9000).

**OBJETIVO:**
Necesito que generes el código y estructura para dos nuevos microservicios independientes.

---

### SERVICIO 1: `auth-service` (Seguridad)
**Puerto**: 8081
**Tecnología**: Spring Boot, Spring Security, PostgreSQL, JWT (JJWT library).
**Responsabilidad**:
1.  Gestionar Usuarios (`User` entity) con Roles (`USER`, `ADMIN`).
2.  Endpoints:
    *   `POST /auth/register`: Registro de usuarios.
    *   `POST /auth/login`: Devuelve un JWT firmado.
    *   `POST /auth/validate`: (Opcional) Para validar tokens.
3.  **Importante**: Debe generar tokens firmados con una clave secreta robusta que luego pueda ser usada por el `catalog-music-service` (como Resource Server) para validar acceso a `/api/admin`.

---

### SERVICIO 2: `streaming-music-service` (Audio Player)
**Puerto**: 8082
**Tecnología**: Spring Boot **WebFlux** (Reactive Stack) para alto rendimiento, MinIO Java SDK.
**Responsabilidad**:
1.  Recibir peticiones de streaming de audio.
2.  Conectarse a MinIO usando el `filePath` provisto.
3.  Hacer streaming reactivo de los bytes hacia el cliente.
**Requerimientos Clave**:
1.  **Soporte HTTP Range Requests**: Es obligatorio permitir `seek` (adelantar/atrasar) en el reproductor del cliente.
2.  **Métricas**: Usar Micrometer/Actuator para registrar:
    *   `music.stream.started`: Contador de canciones iniciadas (con etiquetas `songId`, `userId`).
    *   `music.bytes.streamed`: Contador de tráfico de datos.
3.  **Endpoint**:
    *   `GET /stream?key={minioFileKey}&songId={id}`
    *   Este endpoint NO debe exponer la URL de MinIO, debe actuar como Proxy Reactivo.

---

**INSTRUCCIONES DE ENTREGA:**
1.  Genera la estructura de carpetas y archivos `pom.xml` necesarios para ambos servicios.
2.  Proporciona el código de las clases principales (`Controller`, `Service`, `Config`).
3.  Asegúrate de que compartan la red de Docker creada anteriormente (`music_net`).
