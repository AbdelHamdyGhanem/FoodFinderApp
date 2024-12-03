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

---

