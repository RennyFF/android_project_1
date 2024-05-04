package com.example.myapplication.ui.home;// TermsFragment.java
import static android.content.Context.CLIPBOARD_SERVICE;
import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;
import static androidx.core.content.ContextCompat.getSystemService;
import static com.example.myapplication.ui.home.HomeFragment.ResultTextCODE;
import static com.example.myapplication.ui.home.HomeFragment.aboba;

import static java.lang.System.in;

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
import androidx.room.Room;

import com.example.myapplication.History;
import com.example.myapplication.HistoryDAO;
import com.example.myapplication.HistoryDB;
import com.example.myapplication.R;
import com.example.myapplication.ui.GenerateCode;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ScannedFragment extends Fragment {
    protected ArrayList<BarcodeFormat> QrSimilar = new ArrayList<BarcodeFormat>(4);
    private GenerateCode generateCode = new GenerateCode();
    private HistoryDAO mHistoryDAO;
    private HistoryDB mHistoryDB;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        QrSimilar.add(BarcodeFormat.DATA_MATRIX);
        QrSimilar.add(BarcodeFormat.AZTEC);
        QrSimilar.add(BarcodeFormat.MAXICODE);
        QrSimilar.add(BarcodeFormat.QR_CODE);

        mHistoryDB = Room.databaseBuilder(requireContext(), HistoryDB.class, "HistoryDB").build();
        mHistoryDAO = mHistoryDB.getHistoryDAO();

        View rootView = inflater.inflate(R.layout.scanned_layout, container, false);;
        TextView info = rootView.findViewById(R.id.info_scanned);
        TextView text = rootView.findViewById(R.id.result_text);
        ImageView copybtn = rootView.findViewById(R.id.copyresult);
        ImageView deletebtn = rootView.findViewById(R.id.deleteresult);
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

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteLastHistoryEntry();
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.navigation_scan);
            }
        });

        Bitmap bitmap = generateCode.encodeAsBitmap(aboba.getText(), aboba.getBarcodeFormat(), getHeightByFormat(aboba.getBarcodeFormat()) , 600, getContext());
        final ImageView myImage = (ImageView) rootView.findViewById(R.id.test);
        myImage.setImageBitmap(bitmap);

        return rootView;
    }

    private int getHeightByFormat(BarcodeFormat bf){
        return QrSimilar.contains(bf)?600:300;
    }

    private void deleteLastHistoryEntry() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                History lastEntry = mHistoryDAO.getLastEntry();
                if (lastEntry != null) {
                    mHistoryDAO.deleteHistory(lastEntry);
                }
            }
        }).start();
    }

}
