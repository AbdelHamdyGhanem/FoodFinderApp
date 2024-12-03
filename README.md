# FoodFinder App

FoodFinder is a versatile mobile application designed to help users manage their pantry, explore recipes, and maintain a shopping list. The app integrates with the Spoonacular API to fetch recipe recommendations based on the ingredients you have and supports allergy alerts, voice recognition, and intuitive navigation.

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
- Add Items to Pantry
#### Manually Add Items:
- Tap the "Add" button after entering the desired item in the input field to include it in your pantry.
#### Voice Recognition:
- Tap the microphone icon to use voice input for adding items.
- Example: Say "Add milk and eggs" to automatically include these items in your pantry list.
#### Search Recipes
#### Set Preferences:
- Tap the "Search Parameters" button to configure your recipe search preferences:
- Specify the number of recipes.
- Enable or disable options like "Ignore Pantry" and "Maximize Ingredients."
#### View Results:
- After setting preferences, browse through recipes tailored to your pantry and preferences. Each recipe displays:
#### Ingredients.
- Step-by-step instructions.
- Nutrition information.
- Allergy alerts for detected allergens.
### Navigate Between Features
#### Pantry:
- Manage your stored items directly from the Pantry section. Add, edit, or delete items with ease.
#### Home:
- Tap the "Home" button to return to the main screen, where you can access all primary features.
#### Shopping List:
- Maintain a dedicated Shopping List for items not in your pantry. Switch between features effortlessly using navigation buttons.
### Favorite Recipes
#### Save Recipes:
- Double-tap or click the heart icon on a recipe card to mark it as a favorite.
#### Access Favorites:
- View all saved recipes in the Favorites section. Recipes are stored with detailed information for quick access.
### Additional Use Cases
- Send Recipes to Friends:
	- Share recipes directly via supported apps. Tap the "Share" button in a recipe detail view to send the recipe to your friends.
- Get Grocery Notifications:
	- Receive notifications when you're near a grocery store, reminding you to buy items from your shopping list.
(Note: Grocery store locations are hardcoded.)
- Track Allergens:
	- View detailed allergy alerts for recipes. Add or remove allergens in your preferences to refine results further.
- Persistent Storage:
	- Pantry and Shopping List items are saved locally, ensuring data is preserved across app sessions.
- Explore Nutrition:
	- Tap on recipes to explore detailed nutrition facts, helping you plan meals effectively.
### Pro Tips
#### Quickly Clear Pantry Items:
- Use the remove button in the Pantry view to delete unwanted items.
#### Optimize Recipe Searches:
- Use Maximize Ingredients to prioritize recipes that utilize more pantry items.
#### Simplify Lists:
- Use the Shopping List for items you need, separate from your pantry inventory

### Technical Details
- Languages: Kotlin
- Frameworks: Android SDK
- API Integration: Spoonacular API
- Data Storage: Local file storage using Android's filesDir
- UI Framework: ConstraintLayout, ListView, and Custom Adapters

---

