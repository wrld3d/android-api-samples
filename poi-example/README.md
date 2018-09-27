# Poi View Example

## Getting Set up

### Obtaining an API Key

1. Sign in to your WRLD account [here](https://www.wrld3d.com/).

2. Navigate to your [account page](https://accounts.wrld3d.com/users/sign_in?service=https%3A%2F%2Faccounts.wrld3d.com%2F%23apikeys).

3. Here you can copy the API key of your app, and create a new app if you don't already have one.

### Importing POIs from a CSV

1. Log into your WRLD account and select 'Places Designer'.

2. Select 'Upload New Collection'. Drag the poi-examples.csv provided, or your own .csv, into the upload box.

3. Click the edit button on your POI collection, click 'Add An App' and select the app you want to associate it with.

4. Done!

#### Tip
You can edit and preview the Pois you have imported here in the Places Designer by selecting them and clicking the tabs on the right side panel.

### Setting up the example

1. Clone this repository if you have not already.

2. Open the gradle project in Android Studio.

3. Set your api-key [here](https://github.com/wrld3d/android-api-samples/blob/poi-example/poi-example/app/src/main/java/com/example/michaelchan/androidpoiexample/MainActivity.java#L41).
4. Click run!

### Types of Pois

In this example there are two types of Pois. The first is a standard Poi uses the default layout and customization option offered in Places Designer.

The second is a custom html Poi. This type of Poi is created by setting 'custom_view', within the 'user_data' field of a poi, to a custom html view. You can read more about the 'user_data' fields [here](https://github.com/wrld3d/wrld-poi-api) under Points of Interest.
