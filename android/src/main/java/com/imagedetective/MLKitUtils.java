package com.imagedetective;

import android.graphics.Point;
import android.graphics.Rect;

import androidx.annotation.Nullable;

import com.google.mlkit.common.MlKitException;

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

  static String[] getErrorCodeAndMessageFromException(@Nullable Exception possibleMLException) {
    String code = "unknown";
    String message = "An unknown error has occurred.";

    if (possibleMLException != null) {
      message = possibleMLException.getMessage();
      if (possibleMLException instanceof MlKitException) {
        MlKitException mlException = (MlKitException) possibleMLException;

        switch (mlException.getErrorCode()) {
          case MlKitException.ABORTED:
            code = "aborted";
            message = "The operation was aborted, typically due to a concurrency issue like transaction aborts, etc.";
            break;
          case MlKitException.ALREADY_EXISTS:
            code = "already-exists";
            message = "Some resource that we attempted to create already exists.";
            break;
          case MlKitException.CANCELLED:
            code = "cancelled";
            message = "The operation was cancelled (typically by the caller).";
            break;
          case MlKitException.DATA_LOSS:
            code = "data-loss";
            message = "Unrecoverable data loss or corruption.";
            break;
          case MlKitException.DEADLINE_EXCEEDED:
            code = "deadline-exceeded";
            message = "Deadline expired before operation could complete.";
            break;
          case MlKitException.FAILED_PRECONDITION:
            code = "failed-precondition";
            message = "Operation was rejected because the system is not in a state required for the operation's execution.";
            break;
          case MlKitException.INTERNAL:
            code = "internal";
            message = "Internal errors.";
            break;
          case MlKitException.INVALID_ARGUMENT:
            code = "invalid-argument";
            message = "Client specified an invalid argument.";
            break;
          case MlKitException.MODEL_HASH_MISMATCH:
            code = "model-hash-mismatch";
            message = "The downloaded model's hash doesn't match the expected value.";
            break;
          case MlKitException.MODEL_INCOMPATIBLE_WITH_TFLITE:
            code = "model-incompatible-with-tflite";
            message = "The downloaded model isn't compatible with the TFLite runtime.";
            break;
          case MlKitException.NETWORK_ISSUE:
            code = "network-issue";
            message = "There is a network issue when filing a network request.";
            break;
          case MlKitException.NOT_ENOUGH_SPACE:
            code = "not-enough-space";
            message = "There is not enough space left on the device.";
            break;
          case MlKitException.NOT_FOUND:
            code = "not-found";
            message = "Some requested resource was not found.";
            break;
          case MlKitException.OUT_OF_RANGE:
            code = "out-of-range";
            message = "Operation was attempted past the valid range.";
            break;
          case MlKitException.PERMISSION_DENIED:
            code = "permission-denied";
            message = "The caller does not have permission to execute the specified operation.";
            break;
          case MlKitException.RESOURCE_EXHAUSTED:
            code = "resource-exhausted";
            message = "Some resource has been exhausted, perhaps a per-user quota, or perhaps the entire file system is out of space.";
            break;
          case MlKitException.UNAUTHENTICATED:
            code = "unauthenticated";
            message = "The request does not have valid authentication credentials for the operation.";
            break;
          case MlKitException.UNAVAILABLE:
            code = "unavailable";
            message = "The service is currently unavailable.";
            break;
          case MlKitException.UNIMPLEMENTED:
            code = "unimplemented";
            message = "Operation is not implemented or not supported/enabled.";
            break;
        }
      }
    }

    return new String[]{code, message, possibleMLException != null ? possibleMLException.getMessage() : ""};
  }
}
