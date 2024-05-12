package com.example.myapplication.ui.dashboard;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.room.Room;

import com.example.myapplication.GenerateHistory;
import com.example.myapplication.GenerateHistoryDAO;
import com.example.myapplication.GenerateHistoryDB;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentDashboardBinding;
import com.example.myapplication.ui.home.ScanResult;
import com.google.zxing.BarcodeFormat;

import java.util.ArrayList;
import java.util.List;


public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private GenerateHistoryDAO mGenerateHistoryDAO;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mGenerateHistoryDAO = Room.databaseBuilder(requireContext(), GenerateHistoryDB.class, "GenerateHistoryDB")
                .build().getGenerateHistoryDAO();
        LinearLayout generateHistoryListLayout = root.findViewById(R.id.generate_card);

        LinearLayout generateBtn = root.findViewById(R.id.generate_btn);
        generateBtn.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.navigation_generate_code);
        });


        new DashboardFragment.LoadGenerateHistoryTask(requireContext(), generateHistoryListLayout).execute();

        return root;
    }
    private static class LoadGenerateHistoryTask extends AsyncTask<Void, Void, List<GenerateHistory>> {
        private Context context;
        private LinearLayout generateHistoryListLayout;
        private GenerateHistoryDAO generateHistoryDAO;

        public LoadGenerateHistoryTask(Context context, LinearLayout generateHistoryListLayout) {
            this.context = context;
            this.generateHistoryListLayout = generateHistoryListLayout;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            generateHistoryDAO = Room.databaseBuilder(context, GenerateHistoryDB.class, "GenerateHistoryDB")
                    .build().getGenerateHistoryDAO();
        }

        @Override
        protected List<GenerateHistory> doInBackground(Void... voids) {

            return generateHistoryDAO.getAllGenerateHistory();
        }

        @Override
        protected void onPostExecute(List<GenerateHistory> generateHistoryList) {
            super.onPostExecute(generateHistoryList);
            for (GenerateHistory ghistory : generateHistoryList) {
                CardView cardView = createGHistoryCard(context, ghistory);
                generateHistoryListLayout.addView(cardView);
            }
        }
        private CardView createGHistoryCard(Context context, GenerateHistory ghistory) {
            CardView cardView = new CardView(context);
            CardView.LayoutParams cardParams = new CardView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            cardParams.setMargins(24, 24, 24, 24);
            cardView.setLayoutParams(cardParams);
            cardView.setCardBackgroundColor(Color.WHITE);
            cardView.setCardElevation(0);
            cardView.setRadius(24);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            LinearLayout layout = new LinearLayout(context);
            layoutParams.gravity = Gravity.CENTER_VERTICAL;
            layout.setLayoutParams(layoutParams);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setPadding(24, 24, 0, 24);

            LinearLayout.LayoutParams rightLayoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            LinearLayout rightLayout = new LinearLayout(context);
            rightLayoutParams.setMargins(24, 0,0,0);
            rightLayout.setLayoutParams(rightLayoutParams);
            rightLayout.setOrientation(LinearLayout.VERTICAL);



            LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            textLayoutParams.gravity = Gravity.CENTER_VERTICAL;
            TextView textView = new TextView(context);
            textView.setLayoutParams(textLayoutParams);
            textView.setText(ghistory.getType());

            rightLayout.addView(textView);

            TextView textView2 = new TextView(context);
            LinearLayout.LayoutParams textView2Params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            textView2Params.setMargins(0, 12, 0, 0);
            textView2.setLayoutParams(textView2Params);
            textView2.setText(ghistory.getText());
            textView2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            textView2.setTextColor(Color.BLACK);
            rightLayout.addView(textView2);

            ImageView iconImageView = new ImageView(context);
            iconImageView.setImageResource(R.drawable.qr_svgrepo_com);
            LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            iconParams.gravity = Gravity.CENTER_VERTICAL;
            iconImageView.setLayoutParams(iconParams);
            iconImageView.setPadding(12,12,12,12);
            layout.addView(iconImageView);

            layout.addView(rightLayout);
            cardView.addView(layout);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ScanResult _tmp = new ScanResult(ghistory.getId(), ghistory.getType(), "", ghistory.getText());
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("scan_result", _tmp);

                    NavController navController = Navigation.findNavController(v);
                    navController.navigate(R.id.navigation_preview_generated, bundle);
                }
            });

            return cardView;
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}