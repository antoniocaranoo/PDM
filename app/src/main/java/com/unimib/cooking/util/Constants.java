package com.unimib.cooking.util;

import android.content.Context;

import com.unimib.cooking.R;
import com.unimib.cooking.model.Category;
import com.unimib.cooking.model.Country;

import java.util.ArrayList;
import java.util.List;

public class Constants {
    public static final int DATABASE_VERSION = 1;

    public static final int MINIMUM_LENGTH_PASSWORD = 8;
    public static final String SAVED_RECIPES_DATABASE = "saved_datab";
    public static final String SEARCH_ENDPOINT = "search.php";
    public static final String FILTER_ENDPOINT = "filter.php";
    public static final String RANDOM_ENDPOINT = "random.php";
    public static final String ID_ENDPOINT = "lookup.php";

    public static final String REMOVED_RECIPE_STR_MEAL = "[Removed]";

    public static final String RECIPES_API_BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    public static final int FRESH_TIMEOUT = 1000 * 60;
    public static final long RANDOM_REFRESH = 24 * 60 * 60 * 1000;

    public static final String SHARED_PREFERENCES_FILENAME = "com.unimib.cooking.preferences";
    public static final String SHARED_PREFERNECES_LAST_UPDATE = "last_update";

    public static final String BUNDLE_KEY_CURRENT_RECIPE = "current_article";

    public static final String INVALID_USER_ERROR = "invalidUserError";
    public static final String INVALID_CREDENTIALS_ERROR = "invalidCredentials";
    public static final String USER_COLLISION_ERROR = "userCollisionError";
    public static final String WEAK_PASSWORD_ERROR = "passwordIsWeak";

    public static final String FIREBASE_USERS_COLLECTION = "users";
    public static final String FIREBASE_FAVORITE_RECIPES_COLLECTION = "favoriteRecipes";
    public static final String UNEXPECTED_ERROR = "unexpected_error";

    public static final String SHARED_PREFERENCES_SELECTED_FILTER = "selected_filter";

    public static List<Country> generateCountryList(Context context) {
        List<Country> countries = new ArrayList<>();

        countries.add(new Country("American", R.drawable.ic_usa));
        countries.add(new Country("British", R.drawable.ic_britainn));
        countries.add(new Country("Canadian", R.drawable.ic_canadian));
        countries.add(new Country("Chinese", R.drawable.ic_china));
        countries.add(new Country("Croatian", R.drawable.ic_croatian));
        countries.add(new Country("Dutch", R.drawable.ic_netherlands));
        countries.add(new Country("Egyptian", R.drawable.ic_egyptian));
        countries.add(new Country("Filipino", R.drawable.ic_filippine));
        countries.add(new Country("French", R.drawable.ic_france));
        countries.add(new Country("Greek", R.drawable.ic_greece));
        countries.add(new Country("Indian", R.drawable.ic_india));
        countries.add(new Country("Irish", R.drawable.ic_ireland));
        countries.add(new Country("Italian", R.drawable.ic_italian));
        countries.add(new Country("Jamaican", R.drawable.ic_jamaican));
        countries.add(new Country("Japanese", R.drawable.ic_japanese));
        countries.add(new Country("Kenyan", R.drawable.ic_kenya));
        countries.add(new Country("Malaysian", R.drawable.ic_malesyan));
        countries.add(new Country("Mexican", R.drawable.ic_mexico));
        countries.add(new Country("Moroccan", R.drawable.ic_marocco));
        countries.add(new Country("Polish", R.drawable.ic_polonia));
        countries.add(new Country("Portuguese", R.drawable.ic_portugese));
        countries.add(new Country("Russian", R.drawable.ic_russia));
        countries.add(new Country("Spanish", R.drawable.ic_spain));
        countries.add(new Country("Thai", R.drawable.ic_thay));
        countries.add(new Country("Tunisian", R.drawable.ic_tunisian));
        countries.add(new Country("Turkish", R.drawable.ic_turkish));
        countries.add(new Country("Ukrainian", R.drawable.ic_ucrenian));
        countries.add(new Country("Vietnamese", R.drawable.ic_vietnam));
        countries.add(new Country("Unknown", R.drawable.ic_origin));

        return countries;
    }

    public static List<Category> generateCategoryList(Context context) {
        List<Category> categories = new ArrayList<>();

        categories.add(new Category("Beef", R.drawable.ic_beef));
        categories.add(new Category("Breakfast", R.drawable.ic_breakfast));
        categories.add(new Category("Chicken", R.drawable.ic_chicken));
        categories.add(new Category("Dessert", R.drawable.ic_dessert));
        categories.add(new Category("Goat", R.drawable.ic_goat));
        categories.add(new Category("Lamb", R.drawable.ic_lamb));
        categories.add(new Category("Miscellaneous", R.drawable.ic_miscellaneous));
        categories.add(new Category("Pasta", R.drawable.ic_pasta));
        categories.add(new Category("Pork", R.drawable.ic_pork));
        categories.add(new Category("Seafood", R.drawable.ic_seafood));
        categories.add(new Category("Side", R.drawable.ic_side_dish));
        categories.add(new Category("Starter", R.drawable.ic_starter));
        categories.add(new Category("Vegan", R.drawable.ic_vegan));
        categories.add(new Category("Vegetarian", R.drawable.ic_vegetarian));

        return categories;
    }

}
