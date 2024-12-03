# FoodFinder App

FoodFinder is a versatile mobile application designed to help users manage their pantry, explore recipes, and maintain a shopping list. The app integrates with the Spoonacular API to fetch recipe recommendations based on the ingredients you have and supports allergy alerts, voice recognition, and intuitive navigation.

## Features

### 1. Pantry Management
- Add, edit, or delete pantry items.
- Save and load pantry items from a local file for persistent storage.
- Easily view your pantry contents with a user-friendly interface.

### 2. Recipe Finder
- Search for recipes using ingredients from your pantry.
- Provides detailed recipe information, including:
  - Ingredients
  - Instructions
  - Nutrition facts
- Allergy alerts highlight recipes containing potential allergens.

### 3. Voice Recognition
- Use the microphone to add items to your pantry via voice commands.
- Powered by Android's SpeechRecognizer for free-form speech input.

### 4. Shopping List
- Maintain a shopping list for items you need to buy.
- Easily navigate between pantry and shopping list functionalities.

### 5. Favorites
- Save your favorite recipes for quick access later.
- View detailed information about saved recipes.

### 6. Allergies List
- Easily add allergies to a persistent list stored on the device
- Allergy alerts are included in recipe details.

## Getting Started

### Prerequisites
- Android Studio (latest version recommended)
- An Android device or emulator
- A valid API key from Spoonacular

### Setup
1. Clone this repository:
   ```bash
   git clone https://github.com/yourusername/foodfinder.git
2. Open the project in Android Studio.
3. Add your Spoonacular API credentials:
	- Open gradle.properties and add:
   ```bash
  	API_KEY=your_api_key
	API_HOST=spoonacular-recipe-food-nutrition-v1.p.rapidapi.com
4. Build and run the app on your device or emulator.
   
### Usage
- Add Items to Pantry:
	- Use the "Add" button to manually input items.
	- Tap the microphone icon to add items via voice.
   
### Search Recipes:
- Tap "Search Parameters" to configure your preferences and find recipes.
- Navigate Between Features:
	- Use the navigation buttons for Pantry, Home, or Shopping List.
   
### Favorite Recipes:
- Double-tap or tap the heart icon on a recipe to save it to favorites.

### Technical Details
- Languages: Kotlin
- Frameworks: Android SDK
- API Integration: Spoonacular API
- Data Storage: Local file storage using Android's filesDir
- UI Framework: ConstraintLayout, ListView, and Custom Adapters
  
### Known Issues
#### Speech Recognition Errors:
- Ensure the app has the necessary microphone permissions.
- Some devices may require additional configuration for speech services.
  
#### Performance Lag:
- Rendering delays may occur on older devices when loading large data sets.

#### Recipe information:
- Ingredient quantities not shown on recipe cards

#### Recipe Details API Returns Null for Instructions
- The recipe details search API occasionally returns null values for the instructions field. This behavior is likely due to regional variations in recipe names, where the API might fail to locate the exact recipe name in its database

#### Nutrition Data API Returns Null for Protein Values
- The nutrition data API sometimes returns null values for the protein field. This occurs when the database lacks calculated or available protein values for certain recipes.

---

## Release Notes - FoodFinder App
### Version Highlights
- Expanded user sharing and shopping list functionalities.
- Introduced location-based grocery store notifications.
- Enhanced pantry management, allergy detection, and user interface for a better experience.

### New Features:
#### 1. Added functionality for users to manage a shopping list.
#### 2. Updated logo and color scheme for a fresh and consistent look.
#### 3. Recipe Sharing
- Users can now share recipes with friends.

#### 4. Home Screen
- New centralized home screen with buttons for:
		- Adding food
		- Food preferences
		- Pantry
		- Favorite recipes
  
#### 5. Pantry Management:
- Introduced a pantry activity with persistent item storage on the device's internal storage.
	- Users can now:
		- Add items via the Search Parameters button on the main activity.
		- Remove items directly within the pantry.
### Improvements
#### 1. Allergy Management
- Resolved allergy detection issues; alerts now correctly handle singular and plural forms.
- Added a button to manage or remove allergies from the list.

#### 2. Favorites
- Fixed favorites functionality so recipes are added properly when the heart icon is tapped.
- Removed redundant API calls in the Favorites Activity to optimize request usage.
- Cached recipe data for faster access and reduced API usage.

#### 3. User Interface
- Redesigned card layouts for better display of longer recipe descriptions.
- Ensured button text is hardcoded to white for better visibility.
- Updated the pantry activity to include a Home button for easier navigation.

#### 4. Security Enhancements
- Removed hardcoded API keys and host details for improved security and maintainability.

#### 5. Adaptability
- Introduced a generic ItemAdapter class for better management of mutable lists and file-based storage.
### Bug Fixes
- Fixed the "Search Parameters" workflow to ensure items are correctly added to the pantry.
- Resolved issues with long recipe descriptions causing layout problems.
- Addressed a UI glitch in the Favorites Activity where API calls were inefficiently handled.

### Known Issues
#### Proximity Notifications:
- Grocery store locations are hard-coded. This feature may not work for all users.
	- Future updates will explore integrating a free location API for dynamic store detection.
### Upcoming Enhancements
#### Shopping List
- Integrated shopping list notifications for nearby grocery stores (see Proximity-Based Grocery Notifications).
	- Proximity-Based Grocery Notifications
	- Further refine grocery notifications for greater accuracy and user customization.
	- Notifies users when they are near a grocery store, suggesting items from their shopping list.
Note: Grocery store locations are hard-coded due to the lack of a free location API. Users can disable this feature by rolling back to a previous commit.
- Explore additional API integrations for enhanced location services and recipe features.
