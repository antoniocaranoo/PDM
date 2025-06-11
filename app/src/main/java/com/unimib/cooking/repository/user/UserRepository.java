package com.unimib.cooking.repository.user;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.unimib.cooking.model.Recipe;
import com.unimib.cooking.model.RecipeAPIResponse;
import com.unimib.cooking.model.Result;
import com.unimib.cooking.model.User;
import com.unimib.cooking.repository.recipe.RecipeResponseCallBack;
import com.unimib.cooking.source.recipe.BaseRecipeLocalDataSource;
import com.unimib.cooking.source.user.BaseUserAuthenticationRemoteDataSource;
import com.unimib.cooking.source.user.BaseUserDataRemoteDataSource;

import java.util.List;

public class UserRepository implements IUserRepository, UserResponseCallback, RecipeResponseCallBack {

    private static final String TAG = UserRepository.class.getSimpleName();

    private final BaseUserAuthenticationRemoteDataSource userRemoteDataSource;
    private final BaseUserDataRemoteDataSource userDataRemoteDataSource;
    private final BaseRecipeLocalDataSource recipeLocalDataSource;
    private final MutableLiveData<Result> userMutableLiveData;
    private final MutableLiveData<Result> userFavoriteRecipesMutableLiveData;
    private final MutableLiveData<Result> userPreferencesMutableLiveData;

    public UserRepository(BaseUserAuthenticationRemoteDataSource userRemoteDataSource,
                          BaseUserDataRemoteDataSource userDataRemoteDataSource,
                          BaseRecipeLocalDataSource recipesLocalDataSource) {
        this.userRemoteDataSource = userRemoteDataSource;
        this.userDataRemoteDataSource = userDataRemoteDataSource;
        this.recipeLocalDataSource = recipesLocalDataSource;
        this.userMutableLiveData = new MutableLiveData<>();
        this.userPreferencesMutableLiveData = new MutableLiveData<>();
        this.userFavoriteRecipesMutableLiveData = new MutableLiveData<>();
        this.userRemoteDataSource.setUserResponseCallback(this);
        this.userDataRemoteDataSource.setUserResponseCallback(this);
        this.recipeLocalDataSource.setRecipeCallback(this);
    }

    @Override
    public MutableLiveData<Result> getUser(String email, String password, boolean isUserRegistered) {
        if (isUserRegistered) {
            signIn(email, password);
        } else {
            signUp(email, password);
        }
        return userMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> getGoogleUser(String idToken) {
        signInWithGoogle(idToken);
        return userMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> getUserFavoriteRecipes() {
        User user = getLoggedUser();
        userDataRemoteDataSource.getUserFavoriteRecipes(user.getIdToken());
        return userFavoriteRecipesMutableLiveData;
    }


    @Override
    public User getLoggedUser() {
        return userRemoteDataSource.getLoggedUser();
    }

    @Override
    public MutableLiveData<Result> logout() {
        userRemoteDataSource.logout();
        return userMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> deleteUser() {
        userRemoteDataSource.deleteUser();
        return userMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> deleteProfile() {
        User user = getLoggedUser();
        userDataRemoteDataSource.deleteProfile(user.getIdToken());
        return userMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> removeAllFavorites() {
        User user = getLoggedUser();
        userDataRemoteDataSource.removeAllFavorites(user.getIdToken());
        return userMutableLiveData;
    }

    @Override
    public void signUp(String email, String password) {
        userRemoteDataSource.signUp(email, password);
    }

    @Override
    public void signIn(String email, String password) {
        userRemoteDataSource.signIn(email, password);
    }

    @Override
    public void signInWithGoogle(String token) {
        userRemoteDataSource.signInWithGoogle(token);
    }

    @Override
    public void onSuccessFromAuthentication(User user) {
        if (user != null) {
            userDataRemoteDataSource.saveUserData(user);
            Log.d("TAGGA", user.getIdToken());
        }
    }

    @Override
    public void onFailureFromAuthentication(String message) {
        Result.Error result = new Result.Error(message);
        userMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessFromRemoteDatabase(User user) {
        Result.UserSuccess result = new Result.UserSuccess(user);
        userMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessFromRemoteDatabase(List<Recipe> recipeList) {
        recipeLocalDataSource.insertRecipes(recipeList);
        Result.RecipeListSuccess result = new Result.RecipeListSuccess(recipeList);
        userFavoriteRecipesMutableLiveData.postValue(result);
    }

    @Override
    public void onFailureFromRemoteDatabase(String message) {
        Result.Error result = new Result.Error(message);
        userMutableLiveData.postValue(result);
    }

    @Override
    public void updateFavoriteStatus(Recipe recipe) {
        User user = getLoggedUser();
        userDataRemoteDataSource.updateFavoriteStatus(user.getIdToken(), recipe);
    }

    @Override
    public void onSuccessLogout() {
        Result.Success success = new Result.Success();
        userMutableLiveData.postValue(success);
    }

    @Override
    public void onSuccessDeleteUser() {
        Result.Success success = new Result.Success();
        userMutableLiveData.postValue(success);
    }

    @Override
    public void onErrorDeleteUser() {
        Result.Error error = new Result.Error(null);
        userMutableLiveData.postValue(error);
    }

    @Override
    public void onSuccessDeleteProfile() {
        Result.Success success = new Result.Success();
        userMutableLiveData.postValue(success);
    }

    @Override
    public void onErrorDeleteProfile() {
        Result.Error error = new Result.Error(null);
        userMutableLiveData.postValue(error);
    }

    @Override
    public void onSuccessDeleteAllFavorites() {
        Result.Success success = new Result.Success();
        userMutableLiveData.postValue(success);
    }

    @Override
    public void onErrorDeleteAllFavorites() {
        Result.Error error = new Result.Error(null);
        userMutableLiveData.postValue(error);
    }

    @Override
    public void onSuccessFromRemote(RecipeAPIResponse recipeAPIResponse, long lastUpdate) {

    }

    @Override
    public void onFailureFromRemote(Exception exception) {

    }

    @Override
    public void onSuccessFromLocal(List<Recipe> recipesList) {

    }

    @Override
    public void onFailureFromLocal(Exception exception) {

    }

    @Override
    public void onRecipesFavoriteStatusChanged(Recipe recipe, List<Recipe> favoriteRecipes) {

    }

    @Override
    public void onRecipesFavoriteStatusChanged(List<Recipe> recipes) {

    }

    @Override
    public void onDeleteFavoriteRecipesSuccess(List<Recipe> favoriteRecipes) {

    }

    @Override
    public void onSuccessFromLocalByLetter(List<Recipe> recipesList) {

    }

    @Override
    public void onSuccessFromLocalByCategory(List<Recipe> recipesList) {

    }

    @Override
    public void onSuccessFromLocalByArea(List<Recipe> recipesList) {

    }
}
