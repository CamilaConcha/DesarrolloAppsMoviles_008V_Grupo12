Guía interna de firma de aplicación (release)

Generar `release.jks` (Windows PowerShell):

1. Carpeta: `app/keystore/`
2. Comando (ajusta alias y contraseñas a tu preferencia):

```
keytool -genkeypair -v \
  -keystore app/keystore/release.jks \
  -storetype JKS \
  -storepass ******** \
  -keyalg RSA -keysize 2048 -validity 3650 \
  -alias key0 \
  -keypass ******** \
  -dname "CN=Usagi, OU=Mobile, O=Usagi, L=Santiago, S=RM, C=CL"
```

Propiedades (archivo `gradle.properties`):

```
RELEASE_STORE_FILE=keystore/release.jks
RELEASE_STORE_PASSWORD=********
RELEASE_KEY_ALIAS=key0
RELEASE_KEY_PASSWORD=********
```

Builds:

- APK firmado: `./gradlew.bat assembleRelease` → `app/build/outputs/apk/release/`
- AAB: `./gradlew.bat bundleRelease` → `app/build/outputs/bundle/release/`

Verificación (opcional):

```
"%ANDROID_HOME%/build-tools/<version>/apksigner" verify --print-certs app/build/outputs/apk/release/*.apk
```

Notas:
- Resguardar el keystore y contraseñas en un lugar seguro fuera del repositorio.
- Evitar sincronizar el keystore en ubicaciones con bloqueo de archivos.
