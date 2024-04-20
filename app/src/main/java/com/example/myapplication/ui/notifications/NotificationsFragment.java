package com.example.myapplication.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Toolbar toolbar = root.findViewById(R.id.custom_app_bar);

        // Добавляем Toolbar в ActionBar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        View switchLayoutSound = root.findViewById(R.id.switch_layout_sound);
        View switchLayoutVibration = root.findViewById(R.id.switch_layout_vibration);

        SwitchCompat switchCompat_sound = switchLayoutSound.findViewById(R.id.switch_compat);
        SwitchCompat switchCompat_vibr = switchLayoutVibration.findViewById(R.id.switch_compat);
        switchCompat_sound.setText("MeowSound");
        switchCompat_vibr.setText("MeowVibr");

        // Настройте иконки для каждого кастомного макета
        ImageView iconImageViewSound = switchLayoutSound.findViewById(R.id.icon);
        ImageView iconImageViewVibration = switchLayoutVibration.findViewById(R.id.icon);

        // Установите иконки
        iconImageViewSound.setImageResource(R.drawable.ic_sound_24dp);
        iconImageViewVibration.setImageResource(R.drawable.ic_round_vibration_24dp);
        iconImageViewSound.setVisibility(View.VISIBLE);
        iconImageViewVibration.setVisibility(View.VISIBLE);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}