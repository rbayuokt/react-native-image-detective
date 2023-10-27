import * as React from 'react';

import {
  StyleSheet,
  Alert,
  Text,
  ScrollView,
  SafeAreaView,
  View,
} from 'react-native';
import ImageDetective from 'react-native-image-detective';
import UploadImagesCard from './components/UploadImagesCard./UploadImagesCard';
import { useForm } from './utils/useForm';
import type { ImagePickerResponse } from 'react-native-image-picker';
import normalize from './utils/normalize';
import type { ImageLabelerResult } from '../../src/types';

export type IUploadImagesForm = {
  imageFace: string;
  imageBarcode: string;
  imageLabeler: string;
  ImageLabelerResult: Array<ImageLabelerResult>;
};

export default function App() {
  const { form, inputChangeHandler } = useForm<IUploadImagesForm>({
    imageFace: '',
    imageBarcode: '',
    imageLabeler: '',
    ImageLabelerResult: [],
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
        Alert.alert('Face detected', 'Hoorayy !!');
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
        Alert.alert('barcode result', JSON.stringify(barcode));
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

  const onImageLabeler = async (res: ImagePickerResponse) => {
    try {
      if (!res.assets || !res.assets[0]?.uri) {
        return;
      }

      const imagePath = res.assets[0].uri;
      const imageLabeler = await ImageDetective.analyzeImage(imagePath);
      console.log('[image labeler response] :', JSON.stringify(imageLabeler));

      if (imageLabeler.isValid) {
        inputChangeHandler('imageLabeler')(imagePath);
        inputChangeHandler('ImageLabelerResult')(
          imageLabeler.imageLabelerResult
        );
      } else {
        inputChangeHandler('imageLabeler')('');
        Alert.alert('Barcode not detected', 'Please reupload a barcode in it.');
      }
    } catch (error) {
      inputChangeHandler('imageLabeler')('');
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
        <Text style={styles.bold}>Image Labeler</Text>
        <UploadImagesCard
          imageUri={form.imageLabeler}
          onImageCallback={(e) => onImageLabeler(e)}
          isContainImg={false}
          style={{ marginBottom: normalize(12, 'height') }}
        />
        {form.ImageLabelerResult.length > 0
          ? form.ImageLabelerResult.map((result, index) => {
              return (
                <View key={result.index.toString()} style={styles.wraperLabel}>
                  <Text style={styles.font}>Label {index}</Text>
                  <Text style={styles.font}>Index: {result.index}</Text>
                  <Text style={styles.font}>Text: {result.labelText}</Text>
                  <Text style={styles.font}>
                    Confidence: {(Number(result.confidence) * 100).toFixed(2)} %
                  </Text>
                </View>
              );
            })
          : null}
        <View />
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
    color: 'black',
    fontSize: 20,
    fontWeight: 'bold',
    marginVertical: normalize(14, 'height'),
  },
  wraperLabel: {
    marginVertical: normalize(6, 'height'),
  },
  font: {
    fontSize: normalize(16),
  },
});
