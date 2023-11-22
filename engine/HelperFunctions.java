package engine;

import engine.support.Vec2d;

public final class HelperFunctions {

  public static Vec2d[] parseVec2dArray(String input) {
    input = input.trim(); // Trim the input string
    input = input.substring(2, input.length() - 2); // Remove the opening and closing brackets

    // Split the string into individual (x, y) strings
    String[] pairs = input.split("\\), \\(");

    // Initialize the array with the number of pairs
    Vec2d[] vec2dArray = new Vec2d[pairs.length];

    // Convert each (x, y) string into a Vec2d object and store in the array
    for (int i = 0; i < pairs.length; i++) {
      // Add parentheses back to match the expected format for the constructor
      String pair = "(" + pairs[i] + ")";
      vec2dArray[i] = new Vec2d(pair); // Use the alternate constructor
    }

    return vec2dArray;
  }


  public static String arrayToString(Vec2d[] array) {
    StringBuilder sb = new StringBuilder();
    sb.append("[");

    // Iterate over the array and append Vec2d string representations
    for (int i = 0; i < array.length; i++) {
      sb.append(array[i].toString());
      if (i < array.length - 1) {
        sb.append(", ");
      }
    }

    sb.append("]");
    return sb.toString();
  }

}
