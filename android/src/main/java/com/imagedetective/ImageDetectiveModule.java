package com.imagedetective;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceContour;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.google.mlkit.vision.face.FaceLandmark;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;

import static com.imagedetective.FaceDetectionCommon.*;
import static com.imagedetective.FaceDetectionUtils.*;
import static com.imagedetective.BarcodeScannerCommon.*;
import static com.imagedetective.MLKitUtils.*;

import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;

@ReactModule(name = ImageDetectiveModule.NAME)
public class ImageDetectiveModule extends ReactContextBaseJavaModule {
  public static final String NAME = "ImageDetective";

  public ImageDetectiveModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }

  /**
   * FACE DETECTOR
   */
  @ReactMethod
  public void processImage(String filePath, ReadableMap faceDetectorOptions, Promise promise) {
    FaceDetectorOptions options = getFaceDetectorOptions(Arguments.toBundle(faceDetectorOptions));

    InputImage image = null;
    try {
      image = InputImage.fromFilePath(
        this.getReactApplicationContext(),
        getUri(filePath)
      );
    } catch (IOException e) {
      e.printStackTrace();

      String code = "io-exception";
      String message = e.getMessage();

      WritableMap userInfoMap = Arguments.createMap();
      userInfoMap.putString("code", code);
      userInfoMap.putString("message", message);

      promise.reject(code, message, userInfoMap);
      return;
    }

    FaceDetector detector = FaceDetection.getClient(options);

    Task<List<Face>> result =detector.process(image);
    result.addOnSuccessListener(
      new OnSuccessListener<List<Face>>() {
        @Override
        public void onSuccess(@NonNull List<Face> faces) {
          List<Map<String, Object>> facesFormatted = new ArrayList<>(faces.size());

          for (Face face : faces) {
            Map<String, Object> faceFormatted = new HashMap<>();

            faceFormatted.put(KEY_BOUNDING_BOX,
              MLKitUtils.rectToIntArray(face.getBoundingBox())
            );

            faceFormatted.put(KEY_HEAD_EULER_ANGLE_X, face.getHeadEulerAngleX());
            faceFormatted.put(KEY_HEAD_EULER_ANGLE_Y, face.getHeadEulerAngleY());
            faceFormatted.put(KEY_HEAD_EULER_ANGLE_Z, face.getHeadEulerAngleZ());
            faceFormatted.put(KEY_LEFT_EYE_OPEN_PROBABILITY, face.getLeftEyeOpenProbability());
            faceFormatted.put(KEY_RIGHT_EYE_OPEN_PROBABILITY, face.getRightEyeOpenProbability());

            faceFormatted.put(KEY_SMILING_PROBABILITY, face.getSmilingProbability());
            faceFormatted.put(KEY_TRACKING_ID, face.getTrackingId());

            List<Map<String, Object>> faceContoursFormatted;

            int classificationMode = (int) faceDetectorOptions.getDouble(KEY_CLASSIFICATION_MODE);

            if (classificationMode == FaceDetectorOptions.CONTOUR_MODE_NONE) {
              faceContoursFormatted = new ArrayList<>(0);
            } else {
              faceContoursFormatted = new ArrayList<>(14);
              faceContoursFormatted.add(getContourMap(face.getContour(FaceContour.FACE)));
              faceContoursFormatted.add(getContourMap(face.getContour(FaceContour.LEFT_EYEBROW_TOP)));
              faceContoursFormatted.add(getContourMap(face.getContour(FaceContour.LEFT_EYEBROW_BOTTOM)));
              faceContoursFormatted.add(getContourMap(face.getContour(FaceContour.RIGHT_EYEBROW_TOP)));
              faceContoursFormatted.add(getContourMap(face.getContour(FaceContour.RIGHT_EYEBROW_BOTTOM)));
              faceContoursFormatted.add(getContourMap(face.getContour(FaceContour.LEFT_EYE)));
              faceContoursFormatted.add(getContourMap(face.getContour(FaceContour.RIGHT_EYE)));
              faceContoursFormatted.add(getContourMap(face.getContour(FaceContour.UPPER_LIP_TOP)));
              faceContoursFormatted.add(getContourMap(face.getContour(FaceContour.UPPER_LIP_BOTTOM)));
              faceContoursFormatted.add(getContourMap(face.getContour(FaceContour.LOWER_LIP_TOP)));
              faceContoursFormatted.add(getContourMap(face.getContour(FaceContour.LOWER_LIP_BOTTOM)));
              faceContoursFormatted.add(getContourMap(face.getContour(FaceContour.NOSE_BRIDGE)));
              faceContoursFormatted.add(getContourMap(face.getContour(FaceContour.NOSE_BOTTOM)));
            }

            faceFormatted.put(KEY_FACE_CONTOURS, faceContoursFormatted);

            List<Map<String, Object>> faceLandmarksFormatted;

            int landmarkMode = (int) faceDetectorOptions.getDouble(KEY_LANDMARK_MODE);

            if (landmarkMode == FaceDetectorOptions.LANDMARK_MODE_NONE) {
              faceLandmarksFormatted = new ArrayList<>(0);
            } else {
              faceLandmarksFormatted = new ArrayList<>(14);

              if (face.getLandmark(FaceLandmark.MOUTH_BOTTOM) != null) {
                faceLandmarksFormatted.add(getLandmarkMap(
                  face.getLandmark(FaceLandmark.MOUTH_BOTTOM)
                ));
              }

              if (face.getLandmark(FaceLandmark.MOUTH_RIGHT) != null) {
                faceLandmarksFormatted.add(getLandmarkMap(
                  face.getLandmark(FaceLandmark.MOUTH_RIGHT)
                ));
              }

              if (face.getLandmark(FaceLandmark.MOUTH_LEFT) != null) {
                faceLandmarksFormatted.add(getLandmarkMap(
                  face.getLandmark(FaceLandmark.MOUTH_LEFT)
                ));
              }

              if (face.getLandmark(FaceLandmark.RIGHT_EYE) != null) {
                faceLandmarksFormatted.add(getLandmarkMap(
                  face.getLandmark(FaceLandmark.RIGHT_EYE)
                ));
              }

              if (face.getLandmark(FaceLandmark.LEFT_EYE) != null) {
                faceLandmarksFormatted.add(getLandmarkMap(
                  face.getLandmark(FaceLandmark.LEFT_EYE)
                ));
              }

              if (face.getLandmark(FaceLandmark.RIGHT_EAR) != null) {
                faceLandmarksFormatted.add(getLandmarkMap(
                  face.getLandmark(FaceLandmark.RIGHT_EAR)
                ));
              }

              if (face.getLandmark(FaceLandmark.LEFT_EAR) != null) {
                faceLandmarksFormatted.add(getLandmarkMap(
                  face.getLandmark(FaceLandmark.LEFT_EAR)
                ));
              }

              if (face.getLandmark(FaceLandmark.RIGHT_CHEEK) != null) {
                faceLandmarksFormatted.add(getLandmarkMap(
                  face.getLandmark(FaceLandmark.RIGHT_CHEEK)
                ));
              }

              if (face.getLandmark(FaceLandmark.LEFT_CHEEK) != null) {
                faceLandmarksFormatted.add(getLandmarkMap(
                  face.getLandmark(FaceLandmark.LEFT_CHEEK)
                ));
              }

              if (face.getLandmark(FaceLandmark.NOSE_BASE) != null) {
                faceLandmarksFormatted.add(getLandmarkMap(
                  face.getLandmark(FaceLandmark.NOSE_BASE)
                ));
              }
            }

            faceFormatted.put(KEY_LANDMARKS, faceLandmarksFormatted);
            facesFormatted.add(faceFormatted);
          }

          promise.resolve(
            Arguments.makeNativeArray(facesFormatted)
          );
        }
      }
    );
    result.addOnFailureListener(
      new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
          String[] errorCodeAndMessage = FaceDetectionCommon.getErrorCodeAndMessageFromException(e);

          String code = errorCodeAndMessage[0];
          String message = errorCodeAndMessage[1];
          String nativeErrorMessage = errorCodeAndMessage[2];

          WritableMap userInfoMap = Arguments.createMap();
          userInfoMap.putString("code", code);
          userInfoMap.putString("message", message);
          userInfoMap.putString("nativeErrorMessage", nativeErrorMessage);

          promise.reject(code, message, userInfoMap);
        }
      }
    );
  }
  /**
   * END OF FACE DETECTOR
   */

  /**
   * BARCODE SCANNER
   */
  @ReactMethod
  public void processBarcode(String filePath, Promise promise){
    InputImage image = null;
    try {
      image = InputImage.fromFilePath(
        this.getReactApplicationContext(),
        getUri(filePath)
      );
    } catch (IOException e) {
      e.printStackTrace();

      String code = "io-exception";
      String message = e.getMessage();

      WritableMap userInfoMap = Arguments.createMap();
      userInfoMap.putString("code", code);
      userInfoMap.putString("message", message);

      promise.reject(code, message, userInfoMap);
      return;
    }

    BarcodeScannerOptions options =
      new BarcodeScannerOptions.Builder()
        .setBarcodeFormats(
          Barcode.FORMAT_CODE_128,
          Barcode.FORMAT_CODE_39,
          Barcode.FORMAT_CODE_93,
          Barcode.FORMAT_CODABAR,
          Barcode.FORMAT_EAN_13,
          Barcode.FORMAT_EAN_8,
          Barcode.FORMAT_ITF,
          Barcode.FORMAT_UPC_A,
          Barcode.FORMAT_UPC_E,
          Barcode.FORMAT_QR_CODE,
          Barcode.FORMAT_PDF417,
          Barcode.FORMAT_AZTEC,
          Barcode.FORMAT_DATA_MATRIX
        )
        .build();

    BarcodeScanner scanner = BarcodeScanning.getClient();

    Task<List<Barcode>> result = scanner.process(image)
      .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
        @Override
        public void onSuccess(List<Barcode> barcodes) {
          List<Map<String, Object>> barcodesFormatted = new ArrayList<>(barcodes.size());
          for (Barcode barcode: barcodes) {
            Map<String, Object> barcodeFormatted = new HashMap<>();
            Point[] corners = barcode.getCornerPoints();
            List<float[]> cornersFormatted = new ArrayList<>(corners.length);
            String rawValue = barcode.getRawValue();
            String displayValue = barcode.getDisplayValue();

            for (Point pointRaw : corners) {
              cornersFormatted.add(MLKitUtils.getPointMap(pointRaw));
            }

            barcodeFormatted.put(KEY_BARCODE_BOUNDING_BOX,
              MLKitUtils.rectToIntArray(barcode.getBoundingBox())
            );

            barcodeFormatted.put(KEY_CORNERS, cornersFormatted);
            barcodeFormatted.put(KEY_DISPLAY_VALUE, displayValue);
            barcodeFormatted.put(KEY_RAW_VALUE, rawValue);

            barcodesFormatted.add(barcodeFormatted);
          }
          promise.resolve(
            Arguments.makeNativeArray(barcodesFormatted)
          );
        }
      })
      .addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
          String[] errorCodeAndMessage = FaceDetectionCommon.getErrorCodeAndMessageFromException(e);

          String code = errorCodeAndMessage[0];
          String message = errorCodeAndMessage[1];
          String nativeErrorMessage = errorCodeAndMessage[2];

          WritableMap userInfoMap = Arguments.createMap();
          userInfoMap.putString("code", code);
          userInfoMap.putString("message", message);
          userInfoMap.putString("nativeErrorMessage", nativeErrorMessage);

          promise.reject(code, message, userInfoMap);
        }
      });
  }
  /**
   * END OF SCANNER
   */
}
