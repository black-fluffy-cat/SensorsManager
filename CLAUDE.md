# CLAUDE.md

Guidance for working in this repository.

## What this project is

SensorsManager reads data from a phone's hardware sensors (accelerometer,
gyroscope, light, linear acceleration, magnetic field, proximity, rotation
vector, heart rate) and plots the readings on live charts. It started as an
Android-only app and now shares its platform-agnostic domain logic through a
Kotlin Multiplatform module so it can also run on iOS.

## Modules

| Module     | Type                              | Purpose |
|------------|-----------------------------------|---------|
| `:app`     | Android application               | The Android UI, sensors, services, networking, ads, analytics. |
| `:shared`  | Kotlin Multiplatform library      | Platform-agnostic domain: measurement units, `MeasurementConverter`, `SensorValues`. Targets Android + `iosX64`/`iosArm64`/`iosSimulatorArm64`. |
| `iosApp`   | SwiftUI app (XcodeGen)            | Thin iOS shell that consumes the `shared` framework. |

Domain code shared between platforms lives in `:shared` under
`com.fluffycat.sensorsmanager.values` / `.sensors`. The Android
`ValuesConverter` maps Android `Sensor` types and the stored unit preferences
onto the shared `MeasurementConverter`.

## Toolchain

- Gradle 8.7 (wrapper), Android Gradle Plugin 8.5.2, Kotlin 1.9.24
- JDK 17
- `minSdk` 21, `compileSdk`/`targetSdk` 34
- Dependencies are declared in the version catalog: `gradle/libs.versions.toml`

## Common commands

```bash
# Android
./gradlew assembleDebug              # build the debug APK
./gradlew testDebugUnitTest          # JVM unit tests (JUnit 5)
./gradlew lintDebug                  # Android lint
./gradlew connectedDebugAndroidTest  # instrumented tests (needs a device/emulator)

# Shared / iOS (iOS link tasks require macOS)
./gradlew :shared:testDebugUnitTest
./gradlew :shared:linkDebugFrameworkIosSimulatorArm64
cd iosApp && xcodegen generate       # generate iosApp.xcodeproj
```

## Architecture notes

- **DI:** Koin (`koin/smMainModule.kt`, started in `SensorsManagerApplication`).
- **UI:** AppCompat activity with a navigation drawer; fragments use **View Binding**
  (the old Kotlin Android Extensions synthetics were removed). `BaseChartFragment`
  is subclassed for the per-sensor screens.
- **Charts:** MPAndroidChart.
- **Networking:** Retrofit + Jackson (`rest/`), used by the optional
  `CollectingDataService`.
- **Ads:** Google Mobile Ads (banner + interstitial via `InterstitialAd.load`).

## CI

- `.github/workflows/androidGradleBuild.yml` — three jobs: unit tests + debug
  build (uploads the APK), Android lint, and instrumented tests on an emulator.
  Runs on `develop`, `claude/**` and PRs to `develop`.
- `.github/workflows/iosBuild.yml` — macOS job that builds the shared iOS
  framework and the iOS app (via XcodeGen). Path-filtered to shared/iOS changes.
- `.github/workflows/createDebugApk.yml` — builds and attaches a debug APK to a
  GitHub Release when a `*d` tag is pushed.
- `.github/workflows/createReleaseArtifacts.yml` — builds a signed release AAB
  (the format Google Play requires) and a signed release APK, and attaches both
  to a GitHub Release when a tag is pushed. Signing keystore and passwords come
  from the `KEYSTORE_BASE64`, `KEYSTORE_PASSWORD`, `KEY_ALIAS` and `KEY_PASSWORD`
  repository secrets.

## Conventions

- Commit subjects are imperative, sentence case, no type prefixes
  (e.g. "Modernize Gradle, Android Gradle Plugin and dependencies").
- Keep behaviour-preserving refactors free of functional changes; put UI styling
  tweaks in their own commits so they can be reverted independently.

## Gotchas

- A full Android build needs the Android SDK (and sensors/ads need a real device);
  CI is the source of truth for build/test verification.
- `SettingsFragment` only wires up its debug controls and does not inflate a layout
  through `onCreateView`; treat it as debug-only behaviour.
- iOS link/build tasks only run on macOS; Linux configures the targets but cannot
  assemble them.
