# Usagi Tienda – App Android

Proyecto de tienda en Android, hecho con Kotlin y Jetpack Compose. Incluye autenticación básica, catálogo con filtros, carrito, aplicación de cupones, checkout simulado y una sección de fotos remotas (JSONPlaceholder) para probar integración con Retrofit y Coil.

## Características
- Autenticación: login y registro con validación sencilla.
- Catálogo: filtros por categoría (Todos, Chiikawa, Peluches, Llaveros, Accesorios).
- Carrito: conteo, total y limpieza.
- Cupones: aplicación de códigos y cálculo de descuento.
- Checkout: flujo de pago simulado con confirmación.
- Fotos remotas: consumo de `/photos` de JSONPlaceholder con Retrofit + Coil.
- Persistencia: Room con KSP (DAOs, entidades y esquemas generados).
- UI: Material 3, navegación declarativa con `NavHost`.

## Pantallas y rutas
- login y register
- home
- `catalog
- detail/{id}
- cart
- coupon_scan
- checkout
- photos

## Stack técnico
- Kotlin, Coroutines
- Jetpack Compose + Material 3 + Navigation
- Room (KSP)
- Retrofit + Moshi + OkHttp
- Coil (carga de imágenes)
- R8/ProGuard para optimización

## Estructura del proyecto
- app/src/main/java/com/example/usagi_tienda_app/ código fuente (UI, datos, dominio)
- app/proguard-rules.pro reglas de R8/ProGuard
- app/keystore/ instrucciones y placeholders de firma
- app/schemas/ esquemas de Room generados automáticamente
- preview/index.html resumen del proyecto (informativo)
- gradle/libs.versions.toml version catalog
- lint.xml reglas y severidades de Lint

## Compilación (CLI)
- Compilar debug:
```bash
.\gradlew assembleDebug
```
- Instalar en dispositivo conectado:
```bash
.\gradlew installDebug
```
- Ejecutar pruebas unitarias:
```bash
.\gradlew test
```
- Ejecutar Lint:
```bash
.\gradlew lint
```

## Build de release
- Generar APK de release:
bash
.\gradlew assembleRelease

- Para firmar, los pasos estan en `app/keystore/README.md`. la configuracion de la firma es de release mediante propiedades en `gradle.properties` y variables de entorno. Si no hay keystore, el build de release usa placeholders seguros.

## Notas importantes
- El checkout es una simulación (no procesa pagos reales).
- El escaneo de cupones está pendiente de implementación; aplica un cupón de ejemplo desde la vista para demostrar el flujo.
- La sección de fotos usa datos públicos de JSONPlaceholder.
