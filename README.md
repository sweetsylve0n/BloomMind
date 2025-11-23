# BloomMind: Diario Emocional Interactivo

Resumen
-----------------------------------------------
BloomMind es una aplicación Android desarrollada en Kotlin nativo con Jetpack Compose. Creada como proyecto de Programación Móvil por mi persona, Silvia Chaves, su objetivo es ser un compañero digital para registrar y acompañar las emociones del usuario mediante un check-in interactivo, contenido diario (afirmaciones y consejos) y un chat asistido por IA (potenciado por Gemini).

La aplicación permite a los usuarios:
- Registrar cómo se sienten a través de un check-in emocional.
- Iniciar una conversación con una IA contextualizada según el check-in.
- Recibir una afirmación y un consejo diario.
- Guardar perfil y preferencias localmente.

Arquitectura y Tech Stack
-------------------------------------------------
- UI: 100% Jetpack Compose, siguiendo los lineamientos de Material 3.
- Patrón: MVVM (ViewModel) con principios de Clean Architecture.
  - View (Screens/Composables): Presenta la UI y reacciona a los cambios de estado.
  - ViewModel: Mantiene y expone el estado de la UI (`StateFlow`) y coordina las llamadas a los repositorios.
  - Repository: Abstrae el acceso a las fuentes de datos (APIs externas y caché local).
- Asincronía: Coroutines y `StateFlow` para un manejo de datos reactivo y eficiente.
- Red: Retrofit para el consumo de APIs REST, con interceptores de OkHttp para logging y manejo de headers.
- Persistencia Local: DataStore para guardar preferencias, perfil de usuario y estado de la app.
- Navegación: Navigation Compose para gestionar rutas y transiciones entre pantallas.
- Dependencias: Gestionadas centralmente en `gradle/libs.versions.toml` (Compose BOM, Retrofit, Gson/Moshi, DataStore, etc.).

APIs Externas
-------------------------------------------------
El proyecto consume los siguientes servicios externos:
1. Google Gemini: Potencia el chat interactivo.
2. Buddha API: Provee afirmaciones diarias.
3. API-Ninjas: Provee consejos diarios.

Instrucciones para Ejecutar
----------------------------------------------------------
1. Clonar el repositorio:
   - `git clone https://github.com/sweetsylve0n/BloomMind.git`
   - En Android Studio: `File` → `Open` → seleccione la carpeta `BloomMind`.

2. Sincronizar el Proyecto:
   - Espera a que Android Studio sincronice el proyecto y descargue todas las dependencias. Si solicita instalar algún SDK o Build Tools, acepta.

3. Configuración de API Keys (Indispensable):
   - Sin las claves de API, las funcionalidades de chat, afirmaciones y consejos no funcionarán. Para añadirlas, sigue estos pasos:

   - Crea el archivo `secret.properties` en la raíz del proyecto (al mismo nivel que `build.gradle.kts` y `settings.gradle.kts`).

   - Añade las claves al archivo `secret.properties` (reemplaza `"TU_API_KEY_AQUI"` con tus claves reales):
     ```properties
     GEMINI_API_KEY="TU_API_KEY_AQUI"
     NINJA_API_KEY="TU_API_KEY_AQUI"
     ```

   - ¿Cómo obtener las claves?
     - Gemini Key: Ve a Google AI Studio (https://aistudio.google.com/app/apikey), inicia sesión y crea una clave de API.
     - Ninja Key: Ve a API-Ninjas (https://api-ninjas.com/profile), regístrate y copia la clave de tu perfil.
     - Buddha API: Regístrate o consulta el servicio de Buddha en su sitio web para acceder a la documentación.

   - Nota: `secret.properties` está incluido en `.gitignore` para evitar subir claves al repositorio.

4. Ejecutar la App:
   - Selecciona el módulo `app` y ejecuta (Run) en un emulador o dispositivo físico.

Estado y Funcionalidad Implementada
-------------------------------------------------

- Onboarding y Perfil (CRUD Completo):
  - Flujo completo para crear y editar un perfil de usuario (nombre, fecha de nacimiento, género, ícono).
  - El perfil se guarda localmente usando DataStore; permite editar y persistir preferencias.

- Home Screen Dinámico:
  - Saludo personalizado según la hora del día.
  - Tarjeta de emociones que se actualiza al instante después de cada check-in.
  - Carga proactiva: la afirmación y el consejo del día se descargan en segundo plano al entrar al Home, mostrando animaciones de carga y permitiendo navegación instantánea cuando los datos están listos.
  - Carrusel informativo con líneas de ayuda de Costa Rica (números de emergencia y horarios).

- Check-in Emocional:
  - Selección de hasta 4 emociones, agrupadas por categorías (Mal, Okay, Bien).
  - Permite realizar check-in múltiples veces al día; el último check-in sobrescribe el anterior para reflejar el estado emocional más reciente.
  - Al realizar un check-in, se actualiza el contexto para el chat con IA si el usuario lo desea.
  - Permite el acceso desde el Home un desglose descriptivo de las emociones recientes, así como una estadística de los últimos 5 registros realizados divididos por categorías, con información de día, hora y cantidad de emociones de cada registro almacenado. 

- Chat con IA (Gemini):
  - Interfaz de chat funcional con envío y recepción de mensajes.
  - Inicio contextual: si se navega desde el check-in, la IA inicia la conversación basándose en las emociones seleccionadas.
  - Límite de mensajes diario: 25 mensajes por día; este límite se reinicia cada día a las 00:00 del día local.

- Contenido Diario Automatizado:
  - Afirmación del día y consejo del día descargados automáticamente.
  - El contenido (afirmación, consejo y límite de chat) se actualiza con el cambio de día natural para ofrecer contenido fresco cada mañana.

- Navegación y Experiencia:
  - Animaciones de transición (slide) implementadas en todo el `NavHost`.
  - BottomNavigationBar con lógica robusta para evitar errores de navegación y asegurar comportamiento predecible.

Notas finales
-------------------------------------------------
- Asegúrate de no subir `secret.properties` ni ninguna clave a tu propio repositorio público.
