package com.unimib.cooking.ui.homePage.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.unimib.cooking.model.Recipe;
import com.unimib.cooking.model.Result;
import com.unimib.cooking.repository.recipe.RecipeRepository;

public class RecipeViewModel extends ViewModel {

    private static final String TAG = RecipeViewModel.class.getSimpleName();

    private final RecipeRepository recipeRepository;
    private final int page;
    private MutableLiveData<Result> recipesListLiveData;
    private MutableLiveData<Result> randomRecipeLiveData;
    private MutableLiveData<Result> recipeByIdLiveData;
    private MutableLiveData<Result> favoriteRecipesListLiveData;

    public RecipeViewModel(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
        this.page = 1;
    }

    public MutableLiveData<Result> getRecipes(String letter, long lastUpdate) {
        fetchRecipes(letter, lastUpdate);
        return recipesListLiveData;
    }

    public MutableLiveData<Result> getRandomRecipe(long lastUpdate) {
        fetchRandomRecipe(lastUpdate);

        return randomRecipeLiveData;
    }

    public MutableLiveData<Result> getRecipesByName(String name,long lastUpdate) {
        fetchRecipesByName(name, lastUpdate);

        return recipesListLiveData;
    }

    private void fetchRecipesByName(String name, long lastUpdate) {
        recipesListLiveData = recipeRepository.fetchRecipesByName(name, page, lastUpdate);
    }

    public MutableLiveData<Result> getRecipeById(long id, long lastUpdate) {
        fetchRecipeById(id, lastUpdate);
        return recipeByIdLiveData;
    }

    private void fetchRecipeById(long id, long lastUpdate) {
        recipeByIdLiveData = recipeRepository.fetchRecipeById(id, page, lastUpdate);
    }

    private void fetchRecipes(String letter, long lastUpdate) {
        recipesListLiveData = recipeRepository.fetchRecipesByLetter(letter, page, lastUpdate);
    }

    private void fetchRandomRecipe(long lastUpdate) {
        randomRecipeLiveData = recipeRepository.fetchRandomRecipe(page, lastUpdate);
    }

    public void updateRecipe(Recipe recipe) {
        recipeRepository.updateRecipe(recipe);
    }

    public void removeFromFavorites(Recipe recipe) {
        recipe.setLiked(false);

        recipeRepository.updateRecipe(recipe);
    }

    public MutableLiveData<Result> getFavoriteRecipes() {
        if (favoriteRecipesListLiveData == null) {
            favoriteRecipesListLiveData = recipeRepository.getFavoriteRecipes();
        }
        else {
            recipeRepository.getFavoriteRecipes();
        }

        return favoriteRecipesListLiveData;
    }
}
