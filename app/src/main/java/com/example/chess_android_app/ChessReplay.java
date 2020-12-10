package com.example.chess_android_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class ChessReplay extends AppCompatActivity {
    private static final String PREFS_NAME = "PrefsFile";

    private Button title_sort, date_sort;
    private ListView list_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chess_replay);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        Set<String> set = settings.getStringSet("set", null);

        List<String> titles = new ArrayList<>(set);

        title_sort = findViewById(R.id.title_sort);
        date_sort = findViewById(R.id.date_sort);
        list_view = findViewById(R.id.list_view);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, titles);
        list_view.setAdapter(arrayAdapter);

        list_view.setOnItemClickListener((parent, view, position, id) -> {
            String item = list_view.getItemAtPosition(position).toString();
            String title = item.substring(0, item.indexOf("\t\t\t"));
            String[] moveHistory = settings.getString(title, "").split(",");

            Intent intent = new Intent(ChessReplay.this, ReplayGames.class);
            intent.putExtra("moveHistory", moveHistory);
            startActivity(intent);
        });

        title_sort.setOnClickListener(button -> {
            titles.sort(Comparator.comparing(s -> s.substring(0, s.indexOf("\t\t\t")).toLowerCase()));
            ArrayAdapter newAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, titles);
            list_view.setAdapter(newAdapter);
        });;

        date_sort.setOnClickListener(button -> {
            titles.sort(Comparator.comparing(s -> s.substring(s.indexOf("\t\t\t") + 3)));
            ArrayAdapter newAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, titles);
            list_view.setAdapter(newAdapter);
        });;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}