package com.imagedetective;

import static com.imagedetective.FaceDetectionCommon.KEY_POINTS;
import static com.imagedetective.FaceDetectionCommon.KEY_TYPE;

import android.graphics.PointF;

import com.google.mlkit.vision.face.FaceContour;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.imagedetective.MLKitUtils.*;

public class BarcodeScannerCommon {
  static final String KEY_BARCODE_BOUNDING_BOX = "boundingBox";
  static final String KEY_CORNERS = "cornerPoints";
  static final String KEY_DISPLAY_VALUE = "displayValue";
  static final String KEY_RAW_VALUE = "rawValue";
}
