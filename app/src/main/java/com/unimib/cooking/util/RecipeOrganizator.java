package com.unimib.cooking.util;

import com.unimib.cooking.model.Recipe;

import java.util.ArrayList;

public class RecipeOrganizator {

    public static ArrayList<String> getIngredients(Recipe recipe){
        ArrayList <String> ingredients = new ArrayList<>();

        String[] recipeIngredients = {
                recipe.getStrIngredient1(), recipe.getStrIngredient2(), recipe.getStrIngredient3(),
                recipe.getStrIngredient4(), recipe.getStrIngredient5(), recipe.getStrIngredient6(),
                recipe.getStrIngredient7(), recipe.getStrIngredient8(), recipe.getStrIngredient9(),
                recipe.getStrIngredient10(), recipe.getStrIngredient11(), recipe.getStrIngredient12(),
                recipe.getStrIngredient13(), recipe.getStrIngredient14(), recipe.getStrIngredient15(),
                recipe.getStrIngredient16(), recipe.getStrIngredient17(), recipe.getStrIngredient18(),
                recipe.getStrIngredient19(), recipe.getStrIngredient20()
        };

        for (String ingredient : recipeIngredients) {
            if (ingredient != null) {
                ingredients.add(ingredient);
            }
        }

        return ingredients;

    }

    public static String getIngredientsString(Recipe recipe){
        ArrayList <String> ingredients = RecipeOrganizator.getIngredients(recipe);

        String ingredientiString = "";

        for (int i = 0; i < ingredients.size(); i++) {
            ingredientiString = ingredientiString + ingredients.get(i) + "\n";
        }

        return ingredientiString;
    }

    public static String getMeasuresString(Recipe recipe){
        ArrayList <String> measures = RecipeOrganizator.getMeasures(recipe);

        String measuresString = "";

        for (int i = 0; i < measures.size(); i++) {
            measuresString = measuresString + measures.get(i) + "\n";
        }

        return measuresString;
    }

    public static ArrayList<String> getMeasures(Recipe recipe){
        ArrayList <String> measures = new ArrayList<>();

        String[] recipeMeasures = {
                recipe.getStrMeasure1(), recipe.getStrMeasure2(), recipe.getStrMeasure3(),
                recipe.getStrMeasure4(), recipe.getStrMeasure5(), recipe.getStrMeasure6(),
                recipe.getStrMeasure7(), recipe.getStrMeasure8(), recipe.getStrMeasure9(),
                recipe.getStrMeasure10(), recipe.getStrMeasure11(), recipe.getStrMeasure12(),
                recipe.getStrMeasure13(), recipe.getStrMeasure14(), recipe.getStrMeasure15(),
                recipe.getStrMeasure16(), recipe.getStrMeasure17(), recipe.getStrMeasure18(),
                recipe.getStrMeasure19(), recipe.getStrMeasure20()
        };

        for (String measure : recipeMeasures) {
            if (measure != null) {
                measures.add(measure);
            }
        }

        return measures;

    }

}
