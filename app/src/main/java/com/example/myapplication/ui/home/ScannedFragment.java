package com.example.myapplication.ui.home;// TermsFragment.java
import static android.content.Context.CLIPBOARD_SERVICE;
import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;
import static androidx.core.content.ContextCompat.getSystemService;
import static com.example.myapplication.ui.home.HomeFragment.ResultTextCODE;
import static com.example.myapplication.ui.home.HomeFragment.aboba;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.myapplication.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.EnumMap;
import java.util.Map;

public class ScannedFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.scanned_layout, container, false);;
        TextView info = rootView.findViewById(R.id.info_scanned);
        TextView text = rootView.findViewById(R.id.result_text);
        ImageView copybtn = rootView.findViewById(R.id.copyresult);
        info.setText(ResultTextCODE.TYPE +" | Отсканированно: " + ResultTextCODE.DATE);
        text.setText(ResultTextCODE.RESULT_TEXT);

        TextView btn_back = rootView.findViewById(R.id.btn_back);
        btn_back.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.navigation_scan);
        });

        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);

        copybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipData myClip = ClipData.newPlainText("scanned_res", ResultTextCODE.RESULT_TEXT);
                assert clipboard != null;
                clipboard.setPrimaryClip(myClip);
                Toast.makeText(getContext(), "Результат скопирован",
                        Toast.LENGTH_SHORT).show();
            }
        });

        //TODO: ПРОВЕРКА НА HEIGHT если формат как barcode то один height, иначе другой(квадрат)

        Bitmap bitmap = encodeAsBitmap(aboba.getText(), aboba.getBarcodeFormat(), 600, 600);
        final ImageView myImage = (ImageView) rootView.findViewById(R.id.test);
        myImage.setImageBitmap(bitmap);

        return rootView;
    }
    Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) {
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
            Toast.makeText(getContext(), iae.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            return null;
        } catch (Exception e) {
            Toast.makeText(getContext(), "Невозможно создать", Toast.LENGTH_SHORT).show();
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
