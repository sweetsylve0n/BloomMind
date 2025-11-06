Resumen 
-----------------------------------------------
BloomMind es una aplicación Android escrita en Kotlin (Jetpack Compose) desarrollada como proyecto de Programación Móvil, por mi persona, Silvia Chaves Mata. Mi intención es desarrollar un app de monitoreo emocional diario interactivo, con un chat integrado y además, información sobre los números de emergencia disponibles en Costa Rica para cualquier persona que se sienta en peligro y no sepa o tenga a quien llamar, estoy a medio camino todavía, pero muy motivada. En esta guía encontrará:
- Una explicación general de la arquitectura seguida.
- Qué funcionalidades están preparadas/implementadas.
- Instrucciones paso a paso para ejecutar la app en su equipo y cómo configurar la API Key (para la funcionalidad de chat).
  
Arquitectura general
-------------------------------------------------
- UI: Jetpack Compose.
- Patrón recomendado: MVVM/Clean Architecture.
  - View (Screens/Compose): presenta la UI.
  - ViewModel: mantiene el estado y coordina llamadas a repositorios.
  - Repository: abstrae acceso a datos (API, caché).
- Red: Retrofit + OkHttp y Logging Interceptor.
- Persistencia ligera de datos: DataStore (local).
- Navegación: Navigation Compose (rutas/pantallas).
- Dependencias gestionadas en `gradle/libs.versions.toml` (Compose BOM, Retrofit, Gson, DataStore, lifecycle, etc.).

Rol de cada capa (principales)
- UI: recibe estados del ViewModel.
- ViewModel: expone Flows/StateFlows o LiveData con estados; dirige envíos/recepciones de mensajes.
- Repositorio/Network: realiza llamadas a la API externa (Gemini por el momento).
- DataStore: guarda preferencias y configuraciones.

Instrucciones para ejecutar
----------------------------------------------------------
1. Clone el repositorio (o descomprima la carpeta) y abra Android Studio:
   - git clone https://github.com/sweetsylve0n/BloomMind.git
   - En Android Studio: File → Open → seleccione la carpeta `BloomMind`.

2. Espere a que Android Studio sincronice el proyecto y descargue dependencias. Si solicita instalar algún SDK/Build Tools, acepte.

3. Colocar la API Key (explicación abajo). Sin la clave, la funcionalidad de chat que usa la API externa puede no funcionar, asi como el API para las afirmaciones.
   
   - Para poder demostrar la funcionalidad de chat en la entrega, por favor añada su `secret.properties` con las API's Keys antes de ejecutar la app.
   - Hacer las modificaciones pertinentes (GeminiApiInterface URL,...)
     
4. Seleccione el módulo `app` y ejecute (Run) en un emulador o dispositivo físico.

Conteo general del avance 50%:
-------------------------------------------------
-Paleta de colores personalizada, asi como iconos, y elementos de Material3 a lo largo la navegación.
-Chat: interfaz funcional de chat que envía mensajes al servicio de IA y muestra las respuestas en la UI con Jetpack Compose, manejo de limite diario (25 solicitudes).
-Perfil (CRUD): lógica completa para crear, leer y actualizar el perfil de usuario (persistencia local mediante DataStore / repositorio). Me falta implementar la opción de eliminar la cuenta.
-Home: pantalla/flujo principal en implementación.
-Check-in: en implementación.

Estado general: arquitectura MVVM (Compose + ViewModel + Repositorio), llamadas de red encapsuladas en data/retrofit, y almacenamiento de preferencias en data/datastore.
   
