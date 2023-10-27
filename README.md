# react-native-image-detective
![cover](https://github.com/rbayuokt/react-native-image-detective/blob/main/demo/cover.png?raw=true)<br/><br/>
<b>✨ ALL IN ONE, ALL IN JUST ONE LINE ✨</b>

late-night brain sessions often lead to interesting questions. lately, I've been pondering:<br />
"can React Native and the wild world of AI/ML join forces and create some magic? yes, they totally can!" 😎
<br /><br />
not one to let dreams stay as dreams, I dived into research mode. the burning question led me to a fascinating project where React Native and AI/ML got cozy.
<br /><br />
<b>why all this fuss? well, I'm on a mission to shake things up in the React Native community. forget pointing fingers, let's make it better together. this journey might be a baby step, but hey, even small steps can leave giant footprints.</b>
<br /><br />
powered by Google's MLKit, I whipped up native modules for Android (using Java), iOS (using Objective C) and TypeScript 🚀✨

# Features
well, I'm striving to make this library easy to integrate and user-friendly with minimal effort, so let's get started! <br />

1. Face Detection
2. Barcode Scanner
3. ... WIP

## Installation

```sh
npm install react-native-image-detective
or
yarn add react-native-image-detective

npx pod-install
```

## Usage 
select a picture from either your camera or device using another library of your choice. in this example folder<br />I've used `react-native-image-picker`

1. <b>Face Detection</b><br /><br/>
![face-detection-demo](https://github.com/rbayuokt/react-native-image-detective/blob/main/demo/facedetector.gif?raw=true)

```js
import ImageDetective from 'react-native-image-detective';

// ... blablababla

const onImageChanges = async (res: ImagePickerResponse) => {
    try {
      if (!res.assets || !res.assets[0]?.uri) {
        return;
      }

      const imagePath = res.assets[0].uri;
      // MAIN CODE
      const image = await ImageDetective.analyzeFace(imagePath);
      // END OF MAIN CODE

      console.log('[Image response] :', image.faces);

      if (image.isValid) {
        Alert.alert(
          'Face detected',
          'Hoorayy !!'
        );
      } else {
        Alert.alert(
          'Face not detected',
          'Please reupload an image with your face in it.'
        );
      }
    } catch (error) {
      console.log('error', error);
    }
  };
```
2. <b>Barcode Scanner</b><br/><br/>
![barcode-scanner-demo](https://github.com/rbayuokt/react-native-image-detective/blob/main/demo/barcode.gif?raw=true)<br />
supports the following formats: <br />Code-128, 39, 93, Codabar, Data Matrix, EAN-13, EAN-8, ITF, QR Code, UPC-A, UPC-E, PDF-417, and Aztec Code.

```js
import ImageDetective from 'react-native-image-detective';

// ... blablababla

  const onBarcodeScan = async (res: ImagePickerResponse) => {
    try {
      if (!res.assets || !res.assets[0]?.uri) {
        return;
      }

      const imagePath = res.assets[0].uri;
      // MAIN CODE
      const barcode = await ImageDetective.analyzeBarcode(imagePath);
      // END OF MAIN CODE
      console.log('[barcode response] :', JSON.stringify(barcode));

      if (barcode.isValid) {
        Alert.alert('barcode result', JSON.stringify(barcode));
      } else {
        Alert.alert('Barcode not detected', 'Please reupload a barcode in it.');
      }
    } catch (error) {
      console.log('error', error);
    }
  };
```

3. WIP
```
WIP
```

## Contributing
Please help me make this library both fun and useful to use. Share your ideas about what AI/ML features could be on the list for the next version!

## License

MIT

---

Made with ❤️ by @rbayuokt, thanks to [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
