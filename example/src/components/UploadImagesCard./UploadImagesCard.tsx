import React, { memo, useState } from 'react';
import {
  Image,
  type StyleProp,
  StyleSheet,
  TouchableOpacity,
  View,
  Text,
  type ViewStyle,
} from 'react-native';
import * as ImagePicker from 'react-native-image-picker';
import Modal from 'react-native-modal';

import normalize from '../../utils/normalize';

type Props = {
  imageUri?: string;
  onImageCallback: (e: ImagePicker.ImagePickerResponse) => void;
  style?: StyleProp<ViewStyle>;
  isContainImg?: boolean;
};

interface Action {
  title: string;
  type: 'capture' | 'library';
  options: ImagePicker.CameraOptions | ImagePicker.ImageLibraryOptions;
}

const includeExtra = true;

const UploadImagesCard = ({
  imageUri,
  onImageCallback,
  style,
  isContainImg = false,
}: Props) => {
  const [showModal, setShowModal] = useState<boolean>(false);

  const onButtonPress = React.useCallback(
    (
      type: 'capture' | 'library',
      options: ImagePicker.CameraOptions | ImagePicker.ImageLibraryOptions
    ) => {
      if (type === 'capture') {
        ImagePicker.launchCamera(options, (res) => {
          onImageCallback(res);
          setShowModal(false);
        });
      } else {
        ImagePicker.launchImageLibrary(options, (res) => {
          onImageCallback(res);
          setShowModal(false);
        });
      }
    },
    [onImageCallback]
  );

  return (
    <View style={[styles.outer, style]}>
      <TouchableOpacity
        style={[styles.outer, style]}
        onPress={() => {
          setShowModal(true);
        }}
      >
        <View style={{ position: 'relative' }}>
          {imageUri ? (
            <Image
              source={{ uri: imageUri }}
              style={[
                styles.img,
                { resizeMode: isContainImg ? 'contain' : 'cover' },
              ]}
            />
          ) : (
            <View style={styles.box}>
              <Text>Upload Photo</Text>
            </View>
          )}
          <Modal
            isVisible={showModal}
            onBackdropPress={() => setShowModal(false)}
          >
            <View style={styles.modal}>
              {actions.map((action, index) => {
                return (
                  <TouchableOpacity
                    key={`${action.title}${index}`}
                    onPress={() => {
                      onButtonPress(action.type, action.options);
                    }}
                    style={[styles.btn]}
                  >
                    <Text>{action.title}</Text>
                  </TouchableOpacity>
                );
              })}
            </View>
          </Modal>
        </View>
      </TouchableOpacity>
    </View>
  );
};

const actions: Action[] = [
  {
    title: 'Take Image',
    type: 'capture',
    options: {
      saveToPhotos: true,
      mediaType: 'photo',
      includeBase64: false,
      includeExtra,
    },
  },
  {
    title: 'Select Image',
    type: 'library',
    options: {
      selectionLimit: 1,
      mediaType: 'photo',
      includeBase64: false,
      includeExtra,
    },
  },
];

const styles = StyleSheet.create({
  outer: {
    height: normalize(400, 'height'),
    backgroundColor: '#efefef',
    borderRadius: 10,
  },
  container: {
    backgroundColor: '#ffffff',
  },
  box: {
    position: 'relative',
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    width: '100%',
    height: '100%',
  },
  icn: {
    height: normalize(32, 'height'),
    resizeMode: 'contain',
  },
  modal: {
    padding: normalize(16),
    backgroundColor: '#ffffff',
    borderRadius: 8,
  },
  btn: {
    paddingVertical: normalize(16),
  },
  iconCamera: {
    justifyContent: 'center',
    alignItems: 'center',
  },
  img: {
    width: '100%',
    height: '100%',
    borderRadius: 10,
  },
});

export default memo(UploadImagesCard);
