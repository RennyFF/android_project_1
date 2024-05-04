package com.example.myapplication.ui.home;

import static androidx.core.content.ContextCompat.getSystemService;
import static com.example.myapplication.R.drawable;
import static com.example.myapplication.R.id;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.HistoryDAO;
import com.example.myapplication.MainActivity.*;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.room.Room;

import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.example.myapplication.History;
import com.example.myapplication.HistoryDB;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.google.zxing.Result;
import com.example.myapplication.HistoryDAO;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private MediaPlayer success_sound;
    private boolean isFlashOn;
    private boolean isScanning;
    public static ScanResult ResultTextCODE;
    ImageView flashLight;

    public HomeFragment() {
        isFlashOn = false;
    }

    private CodeScanner codeScanner;
    public static Result aboba;
    SharedPreferences settings;
    private HistoryDAO mHistoryDAO;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mHistoryDAO = Room.databaseBuilder(requireContext(), HistoryDB.class, "HistoryDB")
                .build().getHistoryDAO();
        permissionCheck();
        settings = requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);

        success_sound = MediaPlayer.create(requireContext(), R.raw.success_scanned);

        CodeScannerView codeScannerView = root.findViewById(id.scanner_view);
        final Activity activity = getActivity();
        codeScanner = new CodeScanner(activity, codeScannerView);
        codeScanner.setTouchFocusEnabled(true);
        codeScanner.setFormats(CodeScanner.ALL_FORMATS);
        codeScanner.setAutoFocusMode(AutoFocusMode.SAFE);
        codeScanner.setAutoFocusEnabled(true);
        codeScanner.setScanMode(ScanMode.SINGLE);

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        History history = new History(ResultTextCODE.DATE, ResultTextCODE.RESULT_TEXT, ResultTextCODE.TYPE);
                        Vibrator v = (Vibrator) requireContext().getSystemService(requireContext().VIBRATOR_SERVICE);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                mHistoryDAO.addHistory(history);
                            }
                        }).start();
                        if(settings.getBoolean("Vibr_Switch", true)){
                            v.vibrate(150);
                        }
                        if(settings.getBoolean("Sound_Switch", true)){
                            success_sound.start();
                        }
                        ScanResult sr = new ScanResult(result.getBarcodeFormat().toString(), getNow(), result.getText().toString());
                      ResultTextCODE = sr;
                      aboba = result;
                        NavController navController = Navigation.findNavController(requireView());
                        navController.navigate(id.navigation_scanned);
                    }
                });
            }
        });
        flashLight = root.findViewById(id.flashlight);
        flashLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFlashOn = !isFlashOn;
                ChangeTorch();
            }
        });
        return root;
    }

    private String getNow(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy, HH:mm");
        String Date = sdf.format(calendar.getTime());
        return Date;
    }
    private void permissionCheck() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA
            }, 12);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != 12) {
            permissionCheck();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void ChangeTorch() {
        if (isFlashOn) {
            codeScanner.setFlashEnabled(true);
            flashLight.setImageResource(drawable.ic_flashlight_on_24dp);
            flashLight.setBackgroundResource(drawable.custom_rounded_transparent_flashlight_on);
        } else {
            codeScanner.setFlashEnabled(false);
            flashLight.setImageResource(drawable.ic_flashlight_off_24dp);
            flashLight.setBackgroundResource(drawable.custom_rounded_transparent_flashlight_off);
        }
    }

    private void startScanning() {
        try {
            codeScanner.startPreview();
            isScanning = true;
        } catch (Exception e) {
            Toast.makeText(getContext(), "Failed to start scanning", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopScanning() {
        try {
            codeScanner.stopPreview();
            isScanning = false;
        } catch (Exception e) {
            Toast.makeText(getContext(), "Failed to stop scanning", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isScanning) {
            startScanning();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopScanning();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}