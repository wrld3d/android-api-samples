# WRLD Android Java API Samples
Samples demonstrating how to use the [WRLD Android Java Api](https://docs.wrld3d.com/android/latest/docs/api/) to display beautiful, interactive 3D maps on Android devices.

## Building and running the samples
### Prerequisites
* [Android Studio](https://developer.android.com/studio/index.html), together with the Android SDK and supporting libraries appropriate for your target platform.
* For running on Android devices, the library requires Android 4.0.3 (API level 15) or higher.

### <a name="_setup1"></a>Setup
1. Clone or downloaded snapshot of this repo.
2. Sign up [here](https://www.wrld3d.com/register/) for a free WRLD developer account.
3. Navigate to [API Keys](https://accounts.wrld3d.com/#apikeys) and create API keys for the example apps in this repository:
  * In the "New App" edit box enter "HelloWorld" and select "Create API Key".
  * In the "New App" edit box enter "ApiSamples" and select "Create API Key".
  * In the "New App" edit box enter "PoiExample" and select "Create API Key".  
    An API key is a string consisting of 32 alphanumeric characters. You will need to cut and paste each of the keys into a resource file in order to authenticate your apps. This is described in the ['Set your WRLD API key'](#_setApiKey1) step below.

4. Open Android Studio.
5. Select "Open an existing Android Studio project" and select the top level android-api-samples folder.

   If Android Studio is missing supporting libraries or components, you may be prompted to install these. Otherwise, Android Studio will download prerequisites automatically from JCenter.

You should now see three modules in the project:
* [helloworld](https://github.com/wrld3d/android-api-samples/tree/master/helloworld)
* [apisamples](https://github.com/wrld3d/android-api-samples/tree/master/apisamples)
* [poi-example](https://github.com/wrld3d/android-api-samples/tree/master/poi-example)

### HelloWorld
The [helloworld](https://github.com/wrld3d/android-api-samples/tree/master/helloworld) example is a basic app displaying an WRLD map. It demonstates how to integrate WRLD maps into your existing Android application.

#### <a name="_setApiKey1"></a>Set your WRLD API key
Edit the [```strings.xml```](https://github.com/wrld3d/android-api-samples/blob/master/helloworld/src/main/res/values/strings.xml#L4) file in the helloworld module. Paste in the 32 character API key for "HelloWorld" that you created in ['Setup'](#_setup1) as the value for the ```eegeo_api_key``` resource.

#### Build and run
To build it, select "helloworld" in the "Run/Debug configuration" toolbar and click "Make Project". If you have a debuggable Android device attached, or if you are running an emulator, you can debug or run the app with the standard Android Studio tools.

For a step-by-step instructions which show how to create a basic app from scratch, see [our walkthrough documentation](https://docs.wrld3d.com/android/latest/docs/api/Walkthrough/).

### ApiSamples
The [apisamples](https://github.com/wrld3d/android-api-samples/tree/master/apisamples) app contains a collection of Activities, each illustrating an individual API feature.

#### Set your WRLD API key
Edit the [```strings.xml```](https://github.com/wrld3d/android-api-samples/blob/master/apisamples/src/main/res/values/strings.xml#L4) file in the apisamples module. Paste in the 32 character API key for "ApiSamples" that you created in ['Setup'](#_setup1) as the value for the ```eegeo_api_key``` resource.

#### Build and run
To build the examples, ensure "apisamples" is selected in the "Run/Debug configuration" toolbar, then "Run->Run 'apisamples'".

### PoiExample
The [poi-example](https://github.com/wrld3d/android-api-samples/tree/master/poi-example) app demonstates how to display Point Of Interest information from a Places Collection created using the [WRLD Places Designer](https://mapdesigner.wrld3d.com/poi/latest/).

#### Set your WRLD API key
Edit the [```strings.xml```](https://github.com/wrld3d/android-api-samples/blob/master/poi-example/src/main/res/values/strings.xml#L4) file in the poi-example module. Paste in the 32 character API key for "PoiExample" that you created in ['Setup'](#_setup1) as the value for the ```wrld_api_key``` resource.

#### Create a Places Collection
1. Go to [Places Designer](https://mapdesigner.wrld3d.com/poi/latest/).
2. To get you started, import the example Places provided in [poi-examples.csv](https://github.com/wrld3d/android-api-samples/tree/master/poi-example/poi-examples.csv). Select 'Upload New Collection' and drag file poi-examples.csv into the upload box.
3. You will now see a new Places Collection called "poi-examples" in the Places Designer sidebar. Click the pencil icon to open "Manage Place Collection" dialog.
4. Click "+ Add An App". In the "Select App Key..." dropdown, select the "PoiExample" key that you created in ['Setup'](#_setup1).
5. Click "Confirm Changes".

#### Build and run the app
To build the examples, ensure "poi-example" is selected in the Android Studio "Run/Debug configuration" toolbar, then "Run->Run 'poi-example'".

#### Types of Places
The example Places provided in [poi-examples.csv](https://github.com/wrld3d/android-api-samples/tree/master/poi-example/poi-examples.csv) demonstrate display using two types of Views.
* "Test POI 1" demonstrates a default View that displays each of the standard Place fields editable in Places Designer.
* "Test POI 2" demonstrates displaying custom HTML content. This Place has its 'user_data' -> 'custom_view' field populated with a web page url. You can read more about the 'user_data' fields in the [WRLD POI API documentation](https://github.com/wrld3d/wrld-poi-api#points-of-interest).

#### Places Designer Tip
In [Places Designer](https://mapdesigner.wrld3d.com/poi/latest/) you can edit and preview a Place by selecting it to open the right-hand side panel.

## Further information
See our [API docs](https://docs.wrld3d.com/android/latest/docs/api/) for detailed class documentation and other information.

Questions, comments, or problems? All feedback is welcome -- just [create an issue](https://github.com/wrld3d/android-api-samples/issues).

## WRLD Android SDK package
These samples make use of the WRLD Android SDK, available as a Maven package via [Bintray](https://bintray.com/wrld/maven/wrld-android-sdk). 

[ ![Download](https://api.bintray.com/packages/wrld/maven/wrld-android-sdk/images/download.svg) ](https://bintray.com/wrld/maven/wrld-android-sdk/_latestVersion)

The gradle scripts included in this samples repo mean that the package is downloaded automatically by Android Studio, via JCenter.

The source for the SDK is available at https://github.com/wrld3d/android-api

## License
These examples are released under the Simplified BSD License. See the [LICENSE.md](https://github.com/wrld3d/android-api-samples/blob/master/LICENSE.md) file for details.
