# BloomMind: Diario Emocional Interactivo

Resumen 
-----------------------------------------------
BloomMind es una aplicación Android desarrollada en Kotlin nativo con Jetpack Compose. Nacida como un proyecto de Programación Móvil desarrollado por mi persona, Silvia Chaves, su objetivo es ser un compañero diario para el monitoreo emocional, inspirado en la estética de la serie de juegos *Animal Crossing*.

La aplicación permite a los usuarios registrar cómo se sienten a través de un *check-in* interactivo, iniciar una conversación con una IA (potenciada por Gemini) para hablar sobre sus emociones, y recibir contenido diario como una afirmación y un consejo para fomentar el bienestar. Además, provee acceso rápido a líneas de ayuda y emergencia de Costa Rica.

Arquitectura y Tech Stack
-------------------------------------------------
- **UI**: 100% Jetpack Compose, siguiendo los lineamientos de Material 3.
- **Patrón**: MVVM (ViewModel) con principios de Clean Architecture.
  - **View (Screens/Composables)**: Presenta la UI y reacciona a los cambios de estado.
  - **ViewModel**: Mantiene y expone el estado de la UI (`StateFlow`) y coordina las llamadas a los repositorios.
  - **Repository**: Abstrae el acceso a las fuentes de datos (APIs externas y caché local).
- **Asincronía**: Coroutines y `StateFlow` para un manejo de datos reactivo y eficiente.
- **Red**: Retrofit para el consumo de APIs REST, con un interceptor de OkHttp para el logging.
- **Persistencia Local**: DataStore para guardar preferencias, perfil de usuario y estado de la app.
- **Navegación**: Navigation Compose para gestionar las rutas y transiciones entre pantallas.
- **Dependencias**: Gestionadas centralmente en `gradle/libs.versions.toml` (Compose BOM, Retrofit, Gson, DataStore, etc.).

### APIs Externas
El proyecto consume tres servicios externos:
1.  **Google Gemini**: Potencia el chat interactivo.
2.  **Buddha API**: Provee las afirmaciones diarias.
3.  **API-Ninjas**: Provee los consejos diarios.

Instrucciones para Ejecutar
----------------------------------------------------------
1.  **Clonar el Repositorio:**
    -   `git clone https://github.com/sweetsylve0n/BloomMind.git`
    -   En Android Studio: `File` → `Open` → seleccione la carpeta `BloomMind`.

2.  **Sincronizar el Proyecto:**
    -   Espera a que Android Studio sincronice el proyecto y descargue todas las dependencias. Si solicita instalar algún SDK o Build Tools, acepta.

3.  **Configuración de API Keys (Indispensable):**
    -   Sin las claves de API, las funcionalidades de chat, afirmaciones y consejos no funcionarán. Para añadirlas, sigue estos pasos:

    -   **Crea el archivo `secret.properties`:** En la **raíz de tu proyecto** (al mismo nivel que `build.gradle.kts` y `settings.gradle.kts`), crea un nuevo archivo llamado `secret.properties`.

    -   **Añade las claves al archivo:** Abre el archivo y pega las siguientes líneas, reemplazando `"TU_API_KEY_AQUI"` con tus claves reales:
        ```properties
        GEMINI_API_KEY="TU_API_KEY_AQUI"
        NINJA_API_KEY="TU_API_KEY_AQUI"
        ```

    -   **¿Cómo obtener las claves?**
        -   **Gemini Key**: Ve a [Google AI Studio](https://aistudio.google.com/app/apikey), inicia sesión con tu cuenta de Google y crea una nueva clave de API de forma gratuita.
        -   **Ninja Key**: Ve a [API-Ninjas](https://api-ninjas.com/profile), regístrate gratis y copia la clave de API que aparece en tu perfil.

4.  **Ejecutar la App:**
    -   Selecciona el módulo `app` y ejecuta (Run) en un emulador o dispositivo físico.

Estado y Funcionalidades Implementadas (85%)
-------------------------------------------------
-   **Onboarding y Perfil (CRUD Completo):** Flujo completo para crear un perfil de usuario (nombre, fecha de nacimiento, género, ícono), que se guarda localmente usando DataStore. El perfil es editable y se puede eliminar.

-   **Home Screen Dinámico:**
    -   Saluda al usuario según la hora del día.
    -   Muestra una **tarjeta de emociones** que se actualiza al instante después de cada check-in.
    -   Implementa **carga proactiva**: la afirmación y el consejo del día se descargan en segundo plano al entrar al Home, mostrando animaciones de carga y permitiendo una navegación instantánea al tocar las tarjetas.

-   **Check-in Emocional:**
    -   Flujo de selección de hasta 4 emociones, agrupadas por categorías (Mal, Okay, Bien).
    -   Permite realizar el check-in **múltiples veces al día**, sobrescribiendo el anterior para reflejar el estado emocional más reciente.

-   **Chat con IA (Gemini):**
    -   Interfaz de chat completamente funcional.
    -   **Inicio Contextual:** Si se navega desde el check-in, la IA inicia la conversación de forma proactiva basándose en las emociones seleccionadas.
    -   **Límite de Mensajes Diario:** El límite de 25 mensajes se reinicia cada día a las 12:00 AM, no cada 24 horas.

-   **Contenido Diario Automatizado:**
    -   La afirmación, el consejo y el límite del chat se **actualizan con el cambio de día natural**, asegurando una experiencia fresca cada mañana.

-   **Navegación Fluida:**
    -   Se han implementado **animaciones de transición** (slide) en todo el `NavHost` para que el cambio entre pantallas se sienta suave y profesional.
    -   La lógica de la `BottomNavigationBar` ha sido robustecida para evitar errores de navegación y asegurar un comportamiento predecible.

-   **Líneas de Ayuda de Costa Rica:** Carrusel informativo con números de emergencia y horarios.