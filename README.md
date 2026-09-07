# CooKing

## Introduction

### CooKing: Your Culinary Guide at Your Fingertips

**CooKing** is an Android mobile application that allows users to explore a wide variety of recipes from different cultures and culinary categories.

With CooKing, users can:

- Explore a wide range of recipes available in the application.
- Search for recipes based on their category or geographical area.
- Save their favorite recipes in a dedicated section.
- Follow detailed tutorials to easily prepare each dish.

---

## Why CooKing?

The idea behind CooKing was born from the need to provide practical support to university students living away from home, who often struggle to find ideas for their daily meals.

With CooKing, preparing tasty dishes becomes simple and enjoyable, allowing users to experiment with new recipes and improve their cooking skills.

However, CooKing is not only designed for students. The application is suitable for anyone who wants to discover new flavors, diversify their diet, and learn cooking techniques from around the world.

Whether you are a beginner or a cooking enthusiast, CooKing provides inspiration for discovering and preparing new dishes.

---

## Technologies

CooKing was developed using modern technologies and reliable tools to provide a smooth and intuitive user experience.

- **Android Studio** — The official Android development environment, used to develop the application and its features.
- **GitHub** — Used for version control, source code management, and collaboration during development.
- **Firebase** — Google's cloud development platform, used for user authentication, database management, and real-time services.
- **TheMealDB** — A food-focused API providing a large database of recipes, images, ingredients, and information about dishes from different cultures.

---

# Design

## Design Process

The application was designed with the goal of providing a simple, intuitive, and accessible interface that allows users to easily browse recipes and access the main features of the application.

The interface focuses on clear navigation and visual presentation of recipes, making extensive use of cards, images, and familiar Android UI components.

---

## Material Design

The CooKing interface follows the **Material Design 3** guidelines, using and adapting Material Components such as **Buttons** and **Cards** to provide a consistent and intuitive user experience.

Thanks to the Material Design color system, the application supports two display modes:

- **Light Mode**
- **Dark Mode**

Users can switch between the two themes according to their preferences and usage conditions.

---

# Architecture

The Android application architecture follows modern development principles to ensure scalability, maintainability, and a clear separation of responsibilities.

## Single Source of Truth

The **Single Source of Truth** principle ensures that application data is managed centrally and accessed consistently by the different components of the application.

This approach reduces the possibility of data inconsistencies and simplifies synchronization between the different architectural layers.

## Modularization

The project adopts modularization to divide the application into independent components.

This approach facilitates:

- Parallel development.
- Code reuse.
- Dependency management.
- Improved maintainability.
- Reduced compilation times.

Each module has a clearly defined responsibility within the application.

## Single Activity - Multiple Fragments

The application architecture is based on the **Single Activity - Multiple Fragments** pattern, providing smooth navigation and centralized management of the application state.

This model also improves lifecycle management and simplifies integration with Android Jetpack navigation components.

The application uses two main activities:

- **Welcome Activity** — Contains the fragments related to authentication.
- **Main Activity** — Contains the fragments related to recipes and the main application features.

## MVVM

The project follows the **MVVM (Model-View-ViewModel)** architectural pattern.

### Model

The Model manages data access, business data, and persistence.

### ViewModel

The ViewModel acts as an intermediary between the View and the Model. It provides data to the UI and preserves the state of the interface.

### View

The View is responsible for displaying information and handling user interaction.

This architecture, together with Android Jetpack components, makes the application more robust, testable, maintainable, and easier to extend.

---

# Libraries

## Gson

**Gson** is a Java library developed by Google for serializing and deserializing Java objects to and from JSON.

In CooKing, it is used to convert JSON data received from APIs into Java objects.

## Firebase

**Firebase** is Google's mobile development platform and provides several cloud-based services.

CooKing uses Firebase for:

- **Authentication** — Secure management of user authentication through email and password or external providers such as Google.
- **Firestore** — Storage and synchronization of application and user-related data.

## Retrofit

**Retrofit** is an Android library that simplifies communication with REST APIs by providing an efficient and structured way to perform HTTP requests.

It provides a declarative interface for defining API calls using annotations such as:

- `GET`
- `POST`
- `PUT`
- `DELETE`

Retrofit also supports automatic conversion of API responses into Java objects through customizable converters such as Gson.

## Glide

**Glide** is a library for efficient image loading and management.

It supports:

- Loading images from URLs.
- Automatic image caching.
- Resource optimization.
- Efficient image handling on devices with limited resources.

In CooKing, Glide is primarily used to display recipe images retrieved from external sources.

## Room

**Room** is a persistence library that provides an abstraction layer over SQLite, simplifying the management of local databases.

It allows application data to be stored efficiently on the device and provides structured access to locally persisted information.

---

# Features

## Authentication

The CooKing authentication screen provides three different authentication methods:

- **Login** — For users who already have an account.
- **Sign Up** — For users who want to create a new account.
- **Google Sign-In** — Allows users to create an account using their Google profile or directly log in if the account already exists.

If the user has already logged in on the device, the application automatically authenticates them using the last account used.

---

## Login

The login screen contains two input fields:

- Email
- Password

Users must enter the credentials associated with their account.

If invalid credentials are provided, the application prevents access.

After a successful login, the user is redirected to the **Home** screen.

---

## Registration

The registration screen contains four input fields required to create a new account:

- First name
- Last name
- Email
- Password

After registration is successfully completed, the account is created and the user is redirected to the **Home** screen.

---

## Home

The Home screen displays the recipes available through the application.

Recipes are retrieved using a filter based on the initial letter of the recipe name due to limitations of the API used by the application.

Each recipe is displayed through a card.

By selecting a recipe card, users can access the recipe details and perform actions such as:

- Sharing the recipe.
- Removing the recipe when applicable.
- Adding the recipe to favorites.

---

## Favorites

The Favorites screen displays all recipes saved by the user.

Users can:

- Open a recipe and view its details.
- Remove a recipe from favorites.
- Share a recipe.
- Search for a specific recipe using the search bar.

The search functionality makes it possible to quickly find recipes within the user's saved favorites.

---

## Search

The Search screen provides different options for discovering recipes.

Users can search for a specific recipe by name.

The application also suggests a **random recipe** to cook. The suggested recipe changes at predefined time intervals, helping users discover new dishes.

---

## Profile

The Profile screen displays the user's:

- First name.
- Last name.
- Email address.

Users can also switch between:

- **Light Mode**
- **Dark Mode**

Additional account management options include:

- Clearing saved favorites.
- Deleting the account.
- Logging out.

---

## Recipe Details

Each recipe contains the following information:

- **Recipe ID** — Unique identifier used to distinguish the recipe. It is not displayed to the user.
- **Dish Name** — Name of the recipe.
- **Category** — Type of dish, such as appetizer, main course, or dessert.
- **Geographical Area** — Country or region associated with the recipe.
- **Instructions** — Detailed preparation steps.
- **Dish Image** — Visual representation of the recipe.
- **Video Tutorial** — Link to a video showing how to prepare the recipe.
- **Ingredients** — List of ingredients required for preparation.
- **Ingredient Quantities** — Required amount of each ingredient.
- **Recipe Source** — External source from which the recipe originates.

---

# Color Palette

The CooKing color palette was selected to create a visual identity associated with food, natural ingredients, and home cooking.

## Natural and Organic Tones

The predominance of **green and brown tones** recalls natural elements such as fresh vegetables, herbs, and organic ingredients.

These colors help communicate a sense of authenticity, freshness, and well-being.

## Authenticity and Tradition

The use of earthy colors creates a warm and welcoming atmosphere associated with home cooking and culinary traditions.

This contributes to a more engaging and familiar user experience.

## Balance Between Modernity and Familiarity

The selected colors balance modernity with familiarity.

The palette avoids excessively saturated colors, maintaining a refined and professional appearance without compromising the simplicity and usability of the interface.

---

# Main Technologies and Libraries

| Technology | Purpose |
|---|---|
| Android Studio | Android application development |
| Java | Application programming language |
| Firebase Authentication | User authentication |
| Cloud Firestore | Cloud data storage |
| TheMealDB | Recipe API |
| Retrofit | REST API communication |
| Gson | JSON serialization and deserialization |
| Glide | Image loading and caching |
| Room | Local data persistence |
| Material Design 3 | UI components and design system |
| GitHub | Version control and collaboration |

