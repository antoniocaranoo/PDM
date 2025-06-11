package com.unimib.cooking.ui.homePage.fragment;

import static android.widget.Toast.makeText;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.unimib.cooking.R;
import com.unimib.cooking.adapter.RecipeAdapter;
import com.unimib.cooking.model.Recipe;
import com.unimib.cooking.model.Result;
import com.unimib.cooking.repository.recipe.RecipeRepository;
import com.unimib.cooking.repository.user.IUserRepository;
import com.unimib.cooking.ui.homePage.viewmodel.RecipeViewModel;
import com.unimib.cooking.ui.homePage.viewmodel.RecipeViewModelFactory;
import com.unimib.cooking.ui.welcome.viewmodel.UserViewModel;
import com.unimib.cooking.ui.welcome.viewmodel.UserViewModelFactory;
import com.unimib.cooking.util.Constants;
import com.unimib.cooking.util.ServiceLocator;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {

    private List<Recipe> recipes;
    private RecipeAdapter adapter;

    private RecipeViewModel recipeViewModel;
    private UserViewModel userViewModel;
    private RecyclerView recyclerView;
    TextView noRecipesMessage;

    LinearLayout noRecipeLayout;

    private CircularProgressIndicator circularProgressIndicator;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RecipeRepository articleRepository =
                ServiceLocator.getInstance().getRecipesRepository(
                        requireActivity().getApplication()
                );

        recipeViewModel = new ViewModelProvider(
                requireActivity(),
                new RecipeViewModelFactory(articleRepository)).get(RecipeViewModel.class);

        IUserRepository userRepository = ServiceLocator.getInstance().
                getUserRepository(requireActivity().getApplication());

        userViewModel = new ViewModelProvider(
                requireActivity(),
                new UserViewModelFactory(userRepository)).get(UserViewModel.class);


        recipes = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        noRecipesMessage = view.findViewById(R.id.noRecipesMessage);
        Log.d("FAVORITE_FRAGMENT", "onCreateView: " + noRecipesMessage);
        circularProgressIndicator = view.findViewById(R.id.circularProgressIndicator);
        recyclerView = view.findViewById(R.id.recyclerView);
        SearchView searchView = view.findViewById(R.id.searchView);
        noRecipeLayout = view.findViewById(R.id.noRecipesLayout);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        adapter = new RecipeAdapter(R.layout.card_recipe, recipes, false, new RecipeAdapter.OnItemClickListener() {
            @Override
            public void onRecipeItemClick(Recipe recipe) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constants.BUNDLE_KEY_CURRENT_RECIPE, recipe);

                Navigation.findNavController(view).navigate(R.id.action_searchFragment_to_visualize_card, bundle);
            }

            @Override
            public void onFavoriteButtonPressed(int position) {
            }

            @Override
            public void onRemoveFromFavorites(int position) {
                if (position >= 0 && position < recipes.size()) {
                    Recipe recipe = recipes.get(position);
                    recipe.setLiked(false);

                    recipes.remove(position);
                    recipeViewModel.removeFromFavorites(recipe);
                    userViewModel.updateFavoriteStatus(recipe);

                    adapter.notifyItemRemoved(position);

                    makeText(getContext(), R.string.recipe_removed_from_favourite, Toast.LENGTH_SHORT).show();
                } else {
                    makeText(getContext(), R.string.not_valid_index, Toast.LENGTH_SHORT).show();
                }
            }
        });

        recyclerView.setAdapter(adapter);

        userViewModel.getUserFavoriteRecipesMutableLiveData().observe(getViewLifecycleOwner(), result -> {
            if (result.isSuccess()) {
                this.recipes.clear();
                this.recipes.addAll(((Result.RecipeListSuccess) result).getData());

                adapter.notifyDataSetChanged();

                if (recipes.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    noRecipeLayout.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    noRecipeLayout.setVisibility(View.GONE);
                }

                circularProgressIndicator.setVisibility(View.GONE);
            } else {
                Snackbar.make(view, "error", Snackbar.LENGTH_SHORT).show();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterList(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        return view;
    }

    private void filterList(String text) {

        ArrayList<Recipe> filteredRecipes = new ArrayList<>();

        for (Recipe recipe : recipes) {
            if (recipe.getStrMeal().toLowerCase().contains(text.toLowerCase())) {
                filteredRecipes.add(recipe);
            }
        }

        if(text.isEmpty() && filteredRecipes.isEmpty()){
            adapter.setFilteredList(recipes);
            return;
        }

        if (filteredRecipes.isEmpty()) {
            noRecipeLayout.setVisibility(View.VISIBLE);
        }else{
            noRecipeLayout.setVisibility(View.GONE);
        }
        adapter.setFilteredList(filteredRecipes);

    }
}