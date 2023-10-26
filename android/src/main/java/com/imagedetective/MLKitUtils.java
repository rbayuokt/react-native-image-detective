package com.imagedetective;

import android.graphics.Point;
import android.graphics.Rect;

import androidx.annotation.Nullable;

public class MLKitUtils {
  public static int[] rectToIntArray(@Nullable Rect rect) {
    if (rect == null || rect.isEmpty()) {
      return new int[]{};
    }

    return new int[]{
      rect.left,
      rect.top,
      rect.right,
      rect.bottom
    };
  }

  public static float[] getPointMap(Point point) {
    return new float[]{point.x, point.y};
  }
}
