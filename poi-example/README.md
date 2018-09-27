# Poi View Example

## Getting Set up

### Importing Pois from a CSV

1. Log into your WRLD account and select 'Places Designer'.

2. In the Places Designer select 'New Places Collection'. Give it a title and add it to the map you want to associate it with.

3. Select 'Upload New Collection'. Drag the poi-examples.csv provided, or your own .csv, into the upload box.

4. Done!

#### Tip
You can edit and preview the Pois you have imported here in the Places Designer by selecting them and clicking the tabs on the right side panel.

### Setting up the example

1. Clone this repository if you have not already.

2. Open the gradle project in Android Studio.

3. Click run!

### Types of Pois

In this example there are two types of Pois. The first is a standard Poi uses the default layout and customization option offered in Places Designer.

The second is a custom html Poi. This type of Poi is created by setting 'custom_view', within the 'user_data' field of a poi, to a custom html view. You can read more about the 'user_data' fields [here](https://github.com/wrld3d/wrld-poi-api) under Points of Interest.
