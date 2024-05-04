package com.example.myapplication.ui.notifications;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    SharedPreferences settings;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        settings = requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);

        Toolbar toolbar = root.findViewById(R.id.custom_app_bar);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        View switchLayoutSound = root.findViewById(R.id.switch_layout_sound);
        View switchLayoutVibration = root.findViewById(R.id.switch_layout_vibration);

        SwitchCompat switchCompat_sound = switchLayoutSound.findViewById(R.id.switch_compat_sound);
        SwitchCompat switchCompat_vibr = switchLayoutVibration.findViewById(R.id.switch_compat_vibr);
        switchCompat_sound.setChecked(settings.getBoolean("Sound_Switch", true));
        switchCompat_vibr.setChecked(settings.getBoolean("Vibr_Switch", true));
        switchCompat_sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    SharedPreferences.Editor prefEditor = settings.edit();
                    prefEditor.putBoolean("Sound_Switch", switchCompat_sound.isChecked());
                prefEditor.apply();
            }
        });
        switchCompat_vibr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor prefEditor = settings.edit();
                prefEditor.putBoolean("Vibr_Switch", switchCompat_vibr.isChecked());
                prefEditor.apply();
            }
        });

        TextView btn_app_info = root.findViewById(R.id.btn_app_info);
        btn_app_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the dialog fragment
                AboutFragment dialogFragment = new AboutFragment();
                dialogFragment.show(getChildFragmentManager(), "AboutFragment");
            }
        });

        TextView btn_terms = root.findViewById(R.id.btn_terms_of_service);
        btn_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.navigation_terms);
            }
        });

        TextView btn_scan_history = root.findViewById(R.id.btn_scan_history);
        btn_scan_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.navigation_history_scanned);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
