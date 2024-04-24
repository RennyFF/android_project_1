package com.example.myapplication.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentNotificationsBinding;
import com.example.myapplication.ui.notifications.AboutFragment;
import com.example.myapplication.ui.notifications.NotificationsViewModel;
import com.example.myapplication.ui.notifications.TermsFragment;

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
        switchCompat_sound.setText(R.string.sound);
        switchCompat_vibr.setText(R.string.vibr);

        // Настройте иконки для каждого кастомного макета
        ImageView iconImageViewSound = switchLayoutSound.findViewById(R.id.icon);
        ImageView iconImageViewVibration = switchLayoutVibration.findViewById(R.id.icon);

        // Установите иконки
        iconImageViewSound.setImageResource(R.drawable.ic_sound_24dp);
        iconImageViewVibration.setImageResource(R.drawable.ic_round_vibration_24dp);
        iconImageViewSound.setVisibility(View.VISIBLE);
        iconImageViewVibration.setVisibility(View.VISIBLE);

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
                // Получение навигационного контроллера
                NavController navController = Navigation.findNavController(v);
                // Навигация к фрагменту TermsFragment
                navController.navigate(R.id.navigation_terms);
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
