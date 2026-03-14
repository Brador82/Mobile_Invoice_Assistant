# Mobile Invoice Assistant

## Project Overview
- **App Name**: Mobile Invoice Assistant
- **Package**: com.mobileinvoice.ocr
- **Version**: 1.3.4 (versionCode 134)
- **Installed On**: Pixel 9a (as of March 2026)
- **Source**: Decompiled from Pixel-installed APK (March 2026)
- **Min SDK**: 26 (Android 8.0)
- **Target SDK**: 35 (Android 15)

## Branch Strategy
- `pixel-v1.3.3` — Baseline from Pixel-installed APK (versionCode 133). Do not modify.
- `pixel-v1.3.4` — Current build installed on Pixel 9a (versionCode 134).
- Future branches will be created for Debug/Blue builds (versionCode 135).

## Key Activities
- **MainActivity** — Main entry point / dashboard
- **CameraActivity** — Camera capture (landscape)
- **InvoiceDetailActivity** — Invoice detail view
- **InvoiceLibraryActivity** — Invoice library browser
- **ManualExtractionActivity** — Manual data extraction
- **SignatureActivity** — Signature capture (fullscreen)
- **BroadcastMessageActivity** — SMS broadcast messaging
- **SettingsActivity** — App settings
- **RouteMapActivity** — Route optimization map

## Permissions
- Camera, Internet, Storage, SMS, Location, Contacts, Network State

## Dependencies
- Google ML Kit (Text Recognition)
- CameraX
- Google Maps SDK
- Google Play Services (Auth, Location, Maps)
- Room Database
- Apache POI (Excel/document handling)
- Kotlin Coroutines
- Material Components
