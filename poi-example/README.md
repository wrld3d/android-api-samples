# Poi View Example

## Requirements

* [Android Studio](https://developer.android.com/studio/archive) (3.0+ tested)
* Gradle: (4.4, set within the Project tab of the Project Structure window inside Android Studio)
* [Android SDK Tools](https://developer.android.com/studio/releases/sdk-tools): (25.2.3.1 or later)
* [Android NDK](https://developer.android.com/ndk/downloads/older_releases): (>= 11c & < 18)

## Getting Set up

### Obtaining an API Key

1. Sign in to your WRLD account [here](https://www.wrld3d.com/).

2. Navigate to your [account page](https://accounts.wrld3d.com/users/sign_in?service=https%3A%2F%2Faccounts.wrld3d.com%2F%23apikeys).

3. Here you can copy the API key of your app, and create a new app if you don't already have one.

### Importing POIs from a CSV

1. Go to [Places Designer](https://mapdesigner.wrld3d.com/poi/latest/).

2. Select 'Upload New Collection'. Drag the poi-examples.csv provided, or your own .csv, into the upload box.

3. Click the edit button on your POI collection, click 'Add An App' and select the app you want to associate it with.

4. The POIs are now ready to be viewed in your app!

#### Tip
You can edit and preview the Pois you have imported here in the Places Designer by selecting them and clicking the tabs on the right side panel.

### Setting up the example

1. Clone this repository if you have not already.

2. Open the gradle project in Android Studio.

3. Set your API key [here](https://github.com/wrld3d/android-api-samples/blob/poi-example/poi-example/app/src/main/java/com/example/androidpoiexample/MainActivity.java#L35). Make sure it is the same as the one attached to your POI collection in the "Importing POIs from a CSV" section, step 3.
4. Click run!

### Types of POIs

In this example there are two types of POIs. The first is a standard POI uses the default layout and customization option offered in Places Designer.

The second is a custom html POI. This type of POI is created by setting 'custom_view', within the 'user_data' field of a poi, to a custom html view. You can read more about the 'user_data' fields [here](https://github.com/wrld3d/wrld-poi-api#points-of-interest) under Points of Interest.
