# Proyecto simple de notificación Diseño y Arquitectura de Software
En el presente respositorio hay 2 proyectos Maven:
1. Queue-Manager: Gestor de notificaciones al que se suscribe el Client.
2. Queue-Client: Client que al iniciar pide un puerto que deberá ser único y que pedirá al Manager ser tomado en cuenta para futuras notificaciones.

Para probarlo debes correr una instancia del Queue-Manager en el puerto 8080 (para cambiarlo deberías cambiarlo también en el Queue-Client).
Y las intancias que consideres de Queue-Client, pero una vez esté corriendo el Queue-Manager.