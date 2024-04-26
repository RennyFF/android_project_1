package com.example.myapplication.ui.home;

import static com.example.myapplication.R.*;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.google.zxing.Result;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private boolean isFlashOn;
    private boolean isScanning;
    ImageView flashLight;

    public HomeFragment() {
        isFlashOn = false;
    }
    private CodeScanner codeScanner;
    private TextView aboba;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        permissionCheck();
        CodeScannerView codeScannerView = root.findViewById(id.scanner_view);
        aboba = root.findViewById(id.aboba);
        codeScanner = new CodeScanner(getContext(), codeScannerView);
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                new Thread() {
                    public void run() {
                            aboba.setText(result.getText().toString());
                        }
                    };
                }
        });
        flashLight = root.findViewById(id.flashlight);
        startScanning();
        flashLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeTorch();
            }
        });
        return root;
    }

    private void permissionCheck() {
        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(),new String []{
                    Manifest.permission.CAMERA
            }, 12);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode!=12){
            permissionCheck();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void ChangeTorch(){
        if(isFlashOn){
            codeScanner.setFlashEnabled(true);
            flashLight.setImageResource(drawable.ic_flashlight_on_24dp);
        }
        else{
            codeScanner.setFlashEnabled(false);
            flashLight.setImageResource(drawable.ic_flashlight_off_24dp);
        }
        isFlashOn=!isFlashOn;
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
        if (isScanning) {
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
