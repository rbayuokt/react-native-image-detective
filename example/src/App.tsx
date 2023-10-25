import * as React from 'react';

import {
  StyleSheet,
  Alert,
  Text,
  ScrollView,
  SafeAreaView,
} from 'react-native';
import ImageDetective from 'react-native-image-detective';
import UploadImagesCard from './components/UploadImagesCard./UploadImagesCard';
import { useForm } from './utils/useForm';
import type { ImagePickerResponse } from 'react-native-image-picker';
import normalize from './utils/normalize';

export type IUploadImagesForm = {
  imageFace: string;
  imageBarcode: string;
};

export default function App() {
  const { form, inputChangeHandler } = useForm<IUploadImagesForm>({
    imageFace: '',
    imageBarcode: '',
  });

  const onImageChanges = async (res: ImagePickerResponse) => {
    try {
      if (!res.assets || !res.assets[0]?.uri) {
        return;
      }

      const imagePath = res.assets[0].uri;
      const image = await ImageDetective.analyzeFace(imagePath);
      console.log('[Image response] :', image.faces);

      if (image.isValid) {
        inputChangeHandler('imageFace')(imagePath);
      } else {
        inputChangeHandler('imageFace')('');
        Alert.alert(
          'Face not detected',
          'Please reupload an image with your face in it.'
        );
      }
    } catch (error) {
      inputChangeHandler('imageFace')('');
      console.log('error', error);
    }
  };

  const onBarcodeScan = async (res: ImagePickerResponse) => {
    try {
      if (!res.assets || !res.assets[0]?.uri) {
        return;
      }

      const imagePath = res.assets[0].uri;
      const barcode = await ImageDetective.analyzeBarcode(imagePath);
      console.log('[barcode response] :', JSON.stringify(barcode));

      if (barcode.isValid) {
        inputChangeHandler('imageBarcode')(imagePath);
      } else {
        inputChangeHandler('imageBarcode')('');
        Alert.alert('Barcode not detected', 'Please reupload a barcode in it.');
      }
    } catch (error) {
      inputChangeHandler('imageBarcode')('');
      console.log('error', error);
    }
  };

  return (
    <SafeAreaView>
      <ScrollView contentContainerStyle={styles.container}>
        <Text style={styles.bold}>Face Detection</Text>
        <UploadImagesCard
          imageUri={form.imageFace}
          onImageCallback={(e) => onImageChanges(e)}
        />
        <Text style={styles.bold}>Barcode Scanner</Text>
        <UploadImagesCard
          imageUri={form.imageBarcode}
          onImageCallback={(e) => onBarcodeScan(e)}
          isContainImg
        />
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flexGrow: 1,
    padding: normalize(20),
  },
  bold: {
    fontSize: 20,
    fontWeight: 'bold',
    marginVertical: normalize(14, 'height'),
  },
});
