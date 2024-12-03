package com.example.laba1.ui.home;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laba1.DatabaseHelper;
import com.example.laba1.LoginAct;
import com.example.laba1.Recipe;
import com.example.laba1.RecipeAdapter;
import com.example.laba1.R;
import com.example.laba1.databinding.FragmentHomeBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements RecipeAdapter.OnItemClickListener {
    private FragmentHomeBinding binding;
    private RecipeAdapter adapter;
    private Context context;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = root.findViewById(R.id.recyle);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Инициализируем адаптер с пустым списком
        adapter = new RecipeAdapter(new ArrayList<>());
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);

        // Выполняем запрос для получения данных
        fetchRecipes();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(Recipe recipe) {
        Log.i("Click", "Clicked on recipe: " + recipe.getName());
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
        Bundle bundle = new Bundle();
        bundle.putSerializable("recipe", recipe);
        navController.navigate(R.id.nav_gallery, bundle);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private void fetchRecipes() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url;
                HttpURLConnection connection = null;

                try {
                    url = new URL("https://raw.githubusercontent.com/Lpirskaya/JsonLab/master/recipes2022.json");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder json = new StringBuilder();
                        String line;

                        while ((line = reader.readLine()) != null) {
                            json.append(line);
                        }
                        reader.close();

                        DatabaseHelper dbHelper = new DatabaseHelper(new LoginAct());
                        SQLiteDatabase db = dbHelper.getWritableDatabase();

                        try {
                            JSONArray jsonArray = new JSONArray(json.toString());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String name = jsonObject.getString("Name");
                                String ingredients = jsonObject.getString("Ingredients");
                                int difficulty = jsonObject.getInt("Difficulty");
                                int time = jsonObject.getInt("Time");
                                int calorie = jsonObject.getInt("Calorie");

                                ContentValues cv = new ContentValues();
                                cv.put("name", name);
                                cv.put("ingridients", ingredients);
                                cv.put("difficulty", difficulty);
                                cv.put("time", time);
                                cv.put("calorie", calorie);
                                long RowId = db.insert("recipes", null, cv);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        List<Recipe> recipes = new ArrayList<>();
                        String[] columns = new String[]{"_id", "name", "ingridients", "difficulty", "time", "calorie"};
                        Cursor cursor = db.query("recipes", columns, null, null, null, null, null);

                        if (cursor.moveToFirst()) {
                            int idColIndex = cursor.getColumnIndex("_id");
                            int field1ColIndex = cursor.getColumnIndex("name");
                            int field2ColIndex = cursor.getColumnIndex("ingridients");
                            int diffColIndex = cursor.getColumnIndex("difficulty");
                            int timeColIndex = cursor.getColumnIndex("time");
                            int calColIndex = cursor.getColumnIndex("calorie");

                            do {
                                String rName = cursor.getString(field1ColIndex);
                                String rIngridients = cursor.getString(field2ColIndex);
                                int diff = cursor.getInt(diffColIndex);
                                int time = cursor.getInt(timeColIndex);
                                int calories = cursor.getInt(calColIndex);
                                recipes.add(new Recipe(rName, rIngridients, diff, time, calories));
                            } while (cursor.moveToNext());
                        }

                        cursor.close();
                        updateRecyclerView(recipes);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    private List<Recipe> parseJson(String json) {
        List<Recipe> recipes = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("Name");
                String ingredients = jsonObject.getString("Ingredients");
                int difficulty = jsonObject.getInt("Difficulty");
                int time = jsonObject.getInt("Time");
                int calorie = jsonObject.getInt("Calorie");

                DatabaseHelper dbHelper = new DatabaseHelper(new LoginAct());
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put("name", name);
                cv.put("ingridients", ingredients);
                cv.put("difficulty", difficulty);
                cv.put("time", time);
                cv.put("calorie", calorie);
                long RowId = db.insert("recipes", null, cv);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return recipes;
    }

    private void updateRecyclerView(final List<Recipe> recipes) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.setRecipes(recipes);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
