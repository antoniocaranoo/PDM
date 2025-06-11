package com.unimib.cooking.ui.welcome.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.unimib.cooking.model.Recipe;
import com.unimib.cooking.model.Result;
import com.unimib.cooking.model.User;
import com.unimib.cooking.repository.user.IUserRepository;


public class UserViewModel extends ViewModel {
    private static final String TAG = UserViewModel.class.getSimpleName();

    private final IUserRepository userRepository;
    private MutableLiveData<Result> userMutableLiveData;
    private MutableLiveData<Result> userFavoriteRecipesMutableLiveData;
    private boolean authenticationError;

    public UserViewModel(IUserRepository userRepository) {
        this.userRepository = userRepository;
        authenticationError = false;
    }

    public MutableLiveData<Result> getUserMutableLiveData(String email, String password, boolean isUserRegistered) {
        getUserData(email, password, isUserRegistered);

        return userMutableLiveData;
    }

    public MutableLiveData<Result> getGoogleUserMutableLiveData(String token) {
        getUserData(token);

        return userMutableLiveData;
    }

    public MutableLiveData<Result> getUserFavoriteRecipesMutableLiveData() {
        userFavoriteRecipesMutableLiveData = userRepository.getUserFavoriteRecipes();
        return userFavoriteRecipesMutableLiveData;
    }

    public MutableLiveData<Result> removeAllFavorites() {
        if (userMutableLiveData == null) {
            userMutableLiveData = userRepository.removeAllFavorites();
        }
        else {
            userRepository.removeAllFavorites();
        }

        return userMutableLiveData;
    }

    public User getLoggedUser() {
        return userRepository.getLoggedUser();
    }

    public MutableLiveData<Result> logout() {
        if (userMutableLiveData == null) {
            userMutableLiveData = userRepository.logout();
        } else {
            userRepository.logout();
        }

        return userMutableLiveData;
    }

    public MutableLiveData<Result> deleteUser() {
        if (userMutableLiveData == null) {
            userMutableLiveData = userRepository.deleteUser();
        } else {
            userRepository.deleteUser();
        }

        return userMutableLiveData;
    }

    public MutableLiveData<Result> deleteProfile() {
        userMutableLiveData = userRepository.deleteProfile();

        return userMutableLiveData;
    }

    public void getUser(String email, String password, boolean isUserRegistered) {
        userRepository.getUser(email, password, isUserRegistered);
    }

    public boolean isAuthenticationError() {
        return authenticationError;
    }

    public void setAuthenticationError(boolean authenticationError) {
        this.authenticationError = authenticationError;
    }

    public void updateFavoriteStatus(Recipe recipe) {
        userRepository.updateFavoriteStatus(recipe);
    }

    private void getUserData(String email, String password, boolean isUserRegistered) {
        userMutableLiveData = userRepository.getUser(email, password, isUserRegistered);
    }

    private void getUserData(String token) {
        userMutableLiveData = userRepository.getGoogleUser(token);
    }
}
