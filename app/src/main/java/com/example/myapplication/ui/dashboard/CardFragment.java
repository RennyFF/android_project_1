package com.example.myapplication.ui.dashboard;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.room.Room;

import com.example.myapplication.GenerateHistoryDAO;
import com.example.myapplication.GenerateHistoryDB;
import com.example.myapplication.HistoryDAO;
import com.example.myapplication.HistoryDB;
import com.example.myapplication.R;
import com.example.myapplication.ui.GenerateCode;
import com.example.myapplication.ui.home.ScanResult;
import com.google.zxing.BarcodeFormat;

import java.util.ArrayList;

public class CardFragment extends Fragment {
    private ScanResult data;
    private GenerateHistoryDAO mGenerateHistoryDAO;

    private GenerateCode generateCode = new GenerateCode();
    protected ArrayList<BarcodeFormat> QrSimilar = new ArrayList<BarcodeFormat>(4);
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            // Извлекаем объект ScanResult из Bundle
            ScanResult scanResult = bundle.getParcelable("scan_result");
            if (scanResult != null) {
                data = scanResult;
            }
        }
        mGenerateHistoryDAO = Room.databaseBuilder(requireContext(), GenerateHistoryDB.class, "GenerateHistoryDB")
                .build().getGenerateHistoryDAO();
        QrSimilar.add(BarcodeFormat.DATA_MATRIX);
        QrSimilar.add(BarcodeFormat.AZTEC);
        QrSimilar.add(BarcodeFormat.MAXICODE);
        QrSimilar.add(BarcodeFormat.QR_CODE);
        View rootView = inflater.inflate(R.layout.fragment_card, container, false);
        TextView txt = rootView.findViewById(R.id.textcard);
        txt.setText(data.TYPE + " | " + data.RESULT_TEXT);
        BarcodeFormat bf = BarcodeFormat.valueOf(data.TYPE);
        Bitmap bitmap = generateCode.encodeAsBitmap(data.RESULT_TEXT, bf, getHeightByFormat(bf) , 600, requireContext());
        final ImageView myImage = (ImageView) rootView.findViewById(R.id.test_image);
        myImage.setImageBitmap(bitmap);

        ImageView imgDel = rootView.findViewById(R.id.deleteGenerated);
        imgDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mGenerateHistoryDAO.deleteById(data.ID);
                    }
                }).start();
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.navigation_generate);
            }
        });

        TextView textback = rootView.findViewById(R.id.btn_back_mycodes);
        textback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.navigation_generate);
            }
        });
        return rootView;
    }
    private int getHeightByFormat(BarcodeFormat bf){
        return QrSimilar.contains(bf)?600:300;
    }
}