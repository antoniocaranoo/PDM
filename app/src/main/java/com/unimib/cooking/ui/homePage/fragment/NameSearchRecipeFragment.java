package com.unimib.cooking.ui.homePage.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.unimib.cooking.util.NetworkUtil;
import com.unimib.cooking.util.ServiceLocator;

import java.util.ArrayList;

public class NameSearchRecipeFragment extends Fragment {
    private RecipeAdapter recipeAdapter;
    private RecipeViewModel recipeViewModel;
    private ArrayList<Recipe> recipes;
    private UserViewModel userViewModel;
    private RecyclerView recyclerView;
    private LinearLayout noRecipesLayout;


    public NameSearchRecipeFragment() {
        // Required empty public constructor
    }

    public static NameSearchRecipeFragment newInstance() {
        NameSearchRecipeFragment fragment = new NameSearchRecipeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


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
        View rootView = inflater.inflate(R.layout.fragment_category_recipe, container, false);
        String categoryName = getArguments().getString("category_name");
        noRecipesLayout = rootView.findViewById(R.id.noRecipesLayout);
        recyclerView = rootView.findViewById(R.id.ricetteView);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));

        recipes = new ArrayList<>();
        for (int i = 0; i < 5; i++) recipes.add(Recipe.getSampleRecipe());

        recipeAdapter =
                new RecipeAdapter(R.layout.card_recipe, recipes, true,
                        new RecipeAdapter.OnItemClickListener() {
                            @Override
                            public void onRecipeItemClick(Recipe recipe) {

                                Bundle bundle = new Bundle();
                                bundle.putParcelable(Constants.BUNDLE_KEY_CURRENT_RECIPE, recipe);

                                Navigation.findNavController(rootView).navigate(R.id.action_searchFragment_to_visualize_card, bundle);
                            }

                            @Override
                            public void onFavoriteButtonPressed(int position) {
                                Recipe recipe = recipes.get(position);
                                recipe.setLiked(!recipe.getLiked());

                                recipeViewModel.updateRecipe(recipe);

                                userViewModel.updateFavoriteStatus(recipe);
                            }

                            @Override
                            public void onRemoveFromFavorites(int position) {
                                Recipe recipe = recipes.get(position);
                                recipe.setLiked(false);

                                recipeViewModel.removeFromFavorites(recipe);

                                userViewModel.updateFavoriteStatus(recipe);
                            }
                        });

        recyclerView.setAdapter(recipeAdapter);

        String lastUpdate = "0";

        if (!NetworkUtil.isInternetAvailable(getContext())) {
            lastUpdate = System.currentTimeMillis() + "";
        }

        recipeViewModel.getRecipesByName(categoryName ,Long.parseLong(lastUpdate)).observe(getViewLifecycleOwner(),
                result -> {
                    if (result.isSuccess()) {
                        this.recipes.clear();
                        this.recipes.addAll(((Result.RecipeSuccess) result).getData().getRecipes());
                        recipeAdapter.notifyDataSetChanged();

                        if (recipes.isEmpty()) {
                            recyclerView.setVisibility(View.GONE);
                            noRecipesLayout.setVisibility(View.VISIBLE);
                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                            noRecipesLayout.setVisibility(View.GONE);
                        }


                    } else {
                        Snackbar.make(getView(), getString(R.string.noInternetMessage), Snackbar.LENGTH_SHORT).show();
                    }
                });


        recipeAdapter.updateData(recipes);


        return rootView;
    }

        @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavHostFragment.findNavController(this).navigateUp();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}