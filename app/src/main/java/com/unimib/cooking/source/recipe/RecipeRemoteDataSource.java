package com.unimib.cooking.source.recipe;

import android.util.Log;

import androidx.annotation.NonNull;

import com.unimib.cooking.model.Recipe;
import com.unimib.cooking.model.RecipeAPIResponse;
import com.unimib.cooking.service.RecipeApiService;
import com.unimib.cooking.util.ServiceLocator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeRemoteDataSource extends BaseRecipeRemoteDataSource {

    private final RecipeApiService recipeAPIService;

    public RecipeRemoteDataSource() {
        this.recipeAPIService = ServiceLocator.getInstance().getRecipeAPIService();
    }


    @Override
    public void getRecipesByCategory(String category) {
        Call<RecipeAPIResponse> recipesResponseCall = recipeAPIService.getRecipesByCategory(category);

        recipesResponseCall.enqueue(new Callback<RecipeAPIResponse>() {
            @Override
            public void onResponse(@NonNull Call<RecipeAPIResponse> call,
                                   @NonNull Response<RecipeAPIResponse> response) {

                if (response.body() != null && response.isSuccessful()) {

                    recipeCallback.onSuccessFromRemote(response.body(), System.currentTimeMillis());

                } else {
                    recipeCallback.onFailureFromRemote(new Exception("API_KEY_ERROR"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<RecipeAPIResponse> call, @NonNull Throwable t) {
                recipeCallback.onFailureFromRemote(new Exception("RETROFIT_ERROR"));
            }
        });
    }


    @Override
    public void getRecipesByArea(String area) {

        Call<RecipeAPIResponse> recipesResponseCall = recipeAPIService.getRecipesByArea(area);

        recipesResponseCall.enqueue(new Callback<RecipeAPIResponse>() {
            @Override
            public void onResponse(@NonNull Call<RecipeAPIResponse> call,
                                   @NonNull Response<RecipeAPIResponse> response) {

                if (response.body() != null && response.isSuccessful()) {
                    recipeCallback.onSuccessFromRemote(response.body(), System.currentTimeMillis());

                } else {
                    recipeCallback.onFailureFromRemote(new Exception("API_KEY_ERROR"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<RecipeAPIResponse> call, @NonNull Throwable t) {
                recipeCallback.onFailureFromRemote(new Exception("RETROFIT_ERROR"));
            }
        });
    }

    @Override
    public void getRecipesById(long id) {
        Call<RecipeAPIResponse> recipesResponseCall = recipeAPIService.getRecipesById(String.valueOf(id));

        recipesResponseCall.enqueue(new Callback<RecipeAPIResponse>() {
            @Override
            public void onResponse(@NonNull Call<RecipeAPIResponse> call,
                                   @NonNull Response<RecipeAPIResponse> response) {

                if (response.body() != null && response.isSuccessful()) {
                    recipeCallback.onSuccessFromRemote(response.body(), System.currentTimeMillis());

                } else {
                    recipeCallback.onFailureFromRemote(new Exception("API_KEY_ERROR"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<RecipeAPIResponse> call, @NonNull Throwable t) {
                recipeCallback.onFailureFromRemote(new Exception("RETROFIT_ERROR"));
            }
        });
    }

    @Override
    public void getRandomRecipe() {

        Call<RecipeAPIResponse> recipesResponseCall = recipeAPIService.getRandomRecipe();

        recipesResponseCall.enqueue(new Callback<RecipeAPIResponse>() {
            @Override
            public void onResponse(@NonNull Call<RecipeAPIResponse> call,
                                   @NonNull Response<RecipeAPIResponse> response) {

                if (response.body() != null && response.isSuccessful()) {
                    recipeCallback.onSuccessFromRemote(response.body(), System.currentTimeMillis());

                } else {
                    recipeCallback.onFailureFromRemote(new Exception("API_KEY_ERROR"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<RecipeAPIResponse> call, @NonNull Throwable t) {
                recipeCallback.onFailureFromRemote(new Exception("RETROFIT_ERROR"));
            }
        });
    }

    @Override
    public void getRecipesByLetter(String letter) {
        Call<RecipeAPIResponse> recipesResponseCall = recipeAPIService.getRecipes(letter);

        recipesResponseCall.enqueue(new Callback<RecipeAPIResponse>() {
            @Override
            public void onResponse(@NonNull Call<RecipeAPIResponse> call,
                                   @NonNull Response<RecipeAPIResponse> response) {

                if (response.body() != null && response.isSuccessful()) {
                    if(response.body().getRecipes()==null){
                        response.body().setRecipes(new ArrayList<Recipe>());
                    }

                    Log.d("RecipeRemoteDataSource", "onResponse: " + response.body().getRecipes());
                    recipeCallback.onSuccessFromRemote(response.body(), System.currentTimeMillis());

                } else {
                    recipeCallback.onFailureFromRemote(new Exception("API_KEY_ERROR"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<RecipeAPIResponse> call, @NonNull Throwable t) {
                recipeCallback.onFailureFromRemote(new Exception("RETROFIT_ERROR"));
            }
        });
    }

    @Override
    public void getRecipesByName(String name) {
        Call<RecipeAPIResponse> recipesResponseCall = recipeAPIService.getRecipesByName(name);

        recipesResponseCall.enqueue(new Callback<RecipeAPIResponse>() {
            @Override
            public void onResponse(@NonNull Call<RecipeAPIResponse> call,
                                   @NonNull Response<RecipeAPIResponse> response) {

                if (response.body() != null && response.isSuccessful()) {
                    if(response.body().getRecipes()==null){
                        response.body().setRecipes(new ArrayList<Recipe>());
                    }
                    recipeCallback.onSuccessFromRemote(response.body(), System.currentTimeMillis());
                } else {
                    recipeCallback.onFailureFromRemote(new Exception("API_KEY_ERROR"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<RecipeAPIResponse> call, @NonNull Throwable t) {
                recipeCallback.onFailureFromRemote(new Exception("RETROFIT_ERROR"));
            }
        });
    }
}
