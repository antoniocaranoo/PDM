package com.unimib.cooking.ui.homePage.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.unimib.cooking.R;
import com.unimib.cooking.model.Recipe;
import com.unimib.cooking.model.RecipeAPIResponse;
import com.unimib.cooking.model.Result;
import com.unimib.cooking.repository.recipe.RecipeRepository;
import com.unimib.cooking.repository.user.IUserRepository;
import com.unimib.cooking.ui.homePage.viewmodel.RecipeViewModel;
import com.unimib.cooking.ui.homePage.viewmodel.RecipeViewModelFactory;
import com.unimib.cooking.ui.welcome.activity.WelcomeActivity;
import com.unimib.cooking.ui.welcome.viewmodel.UserViewModel;
import com.unimib.cooking.ui.welcome.viewmodel.UserViewModelFactory;
import com.unimib.cooking.util.ServiceLocator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private ImageView profileImage;
    private TextView profileName;
    private TextView profileLocation;
    private Button deleteProfileButton;
    private Button deleteFavouritesButton;
    private Button logoutButton;
    private Button exportFavouritesButton;
    private RecipeViewModel recipeViewModel;
    private UserViewModel userViewModel;
    private List<Recipe> recipes;
    private SwitchMaterial darkModeSwitch;
    private boolean darkMODE;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        profileImage = rootView.findViewById(R.id.profile_image);
        profileName = rootView.findViewById(R.id.profile_name);
        profileLocation = rootView.findViewById(R.id.profile_location);
        deleteProfileButton = rootView.findViewById(R.id.delete_profile_button);
        deleteFavouritesButton = rootView.findViewById(R.id.delete_favourites_button);
        darkModeSwitch = rootView.findViewById(R.id.dark_mode_switch);
        logoutButton = rootView.findViewById(R.id.logout_button);
        exportFavouritesButton = rootView.findViewById(R.id.export_favourites_button);


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            profileName.setText(currentUser.getDisplayName());
            profileLocation.setText(currentUser.getEmail());
        }


        sharedPreferences = requireContext().getSharedPreferences("MODE", Context.MODE_PRIVATE);
        darkMODE = sharedPreferences.getBoolean("dark_mode", false);

        if (darkMODE) {
            darkModeSwitch.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        darkModeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (darkMODE) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("dark_mode", false);

                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("dark_mode", true);
                }
                editor.apply();
            }
        });

        logoutButton.setOnClickListener(v -> {
            deleteLocalFavourites(false);
            logoutUser();
        });

        exportFavouritesButton.setOnClickListener(v -> exportFavourites());

        deleteProfileButton.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                .setTitle(R.string.conferma_eliminazione)
                .setMessage(R.string.sicura_eliminazione)
                .setPositiveButton(R.string.elimina_conferma, (dialog, which) -> {
                    deleteProfile();
                    deleteLocalFavourites(true);
                })
                .setNegativeButton(R.string.annulla, null)
                .show();
        });

        deleteFavouritesButton.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle(R.string.conferma_eliminazione)
                    .setMessage(R.string.sicura_eliminazione_preferiti)
                    .setPositiveButton(R.string.elimina_conferma, (dialog, which) -> {
                        deleteFavorites();
                    })
                    .setNegativeButton(R.string.annulla, null)
                    .show();
        });

        return rootView;
    }

    private void deleteProfile() {
        userViewModel.deleteProfile().observe(getViewLifecycleOwner(), deleteProfileResult -> {
            if (deleteProfileResult.isSuccess()) {
                userViewModel.deleteUser().observe(getViewLifecycleOwner(), deleteUserResult -> {
                    if (deleteUserResult.isSuccess()) {
                        Snackbar.make(requireView(), R.string.profilo_eliminato_con_successo, Snackbar.LENGTH_SHORT).show();
                        requireActivity().finish();
                    } else {
                        Snackbar.make(requireView(), R.string.errore_eliminazione_del_profilo, Snackbar.LENGTH_SHORT).show();
                    }
                });
            } else {
                Snackbar.make(requireView(), R.string.errore_eliminazione_del_profilo, Snackbar.LENGTH_SHORT).show();
            }
        });

    }

    private void deleteFavorites() {
        userViewModel.removeAllFavorites().observe(getViewLifecycleOwner(), result -> {
            if (result.isSuccess()) {
                deleteLocalFavourites(true);
            } else {
                Snackbar.make(requireView(), R.string.errore_eliminazione_preferiti, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteLocalFavourites(boolean showAlert) {
        recipeViewModel.getFavoriteRecipes().observe(getViewLifecycleOwner(), result -> {
            if (result.isSuccess()) {
                RecipeAPIResponse favoriteRecipes = ((Result.RecipeSuccess) result).getData();
                for (Recipe recipe : favoriteRecipes.getRecipes()) {
                    recipeViewModel.removeFromFavorites(recipe);
                }

                if (showAlert) {
                    Snackbar.make(requireView(), R.string.preferiti_eliminate_con_successo, Snackbar.LENGTH_SHORT).show();
                }
            } else if (showAlert) {
                Snackbar.make(requireView(), R.string.errore_eliminazione_preferiti, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void logoutUser() {
        userViewModel.logout().observe(getViewLifecycleOwner(), result -> {
            if (result.isSuccess()) {
                Intent intent = new Intent(requireActivity(), WelcomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                requireActivity().finish();
            } else {
                Snackbar.make(requireView(), R.string.errore_logout, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void exportFavourites() {
        recipeViewModel.getFavoriteRecipes().observe(getViewLifecycleOwner(), result -> {
            if (result.isSuccess()) {
                List<Recipe> favoriteRecipes = ((Result.RecipeSuccess) result).getData().getRecipes(); //CONTROLLARE QUI

                if (favoriteRecipes.isEmpty()) {
                    Snackbar.make(requireView(), R.string.nessuna_ricetta_preferita_da_esportare, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                String fileName = "ricette_preferite.json";
                File file = new File(requireContext().getExternalFilesDir(null), fileName);

                try (FileWriter writer = new FileWriter(file)) {
                    Gson gson = new Gson();
                    writer.write(gson.toJson(favoriteRecipes));

                    Snackbar.make(requireView(), R.string.esportazione_completata, Snackbar.LENGTH_SHORT).show();

                    shareFile(file);
                } catch (IOException e) {
                    Snackbar.make(requireView(), R.string.errore_durante_l_esportazione, Snackbar.LENGTH_SHORT).show();

                }
            } else {
                Snackbar.make(requireView(), R.string.errore_nel_recupero_delle_ricette, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void shareFile(File file) {
        Uri uri = FileProvider.getUriForFile(requireContext(), "com.unimib.cooking.fileprovider", file);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("application/json");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(Intent.createChooser(shareIntent, getString(R.string.condividi_le_tue_ricette_preferite)));
    }

}
