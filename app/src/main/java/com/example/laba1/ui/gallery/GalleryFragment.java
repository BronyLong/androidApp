package com.example.laba1.ui.gallery;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.laba1.R;
import com.example.laba1.Recipe;
import com.example.laba1.databinding.FragmentGalleryBinding;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Получаем данные о рецепте из аргументов
        Bundle args = getArguments();
        if (args != null) {
            Recipe recipe = (Recipe) args.getSerializable("recipe");
            if (recipe != null) {
                displayRecipe(recipe);
            }
        }

        // Найдите кнопку "Назад" в вашем макете
        Button backButton = root.findViewById(R.id.button2);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
                navController.navigateUp(); // или navController.popBackStack();
            }
        });

        return root;
    }

    private void displayRecipe(Recipe recipe) {
        TextView recipie_name = binding.recipieName;
        TextView calories = binding.calories;
        TextView cookTime = binding.cookTime;
        TextView ingridients = binding.ingridients;
        TextView diff = binding.difficulty;
        String textToSpan = "Калорийность: " + recipe.getCalorie() + " ккал";
        SpannableString textToShow = new SpannableString(textToSpan);
        textToShow.setSpan(new UnderlineSpan(), 0, 13, 0);
        recipie_name.setText("" + recipe.getName());
        calories.setText(textToShow);
        textToSpan = "Время приготовления: " + recipe.getTime() + " мин";
        textToShow = new SpannableString(textToSpan);
        textToShow.setSpan(new UnderlineSpan(), 0, 20, 0);
        cookTime.setText(textToShow);
        textToSpan = "Ингридиенты: " + recipe.getIngredients();
        textToShow = new SpannableString(textToSpan);
        textToShow.setSpan(new UnderlineSpan(), 0, 12, 0);
        ingridients.setText(textToShow);
        textToSpan = "Сложность: " + recipe.getDifficulty() + "/10";
        textToShow = new SpannableString(textToSpan);
        textToShow.setSpan(new UnderlineSpan(), 0, 10, 0);
        diff.setText(textToShow);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
