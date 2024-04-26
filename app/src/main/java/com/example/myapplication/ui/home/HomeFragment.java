package com.example.myapplication.ui.home;

import static com.example.myapplication.R.*;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.example.myapplication.ui.notifications.AboutFragment;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private DecoratedBarcodeView barcodeView;
    private boolean isScanning = false;
    private boolean isFlashOn;
    ImageView flashLight;

    public HomeFragment() {
        isFlashOn = false;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        barcodeView = root.findViewById(R.id.barcode_scanner);
        barcodeView.getViewFinder().setLaserVisibility(false);
        startScanning();
        flashLight = root.findViewById(id.flashlight);
        flashLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeTorch();
            }
        });
        return root;
    }
    private void ChangeTorch(){
        if(isFlashOn){
            barcodeView.setTorchOn();
            flashLight.setImageResource(drawable.ic_flashlight_on_24dp);
        }
        else{
            barcodeView.setTorchOff();
            flashLight.setImageResource(drawable.ic_flashlight_off_24dp);
        }
        isFlashOn=!isFlashOn;
    }
    private void startScanning() {
        try {
            barcodeView.setTorchOff();
            barcodeView.resume();
            isScanning = true;
        } catch (Exception e) {
            Toast.makeText(getContext(), "Failed to start scanning", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopScanning() {
        try {
            barcodeView.pause();
            isScanning = false;
        } catch (Exception e) {
            Toast.makeText(getContext(), "Failed to stop scanning", Toast.LENGTH_SHORT).show();
        }
    }

    private BarcodeCallback barcodeCallback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result != null) {
                Toast.makeText(getContext(), "Scanned: " + result.getText(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Scan failed", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
            // Unused callback
        }
    };

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
