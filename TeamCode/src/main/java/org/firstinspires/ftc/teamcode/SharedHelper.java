package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

public abstract class SharedHelper {
    // DEVICE NAME
    public static final String DRIVE_LEFT_NAME = "drive_left";
    public static final String DRIVE_RIGHT_NAME = "drive_right";
    public static final String MOTOR_HAND_LEVEL_1_LEFT = "motor_level_1_left";
    public static final String MOTOR_HAND_LEVEL_1_RIGHT = "motor_level_1_right";
    public static final String MOTOR_HAND_LEVEL_2 = "motor_level_2";
    public static final String SERVO_HANGING_HOOK = "servo_hanging_hook";
    public static final String MOTOR_ESCALATOR = "motor_escalator";

    // LOCATING ALGORITHM CONST
    /**
     * The vuforia license key will be used in {@link VuforiaLocalizer.Parameters}
     */
    public static final String VUFORIA_LICENSE_KEY =
            "ASqiF8L/////AAABmYRisGBQ3ksWvR1WuvbRUmBLx2f/" +
                    "CTYKXqyyGJRRylFgj32VGN4kyEsv7tOqnPX5K5+w+fy+F" +
                    "/9r58CCfpWOf4VLp5zceTZXybHE2yoaXVapfFWr8bBP0V" +
                    "pVzXZAoaq0Di7hlP+uRwGXlNi7lmAWQAbSGY55VIFnQhi" +
                    "kcOeGVHpPhoOp0Lmq5djj7jipGOhDcC90pCukP+dEvceK" +
                    "Q/2Vc/hRkiNB9He+4SFPFirpgjw7uU3AFUIAVd+3nQySh" +
                    "Oo5hcSbtNd7tPgHRh6m/DiCt9RuysRyjx21Ir4ZzqhakK" +
                    "Z6hlWHla6S3CfSH2OArIcee9hN2liHkzqA3i3V88f7umb" +
                    "JIG3p95d3tDpdQvPs+znb";
    /**
     * Shows which camera should be used in {@link VuforiaLocalizer.Parameters}.
     */
    public static final VuforiaLocalizer.CameraDirection CAMERA_DIRECTION = VuforiaLocalizer.CameraDirection.BACK;
    /**
     * One inch is equal to 25.4 mm.
     */
    private static final float MM_PER_INCH = 25.4f;
    /**
     * The height of the targeting pictures.
     */
    public static final float MM_TARGET_HEIGHT = (6) * MM_PER_INCH;
    /**
     * The width of the FTC playing field this year.
     * (from the center point to the outer panels)
     */
    public static final float MM_FTC_FIELD_WIDTH = (12 * 6) * MM_PER_INCH;
    /**
     * The horizontal distance of the camera to the very front of the robot. Unit: mm
     */
    public static final int CAMERA_FORWARD_DISPLACEMENT = 140;
    /**
     * The vertical distance of the camera to the ground. Unit: mm
     */
    public static final int CAMERA_VERTICAL_DISPLACEMENT = 307;
    /**
     * The horizontal distance of the camera to the middle line of the robot. (left is positive, right is negative). Unit: mm
     */
    public static final int CAMERA_LEFT_DISPLACEMENT = -32;

    // ENCODER DRIVING CONST
    private static final double COUNTS_PER_MOTOR_REV = 1478.4;
    private static final double DRIVE_GEAR_REDUCTION = 2.0;     // This is < 1.0 if geared UP
    private static final double WHEEL_DIAMETER_MM = 4.0;     // For figuring circumference
    public static final double COUNTS_PER_CM = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            ((millimeterToInch(WHEEL_DIAMETER_MM) * Math.PI) / 2.54);
    public static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (millimeterToInch(WHEEL_DIAMETER_MM) * Math.PI);

    // OBJECT DETECTING CONST
    public static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    public static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    public static final String LABEL_SILVER_MINERAL = "Silver Mineral";
    public static final String MODEL_ASSET_NAME = "RoverRuckus";

    /**
     * Represents the minimum confidence used in {@link TFObjectDetector}.
     */
    public static final double MINIMUM_CONFIDENCE = 0.8;

    // FILEPATH
    public static final String AUTONOMOUS_FINISH_MUSIC_PATH = "/storage/emulated/0/music/autonomous_finish.mp3";
    public static final String TEST_MUSIC_PATH = "/storage/emulated/0/music/test.mp3";


    // UNIT CONVERTING
    /**
     * Converts an value in mm to inch.
     */
    public static double millimeterToInch(double mm) {
        return mm / MM_PER_INCH;
    }

    /**
     * Converts an value in inch to mm.
     */
    public static double inchToMillimeter(double inch) {
        return inch * MM_PER_INCH;
    }

    // NAVIGATION TARGET HELPER
    /**
     * Provides an easier way to convert target name in
     * format {@link String} to a new {@link NavigationTargetName}.
     */
    public static NavigationTargetName strToNavigationTargetName(String nameStr) {
        switch (nameStr) {
            case "Blue-Rover":
                return NavigationTargetName.BlueRover;
            case "Red-Footprint":
                return NavigationTargetName.RedFootprint;
            case "Front-Craters":
                return NavigationTargetName.FrontCraters;
            case "Back-Space":
                return NavigationTargetName.BackSpace;
            default:
                throw new IllegalArgumentException(nameStr + " is not an acceptable name");
        }
    }

    /**
     * Provides an easier way to convert target name in
     * {@link NavigationTargetName} to a new {@link String}.
     */
    public static String navigationTargetNameToStr(NavigationTargetName name) {
        switch (name) {
            case BlueRover:
                return "Blue-Rover";
            case RedFootprint:
                return "Red-Footprint";
            case FrontCraters:
                return "Front-Craters";
            case BackSpace:
                return "Back-Space";
            default:
                // Do nothing. It's impossible for programs to run to here
                throw new RuntimeException();
        }
    }

    // ENUM
    /**
     * Provides a set of directions.
     */
    public enum Direction {
        Forward,
        Backward,
        TurnLeft,
        TurnRight,
        Unknown
    }

    /**
     * Represents a group of numbers that means position.
     */
    public enum Position {
        Left,
        Center,
        Right,
        Unknown
    }

    /**
     * Represents a group of strings can be used in navigation target detecting.
     */
    public enum NavigationTargetName {
        BlueRover,
        RedFootprint,
        FrontCraters,
        BackSpace
    }

    // BOGUS OBJECT
    /**
     * Provides a set of bogus objects to use in the code.
     */
    public static abstract class BogusObject {
        public static final int BOGUS_INT = Integer.MIN_VALUE;
        public static final float BOGUS_FLOAT = Float.MIN_VALUE;
        public static final double BOGUS_DOUBLE = Double.MIN_VALUE;
        public static final boolean BOGUS_BOOLEAN = false;
        public static final Object BOGUS_OBJECT = new Object();
        public static final String BOGUS_STRING = "BOGUS";
    }
}
