# ProyectoGPS - Aplicación de Monitoreo GPS en Tiempo Real

Aplicación Android que recolecta datos de ubicación (GPS), los almacena en SQLite y los expone mediante un servidor HTTP embebido. 

---

##  Tecnologías Utilizadas

- **Lenguaje:** Java
- **Plataforma:** Android
- **Base de datos:** SQLite
- **Servidor:** NanoHTTPD embebido
- **Diseño:** XML moderno con Material Components

---

##  Funcionalidades

- Recolección automática de coordenadas GPS cada 30 segundos.
- Almacenamiento local en base de datos SQLite.
- Visualización en tabla dentro de la app.
- Servidor HTTP embebido en el dispositivo.
- API para acceder a los datos GPS desde otro dispositivo en la red.

---

## ⚙ Instrucciones

##  Requisitos Previos

- Android Studio (recomendado: versión 2022.3 o superior)
- Android SDK 33 o superior
- Un dispositivo Android físico o emulador con permisos de ubicación
- Conexión a red WiFi (para probar las peticiones remotas)

---

## Instrucciones de Instalación

### 1. Clonar el repositorio

- git clone https://github.com/tuusuario/ProyectoGPS.git
- cd ProyectoGPS

### 2. Abrir y ejecutar en Android Studio
- Abre Android Studio.

- Selecciona Open an existing project.

- Navega hasta la carpeta del proyecto y ábrela.

- Espera a que se sincronicen las dependencias.

> ✅ Cómo ejecutar la aplicación
- Conecta un **dispositivo físico Android** con GPS habilitado.
- Ejecuta el proyecto en el dispositivo (`Run > Run 'app'`).

> **⚠ Importante:** No funciona correctamente en emuladores sin GPS o sin red Wi-Fi.

---

### 3. Iniciar servidor HTTP
- Abre la app en el dispositivo.
- En la pantalla principal, presiona **"Iniciar Servidor GPS"**.
- El servidor se iniciará en el puerto `8080` y navegará automáticamente a la vista de datos GPS.

---

### 4. Obtener tu IP local
- Al precionar **"Iniciar Servidor GPS"**, La IP local del dispositivo se mostrará en la pantalla, y debajo se mostrara el historial de ubicacion almacenado en la basa de datos SQLite

---

### 5. Realizar Peticiones remotas
- Mientras se ejecuta la aplicacion movil, acceder desde un navegador en la misma red local a:
- http://<IP_LOCAL_DEL_DISPOSITIVO_REMOTO>:8080/api/sensor_data
- http://<IP_LOCAL_DEL_DISPOSITIVO_REMOTO>:8080/api/device_status
  


