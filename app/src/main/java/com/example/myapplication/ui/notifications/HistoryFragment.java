package com.example.myapplication.ui.notifications;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

        RelativeLayout historyListLayout = rootView.findViewById(R.id.history_list);

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
        private RelativeLayout historyListLayout;
        private HistoryDAO historyDAO;

        public LoadHistoryTask(Context context, RelativeLayout historyListLayout) {
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
            // Выполнение запроса к базе данных в фоновом потоке
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
            // Создаем новую карточку истории
            CardView cardView = new CardView(context);
            cardView.setLayoutParams(new CardView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            cardView.setCardBackgroundColor(Color.WHITE);
            cardView.setRadius(16); // Устанавливаем скругление углов

            // Создаем макет для содержимого карточки
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            LinearLayout layout = new LinearLayout(context);
            layout.setLayoutParams(layoutParams);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(24, 24, 24, 24);

            // Создаем и добавляем текстовые элементы в макет
            TextView dateTimeTextView = new TextView(context);
            dateTimeTextView.setText(history.getDate_time());
            layout.addView(dateTimeTextView);

            TextView resultTextView = new TextView(context);
            resultTextView.setText(history.getResult_text());
            layout.addView(resultTextView);

            // Добавляем макет в карточку
            cardView.addView(layout);

            return cardView;
        }
    }


}
