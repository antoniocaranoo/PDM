package com.unimib.cooking.ui.homePage.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
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
import com.unimib.cooking.util.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    public static final String TAG = HomeFragment.class.getName();
    private static final int initialShimmerElements = 5;
    private RecipeAdapter recipeAdapter;
    private List<Recipe> recipes;
    private SharedPreferencesUtils sharedPreferencesUtils;
    private RecipeViewModel recipeViewModel;
    private LinearLayout shimmerLinearLayout;
    private RecyclerView recyclerView;
    private FrameLayout noInternetView;
    private String selectedFilter;
    private UserViewModel userViewModel;
    private LinearLayout noRecipesLayout;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        IUserRepository userRepository = ServiceLocator.getInstance().
                getUserRepository(requireActivity().getApplication());
        userViewModel = new ViewModelProvider(
                requireActivity(),
                new UserViewModelFactory(userRepository)).get(UserViewModel.class);

        sharedPreferencesUtils = new SharedPreferencesUtils(requireActivity().getApplication());

        RecipeRepository articleRepository =
                ServiceLocator.getInstance().getRecipesRepository(
                        requireActivity().getApplication()
                );

        recipeViewModel = new ViewModelProvider(
                requireActivity(),
                new RecipeViewModelFactory(articleRepository)).get(RecipeViewModel.class);

        recipes = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        noRecipesLayout = view.findViewById(R.id.noRecipesLayout);
        shimmerLinearLayout = view.findViewById(R.id.shimmerLinearLayout);
        noInternetView = view.findViewById(R.id.noInternetMessage);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        recipes = new ArrayList<>();
        for (int i = 0; i < initialShimmerElements; i++) recipes.add(Recipe.getSampleRecipe());

        recipeAdapter = 
                new RecipeAdapter(R.layout.card_recipe, recipes, true,
                        new RecipeAdapter.OnItemClickListener() {
                            @Override
                            public void onRecipeItemClick(Recipe recipe) {
                                Bundle bundle = new Bundle();
                                bundle.putParcelable(Constants.BUNDLE_KEY_CURRENT_RECIPE, recipe);

                                Navigation.findNavController(view).navigate(R.id.action_searchFragment_to_visualize_card, bundle);
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

        sharedPreferencesUtils = new SharedPreferencesUtils(getContext());

        if (sharedPreferencesUtils.readStringData(
                Constants.SHARED_PREFERENCES_FILENAME, Constants.SHARED_PREFERNECES_LAST_UPDATE) != null) {
            lastUpdate = sharedPreferencesUtils.readStringData(
                    Constants.SHARED_PREFERENCES_FILENAME, Constants.SHARED_PREFERNECES_LAST_UPDATE);
        }

        if (!NetworkUtil.isInternetAvailable(getContext())) {
            noInternetView.setVisibility(View.VISIBLE);

            lastUpdate = System.currentTimeMillis() + "";
        }

        selectedFilter = sharedPreferencesUtils.readStringData(
                Constants.SHARED_PREFERENCES_FILENAME,
                Constants.SHARED_PREFERENCES_SELECTED_FILTER
        );

        if (selectedFilter == null || selectedFilter.equals("-1")) {
            selectedFilter = "A";
        }

        applyFilter(selectedFilter);

        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
        MenuItem sortItem = menu.findItem(R.id.action_sort);
        sortItem.setOnMenuItemClickListener(menuItem -> {
            showFilterMenu();
            return true;
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void showFilterMenu() {
        String[] letters = getResources().getStringArray(R.array.letter_array);
        PopupMenu popupMenu = new PopupMenu(requireContext(), getActivity().findViewById(R.id.action_sort));
        for (String letter : letters) {
            popupMenu.getMenu().add(letter).setOnMenuItemClickListener(menuItem -> {
                applyFilter(letter);
                return true;
            });
        }
        popupMenu.show();
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (isVisible()) {
            menu.setGroupVisible(Menu.NONE, true);

            MenuItem sortItem = menu.findItem(R.id.action_sort);

            int nightModeFlags = getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK;
            if (nightModeFlags == android.content.res.Configuration.UI_MODE_NIGHT_YES) {
                sortItem.setIcon(R.drawable.ic_sort_dark);
            } else {
                sortItem.setIcon(R.drawable.ic_sort);
            }

        } else {
            menu.setGroupVisible(Menu.NONE, false);
        }
    }



    private void applyFilter(String selectedFilter) {
        sharedPreferencesUtils.writeStringData(
                Constants.SHARED_PREFERENCES_FILENAME,
                Constants.SHARED_PREFERENCES_SELECTED_FILTER,
                selectedFilter
        );

        String lastUpdate = "0";

        if (!NetworkUtil.isInternetAvailable(getContext())) {
            noInternetView.setVisibility(View.VISIBLE);
            lastUpdate = System.currentTimeMillis() + "";
        }

        recipeViewModel.getRecipes(selectedFilter, Long.parseLong(lastUpdate)).observe(getViewLifecycleOwner(),
                result -> {
                    if (result.isSuccess()) {
                        recipes.clear();
                        recipes.addAll(((Result.RecipeSuccess) result).getData().getRecipes());
                        recipeAdapter.notifyDataSetChanged();

                        if (recipes.isEmpty()) {
                            recyclerView.setVisibility(View.GONE);
                            noRecipesLayout.setVisibility(View.VISIBLE);
                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                            noRecipesLayout.setVisibility(View.GONE);
                        }

                        shimmerLinearLayout.setVisibility(View.GONE);
                    } else {
                        Snackbar.make(getView(),
                                getString(R.string.noInternetMessage),
                                Snackbar.LENGTH_SHORT).show();
                    }
                });

        recipeAdapter.updateData(recipes);
    }

}