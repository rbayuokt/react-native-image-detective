import * as React from 'react';

import { StyleSheet, View, Alert } from 'react-native';
import ImageDetective from 'react-native-image-detective';
import UploadImagesCard from './components/UploadImagesCard./UploadImagesCard';
import { useForm } from './utils/useForm';
import type { ImagePickerResponse } from 'react-native-image-picker';
import normalize from './utils/normalize';

export type IUploadImagesForm = {
  image: string;
};

export default function App() {
  const { form, inputChangeHandler } = useForm<IUploadImagesForm>({
    image: '',
  });

  const onImageChanges = async (res: ImagePickerResponse) => {
    try {
      if (!res.assets || !res.assets[0]?.uri) {
        return;
      }

      const imagePath = res.assets[0].uri;
      const image = await ImageDetective.analyze(imagePath);
      console.log('[Image response] :', image.faces);

      if (image.isValid) {
        inputChangeHandler('image')(imagePath);
      } else {
        inputChangeHandler('image')('');
        Alert.alert(
          'Face not detected',
          'Please reupload an image with your face in it.'
        );
      }
    } catch (error) {
      inputChangeHandler('image')('');
      console.log('error', error);
    }
  };

  return (
    <View style={styles.container}>
      <UploadImagesCard
        imageUri={form.image}
        onImageCallback={(e) => onImageChanges(e)}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    marginTop: normalize(30, 'height'),
    padding: normalize(20),
  },
});
