# Modernization & KMM migration plan

This document records the plan that was followed to modernize the (previously
~2021-era) SensorsManager codebase and migrate its shared logic to Kotlin
Multiplatform so it can run on iOS.

All work was done on the `claude/legacy-modernize-kmm-3659ep` branch and verified
through GitHub Actions (the build environment has no Android SDK, so CI is the
verification harness).

## Phase 1 — Build system modernization ✅

- Gradle 6.5 → 8.7, AGP 4.1.2 → 8.5.2, Kotlin 1.4.30 → 1.9.24, JDK 8 → 17.
- Removed the dead `jcenter()` repository; everything resolves from
  `mavenCentral()` / `google()` (and jitpack for MPAndroidChart).
- Introduced the `gradle/libs.versions.toml` version catalog.
- Declared `namespace`, enabled `buildConfig` + `viewBinding`, added
  `android:exported`, bumped `minSdk` 19 → 21 (required by current dependencies).

## Phase 2 — Deprecated API removal ✅

- Kotlin Android Extensions synthetics → **View Binding** across the activity and
  every fragment.
- `kotlinx.android.parcel.Parcelize` → `kotlinx.parcelize.Parcelize`.
- Legacy `InterstitialAd` → `InterstitialAd.load` / `FullScreenContentCallback`.
- Dropped the removed `kotlinx.coroutines.flow.collect` import.
- Tests moved from the abandoned `nhaarman.mockitokotlin2` to `org.mockito.kotlin`.

## Phase 3 — Refactor / beautify (no functional changes) ✅

- Simplified `SensorController.getSensorInfo` with `buildString`, dropping dead
  commented-out debug lines.

## Phase 4 — Tests ✅

- Added JUnit 5 unit tests for `AdManager` and the `AppUtils` extensions.
- Added AndroidJUnit4 instrumented tests (application id, `PreferencesManager`
  round-trip) and removed the generated placeholder test.

## Phase 5 — CI ✅

- Split the build workflow into parallel jobs (lint, unit + debug build with APK
  artifact, instrumented tests on an emulator) with a cancel-in-progress
  concurrency group.
- Modernized the release-artifact workflow.

## Phase 6 — UI beautify (separate, revertable commits) ✅

- Harmonized the color palette (a proper dark variant of the primary colour,
  softened error red, cooler neutral) in its own commit.

## Phase 7 — Kotlin Multiplatform migration ✅

- Added the `:shared` KMP module (Android + iOS targets) and moved the
  measurement-unit domain, `SensorValues`, and the pure `MeasurementConverter`
  into `commonMain` with a common test.
- The Android `ValuesConverter` delegates its maths to the shared converter.
- Added a SwiftUI `iosApp` consuming the shared framework via XcodeGen, plus a
  macOS CI workflow that builds the framework and the app.

## Verification

- Android: `assembleDebug`, `testDebugUnitTest`, `lintDebug` and the emulator
  `connectedDebugAndroidTest` job all pass on CI.
- iOS: the shared framework and the SwiftUI app build on the macOS CI job. Running
  on a physical iPhone still requires signing configuration.

## Possible follow-ups

- Migrate networking to a multiplatform client (Ktor) and persistence to
  `multiplatform-settings` to share more logic with iOS.
- Replace the manually managed drawer with the Navigation component (an existing
  in-code TODO).
- Add `values-night` resources for dark mode.
