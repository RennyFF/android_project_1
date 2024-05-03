package com.example.myapplication.ui.notifications;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.room.Room;

import com.example.myapplication.History;
import com.example.myapplication.HistoryDAO;
import com.example.myapplication.HistoryDB;
import com.example.myapplication.R;

import java.util.List;
import java.util.Objects;

public class HistoryFragment extends Fragment {

    private HistoryDAO mHistoryDAO;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);

        mHistoryDAO = Room.databaseBuilder(requireContext(), HistoryDB.class, "HistoryDB")
                .build().getHistoryDAO();

        LinearLayout historyListLayout = rootView.findViewById(R.id.history_list);

        TextView backBtn = rootView.findViewById(R.id.btn_back_settings);
        backBtn.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.navigation_settings);
        });

        new LoadHistoryTask(requireContext(), historyListLayout).execute();

        return rootView;
    }
    private static class LoadHistoryTask extends AsyncTask<Void, Void, List<History>> {
        private Context context;
        private LinearLayout historyListLayout;
        private HistoryDAO historyDAO;

        public LoadHistoryTask(Context context, LinearLayout historyListLayout) {
            this.context = context;
            this.historyListLayout = historyListLayout;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Инициализация DAO перед выполнением запроса
            historyDAO = Room.databaseBuilder(context, HistoryDB.class, "HistoryDB")
                    .build().getHistoryDAO();
        }

        @Override
        protected List<History> doInBackground(Void... voids) {

            return historyDAO.getAllHistory();
        }

        @Override
        protected void onPostExecute(List<History> historyList) {
            super.onPostExecute(historyList);
            // Создание и добавление карточек истории
            for (History history : historyList) {
                CardView cardView = createHistoryCard(context, history);
                historyListLayout.addView(cardView);
            }
        }

        private CardView createHistoryCard(Context context, History history) {
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
            layout.setLayoutParams(layoutParams);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setPadding(24, 24, 24, 24);

            LinearLayout.LayoutParams left_layout_params = new LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    3.0f // увеличиваем вес
            );
            LinearLayout left_layout = new LinearLayout(context);
            left_layout_params.setMargins(0, 0,24,0);
            left_layout.setLayoutParams(left_layout_params);
            left_layout.setOrientation(LinearLayout.VERTICAL);

            TextView dateTimeTextView = new TextView(context);
            dateTimeTextView.setText(history.getDate_time());
            LinearLayout.LayoutParams dateTimeParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            dateTimeParams.setMargins(0, 0, 0, dpToPx(context, 6)); // Уменьшаем отступ вниз
            dateTimeTextView.setLayoutParams(dateTimeParams);
            left_layout.addView(dateTimeTextView);

            TextView resultTextView = new TextView(context);
            resultTextView.setText(history.getResult_text());
            resultTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16); // Устанавливаем размер текста
            resultTextView.setTextColor(Color.BLACK); // Устанавливаем цвет текста
            left_layout.addView(resultTextView);

            layout.addView(left_layout);

            ImageView imageView = new ImageView(context);
            imageView.setImageResource(R.drawable.ic_trash_24dp);
            imageView.setId(history.getId());
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            imageParams.gravity = Gravity.CENTER_VERTICAL | Gravity.END;
            imageView.setLayoutParams(imageParams);
            imageView.setPadding(12,12,12,12);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            historyDAO.deleteHistory(history);
                        }
                    }).start();
                }
            });
            layout.addView(imageView);


            cardView.addView(layout);

            return cardView;
        }

        // Метод для преобразования dp в пиксели
        private int dpToPx(Context context, int dp) {
            float density = context.getResources().getDisplayMetrics().density;
            return Math.round(dp * density);
        }



    }


}
