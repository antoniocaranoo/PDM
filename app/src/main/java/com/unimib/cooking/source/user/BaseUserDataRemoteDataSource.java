package com.unimib.cooking.source.user;

import com.unimib.cooking.model.Recipe;
import com.unimib.cooking.model.User;
import com.unimib.cooking.repository.user.UserResponseCallback;

public abstract class BaseUserDataRemoteDataSource {
        protected UserResponseCallback userResponseCallback;

        public void setUserResponseCallback(UserResponseCallback userResponseCallback) {
            this.userResponseCallback = userResponseCallback;
        }

        public abstract void saveUserData(User user);

        public abstract void getUserFavoriteRecipes(String idToken);

        public abstract void updateFavoriteStatus(String idToken, Recipe recipe);

        public abstract void deleteProfile(String idToken);

        public abstract void removeAllFavorites(String idToken);
    }

