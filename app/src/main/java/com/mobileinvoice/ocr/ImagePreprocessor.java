package com.mobileinvoice.ocr;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/* loaded from: classes7.dex */
public class ImagePreprocessor {
    private static final String TAG = "ImagePreprocessor";
    /** Max dimension for OCR input — larger images just waste time without accuracy gain. */
    private static final int MAX_OCR_DIMENSION = 2000;

    /**
     * Full preprocessing pipeline for OCR:
     * 1. Rotate landscape images to portrait
     * 2. Downscale oversized images
     * 3. Convert to grayscale (removes color noise)
     * 4. Sharpen (crisps up text edges)
     * 5. Gentle contrast boost (makes faded text readable without washing out dark text)
     */
    public static Bitmap pipeline(Bitmap input) {
        Bitmap result = rotateIfLandscape(input);
        result = downscaleIfNeeded(result, MAX_OCR_DIMENSION);
        result = toGrayscale(result);
        result = sharpen(result);
        result = enhanceContrast(result, 1.4f);
        return result;
    }

    public static Bitmap rotateIfLandscape(Bitmap src) {
        if (src.getWidth() > src.getHeight()) {
            Matrix matrix = new Matrix();
            matrix.postRotate(-90.0f);
            Bitmap rotated = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
            return rotated;
        }
        return src;
    }

    /**
     * Downscale so the longest edge is at most maxDimension pixels.
     * ML Kit works best around 1500-2000px; giant photos just slow it down.
     */
    public static Bitmap downscaleIfNeeded(Bitmap src, int maxDimension) {
        int w = src.getWidth();
        int h = src.getHeight();
        int longest = Math.max(w, h);
        if (longest <= maxDimension) {
            return src;
        }
        float scale = (float) maxDimension / longest;
        int newW = Math.round(w * scale);
        int newH = Math.round(h * scale);
        Log.d(TAG, "Downscaling " + w + "x" + h + " -> " + newW + "x" + newH);
        return Bitmap.createScaledBitmap(src, newW, newH, true);
    }

    public static Bitmap toGrayscale(Bitmap src) {
        Bitmap grayscale = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(grayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0.0f);
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(src, 0.0f, 0.0f, paint);
        return grayscale;
    }

    /**
     * Unsharp-mask style sharpening via a 3x3 convolution kernel.
     * Since Android doesn't have a built-in convolution filter, we approximate
     * by blending the original with a contrast-enhanced version weighted toward edges.
     */
    public static Bitmap sharpen(Bitmap src) {
        int width = src.getWidth();
        int height = src.getHeight();
        int[] pixels = new int[width * height];
        int[] output = new int[width * height];
        src.getPixels(pixels, 0, width, 0, 0, width, height);

        // 3x3 sharpen kernel: center=9, neighbors=-1
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int idx = y * width + x;
                int r = 0, g = 0, b = 0;
                for (int ky = -1; ky <= 1; ky++) {
                    for (int kx = -1; kx <= 1; kx++) {
                        int pixel = pixels[(y + ky) * width + (x + kx)];
                        int pr = (pixel >> 16) & 0xFF;
                        int pg = (pixel >> 8) & 0xFF;
                        int pb = pixel & 0xFF;
                        if (kx == 0 && ky == 0) {
                            r += pr * 9;
                            g += pg * 9;
                            b += pb * 9;
                        } else {
                            r -= pr;
                            g -= pg;
                            b -= pb;
                        }
                    }
                }
                // Blend 60% sharpened + 40% original to avoid over-sharpening
                int origPixel = pixels[idx];
                int or = (origPixel >> 16) & 0xFF;
                int og = (origPixel >> 8) & 0xFF;
                int ob = origPixel & 0xFF;
                r = clamp((int)(r * 0.6f + or * 0.4f));
                g = clamp((int)(g * 0.6f + og * 0.4f));
                b = clamp((int)(b * 0.6f + ob * 0.4f));
                output[idx] = 0xFF000000 | (r << 16) | (g << 8) | b;
            }
        }
        // Copy edge pixels as-is
        for (int x = 0; x < width; x++) {
            output[x] = pixels[x];
            output[(height - 1) * width + x] = pixels[(height - 1) * width + x];
        }
        for (int y = 0; y < height; y++) {
            output[y * width] = pixels[y * width];
            output[y * width + width - 1] = pixels[y * width + width - 1];
        }

        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        result.setPixels(output, 0, width, 0, 0, width, height);
        return result;
    }

    private static int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }

    public static Bitmap enhanceContrast(Bitmap src, float factor) {
        float translate = (((-0.5f) * factor) + 0.5f) * 255.0f;
        ColorMatrix cm = new ColorMatrix(new float[]{factor, 0.0f, 0.0f, 0.0f, translate, 0.0f, factor, 0.0f, 0.0f, translate, 0.0f, 0.0f, factor, 0.0f, translate, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f});
        Bitmap result = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(src, 0.0f, 0.0f, paint);
        return result;
    }

    public static String savePreprocessed(Context context, Bitmap bitmap) {
        File dir = new File(context.getFilesDir(), "preprocessed");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String filename = "preprocessed_" + new SimpleDateFormat("yyyyMMdd_HHmmss_SSS", Locale.US).format(new Date()) + ".jpg";
        File file = new File(dir, filename);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 92, fos);
                fos.flush();
                String absolutePath = file.getAbsolutePath();
                fos.close();
                return absolutePath;
            } finally {
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
