import { NativeModules, Platform } from 'react-native';
import {
  FaceDetectorClassificationMode,
  FaceDetectorContourMode,
  FaceDetectorLandmarkMode,
  FaceDetectorPerformanceMode,
  FaceContourType,
  FaceLandmarkType,
  //@ts-ignore
  FaceDetectorOptionsType,
  //@ts-ignore
  FaceResult,
} from './types';

const LINKING_ERROR =
  `The package 'react-native-image-detective' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const ImageDetective = NativeModules.ImageDetective
  ? NativeModules.ImageDetective
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

function createFaceDetectorOptions(
  options?: FaceDetectorOptionsType
): FaceDetectorOptionsType {
  const out: FaceDetectorOptionsType = {
    classificationMode: FaceDetectorClassificationMode.NONE,
    contourMode: FaceDetectorContourMode.NONE,
    landmarkMode: FaceDetectorLandmarkMode.NO,
    minFaceSize: 0.1,
    performanceMode: FaceDetectorPerformanceMode.FAST,
  };

  if (options === undefined || options === null) {
    return out;
  }

  if (options.classificationMode) {
    if (
      options.classificationMode !== FaceDetectorClassificationMode.NONE &&
      options.classificationMode !== FaceDetectorClassificationMode.ALL
    ) {
      throw new Error(
        "'options.classificationMode' invalid classification mode. Expected FaceDetectorClassificationMode.NONE or FaceDetectorClassificationMode.ALL."
      );
    }

    out.classificationMode = options.classificationMode;
  }

  if (options.contourMode) {
    if (
      options.contourMode !== FaceDetectorContourMode.NONE &&
      options.contourMode !== FaceDetectorContourMode.ALL
    ) {
      throw new Error(
        "'options.contourMode' invalid contour mode. Expected FaceDetectorContourMode.NONE or FaceDetectorContourMode.ALL."
      );
    }

    out.contourMode = options.contourMode;
  }

  if (options.landmarkMode) {
    if (
      options.landmarkMode !== FaceDetectorLandmarkMode.NO &&
      options.landmarkMode !== FaceDetectorLandmarkMode.ALL
    ) {
      throw new Error(
        "'options.landmarkMode' invalid landmark mode. Expected FaceDetectorLandmarkMode.NO or FaceDetectorLandmarkMode.ALL."
      );
    }

    out.landmarkMode = options.landmarkMode;
  }

  if (options.minFaceSize !== null && options.minFaceSize !== undefined) {
    if (isNaN(options.minFaceSize)) {
      throw new Error(
        "'options.minFaceSize' expected a number value between 0 & 1."
      );
    }

    if (options.minFaceSize < 0 || options.minFaceSize > 1) {
      throw new Error(
        "'options.minFaceSize' expected value to be between 0 & 1."
      );
    }

    out.minFaceSize = options.minFaceSize;
  }

  if (options.performanceMode) {
    if (
      options.performanceMode !== FaceDetectorPerformanceMode.FAST &&
      options.performanceMode !== FaceDetectorPerformanceMode.ACCURATE
    ) {
      throw new Error(
        "'options.performanceMode' invalid performance mode. Expected FaceDetectorPerformanceMode.FAST or FaceDetectorPerformanceMode.ACCURATE."
      );
    }

    out.performanceMode = options.performanceMode;
  }

  return out;
}

async function analyze(
  localImageFilePath: string,
  faceDetectorOption?: FaceDetectorOptionsType
): Promise<{ isValid: boolean; faces: Array<FaceResult> }> {
  if (!localImageFilePath) {
    throw new Error(
      "FaceDetection.processImage(*) 'localImageFilePath' expected a string local file path."
    );
  }

  const options = createFaceDetectorOptions(faceDetectorOption);

  const faces = await ImageDetective.processImage(localImageFilePath, options);

  return {
    faces,
    isValid: faces.length > 0 ? true : false,
  };
}

export default {
  analyze,
  createFaceDetectorOptions,
};

export {
  FaceDetectorClassificationMode,
  FaceDetectorContourMode,
  FaceDetectorLandmarkMode,
  FaceDetectorPerformanceMode,
  FaceContourType,
  FaceLandmarkType,
  //@ts-ignore
  FaceDetectorOptionsType,
  //@ts-ignore
  FaceResult,
};
