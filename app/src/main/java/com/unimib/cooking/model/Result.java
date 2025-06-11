package com.unimib.cooking.model;

import java.util.List;

public abstract class Result {
    private Result() {}

    public boolean isSuccess() {
        return !(this instanceof Error);
    }

    public static final class RecipeSuccess extends Result {
        private final RecipeAPIResponse recipeAPIResponse;
        public RecipeSuccess(RecipeAPIResponse recipeAPIResponse) {
            this.recipeAPIResponse = recipeAPIResponse;
        }
        public RecipeAPIResponse getData() {
            return recipeAPIResponse;
        }
    }

    public static final class RecipeListSuccess extends Result {
        private final List<Recipe> recipes;
        public RecipeListSuccess(List<Recipe> recipes) {
            this.recipes = recipes;
        }
        public List<Recipe> getData() {
            return recipes;
        }
    }

    public static final class UserSuccess extends Result {
        private final User user;
        public UserSuccess(User user) {
            this.user = user;
        }
        public User getData() {
            return user;
        }
    }

    public static final class Success extends Result {
        private final String message;
        public Success() { this.message = null; }
        public Success(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }

    public static final class Error extends Result {
        private final String message;
        public Error(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }
}
