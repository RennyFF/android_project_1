package com.example.myapplication.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.room.Room;

import com.example.myapplication.GenerateHistory;
import com.example.myapplication.GenerateHistoryDAO;
import com.example.myapplication.GenerateHistoryDB;
import com.example.myapplication.History;
import com.example.myapplication.HistoryDAO;
import com.example.myapplication.HistoryDB;
import com.example.myapplication.R;
import com.google.zxing.BarcodeFormat;

import java.util.List;
import java.util.regex.Pattern;

public class GenerateFragment extends Fragment {
    private BarcodeFormat[] barcodeFormats = { BarcodeFormat.AZTEC, BarcodeFormat.DATA_MATRIX, BarcodeFormat.MAXICODE, BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39,
    BarcodeFormat.CODABAR, BarcodeFormat.CODE_93, BarcodeFormat.CODE_128, BarcodeFormat.EAN_8, BarcodeFormat.EAN_13, BarcodeFormat.ITF, BarcodeFormat.PDF_417,
    BarcodeFormat.RSS_14, BarcodeFormat.RSS_EXPANDED, BarcodeFormat.UPC_A, BarcodeFormat.UPC_E, BarcodeFormat.UPC_EAN_EXTENSION};
    private GenerateHistoryDAO mGenerateHistoryDAO;
    private GenerateHistoryDB mGenerateHistoryDB;
    private String _textToGenerate;
    private BarcodeFormat _typeToGenerate;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.add_newcode, container, false);

        mGenerateHistoryDB = Room.databaseBuilder(requireContext(), GenerateHistoryDB.class, "GenerateHistoryDB").build();
        mGenerateHistoryDAO = mGenerateHistoryDB.getGenerateHistoryDAO();
        EditText inputText = rootView.findViewById(R.id.input_text);

        ImageView generateBtn = rootView.findViewById(R.id.btn_back_generate);
        generateBtn.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.navigation_generate);
        });
        TextView saveCode = rootView.findViewById(R.id.save_new_code);

        Spinner spinner = rootView.findViewById(R.id.generate_type);
        ArrayAdapter<BarcodeFormat> adapter = new ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, barcodeFormats);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                BarcodeFormat item = (BarcodeFormat) parent.getItemAtPosition(position);
                _typeToGenerate = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);
        saveCode.setOnClickListener(v -> {
            Log.i("LP:ASAS", "ASDASDASD");
            _textToGenerate = inputText.getText().toString();
            boolean canDoNext = IsRightTextForFormat(_textToGenerate);
            if(!_textToGenerate.isEmpty() && _typeToGenerate != null) {
                if (((_typeToGenerate != BarcodeFormat.AZTEC && _typeToGenerate != BarcodeFormat.PDF_417 && _typeToGenerate != BarcodeFormat.QR_CODE) && canDoNext) ||
                        (_typeToGenerate == BarcodeFormat.AZTEC || _typeToGenerate == BarcodeFormat.PDF_417 || _typeToGenerate == BarcodeFormat.QR_CODE)) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            GenerateHistory ghis = new GenerateHistory(_textToGenerate, _typeToGenerate.name());
                            mGenerateHistoryDAO.addGenerateHistory(ghis);
                        }
                    }).start();
                    NavController navController = Navigation.findNavController(v);
                    navController.navigate(R.id.navigation_generate);
                }
                else{
                    Toast.makeText(requireContext(), "Данный формат не поддерживает числа или знаки!", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(requireContext(), "Ошибка ввода данных!", Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }
    private boolean IsRightTextForFormat(String text){
        String regex = "\\d+";
        return text.matches(regex);
    }
}
