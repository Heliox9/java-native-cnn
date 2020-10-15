package lab.cnn.custom.network;


import java.io.Serializable;


/**
 * Wrapping class for enums to be created from strings
 */
public class Enums implements Serializable {
    // method to lookup enum constants
    public static Enum get(String str) {
        try {
            OperatingMode mode = OperatingMode.valueOf(str.toUpperCase());
            if (mode != null) {
                return mode;
            }
        } catch (Exception e) {
            // Do nothing to trigger activation lookup
        }
        ActivationFunction func = ActivationFunction.valueOf(str.toUpperCase());
        return func;
    }


    public enum OperatingMode {
        PRODUCTIVE, TRAINING, TEST
    }

    public enum ActivationFunction {
        RELU, SIGMOID
    }
}