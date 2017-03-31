# android-sdk-example
Welcome to the eeGeo Android SDK! This repository contains source code for Android apps which demonstrate the capabilities of our SDK.

Prerequisites
-------------

To use these apps, you'll need:
* [Android Studio](https://developer.android.com/studio/index.html), together with the Android SDK and supporting libraries appropriate for your target platform.
* An [eeGeo API key](https://www.eegeo.com/developers/apikeys/). Sign up [here](https://www.eegeo.com/register/) for a free account and create an API key for your example apps.  The API key is a string consisting of 32 alphanumeric characters.
* A clone or downloaded snapshot of this repo.

Getting started
---------------

In Android Studio, select "Open..." and then select the top level android-sdk-example folder.  If Android Studio is missing supporting libraries or components, you may be prompted to install these. Otherwise, Android Studio will download prerequisites automatically from JCenter.

You should see two modules in the project: [helloworld](https://github.com/eegeo/android-sdk-example/tree/master/helloworld) and [sdkexamples](https://github.com/eegeo/android-sdk-example/tree/master/sdkexamples).

Hello world
-----------

The [helloworld](https://github.com/eegeo/android-sdk-example/tree/master/helloworld) example is a very basic application which displays a map.  

All you need to do is open the ```res/values/strings.xml``` file in the helloworld module and paste in your 32 character API key as the value for the ```eegeo_api_key``` resource.

To build it, select "helloworld" in the "Run/Debug configuration" toolbar and click "Make Project". If you have a debuggable Android device attached, or if you are running an emulator, you can debug or run the app with the standard Android Studio tools.

For a step-by-step instructions which show how to create a basic app from scratch, see [our walkthrough documentation](https://docs.eegeo.com/android/latest/docs/api/Walkthrough/).

SDK Examples
------------
The [sdkexamples](https://github.com/eegeo/android-sdk-example/tree/master/sdkexamples) app shows a selection of SDK features in individual Android Activities.

Again, you will need to open the ```res/values/strings.xml``` file in this sdkexamples and paste in your 32 character API key as the value for the ```eegeo_api_key``` resource.

To build the examples, ensure "sdkexamples" is selected in the "Run/Debug configuration" toolbar and build as usual.

Further information
-------------------
See our [API docs](https://docs.eegeo.com/android/latest/docs/api/) for detailed class documentation and other information.

Questions, comments, or problems? All feedback is welcome -- just [create an issue](https://github.com/eegeo/android-sdk-example/issues).

License
-------

The Android SDK examples are released under the Simplified BSD License. See the [LICENSE.md](https://github.com/eegeo/android-sdk-example/blob/master/LICENSE.md) file for details.