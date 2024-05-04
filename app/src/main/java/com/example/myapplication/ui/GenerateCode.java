package com.example.myapplication.ui;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import java.util.EnumMap;
import java.util.Map;

public class GenerateCode {
    public Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_height, int img_width, Context context) {
        String contentsToEncode = contents;
        if (contentsToEncode == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);

        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            Log.d("XXX4", "Не поддерживаемый формат -" + iae);
            Toast.makeText(context, iae.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            return null;
        } catch (Exception e) {
            Toast.makeText(context, "Невозможно создать", Toast.LENGTH_SHORT).show();
            return null;
        }

        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];


        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        int img_widths = 400;
        int img_heights = 400;

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

        if (format == BarcodeFormat.DATA_MATRIX) {
            bitmap = drawScaledBitmap(bitmap, img_widths, img_heights);
        }
        return bitmap;

    }

    public Bitmap drawScaledBitmap(Bitmap bitmap, int img_widths, int img_heights) {
        final int bmWidth = bitmap.getWidth();
        final int bmHeight = bitmap.getHeight();
        final int wScalingFactor = img_widths / bmWidth;
        final int hScalingFactor = img_heights / bmHeight;
        final int scalingFactor = Math.min(wScalingFactor, hScalingFactor);
        return scalingFactor > 1 ? Bitmap.createScaledBitmap(bitmap, bmWidth * scalingFactor, bmHeight * scalingFactor, false) : bitmap;
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }
}
