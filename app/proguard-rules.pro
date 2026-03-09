# Mobile Invoice Assistant ProGuard Rules

# Keep Room entities and DAOs
-keep class com.mobileinvoice.ocr.database.** { *; }
-keep class com.mobileinvoice.delivery.data.** { *; }

# Keep ONNX Runtime
-keep class ai.onnxruntime.** { *; }

# Keep Apache POI
-dontwarn org.apache.poi.**
-dontwarn org.apache.commons.**
-dontwarn org.apache.logging.**
-keep class org.apache.poi.** { *; }

# Keep ML Kit
-keep class com.google.mlkit.** { *; }
