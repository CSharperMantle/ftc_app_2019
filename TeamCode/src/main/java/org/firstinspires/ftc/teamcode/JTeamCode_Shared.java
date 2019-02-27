package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

public abstract class JTeamCode_Shared {
    public static final String LEFT_DRIVE_NAME = "left_drive";
    public static final String RIGHT_DRIVE_NAME = "right_drive";
    public static final String LEVEL_1_LEFT_SERVO_NAME = "level_1_left_servo";
    public static final String LEVEL_1_RIGHT_SERVO_NAME = "level_1_right_servo";
    public static final String LEVEL_2_LEFT_SERVO_NAME = "level_2_left_servo";
    public static final String LEVEL_2_RIGHT_SERVO_NAME = "level_2_right_servo";
    public static final String LEVEL_3_LEFT_SERVO_NAME = "level_3_left_servo";
    public static final String LEVEL_3_RIGHT_SERVO_NAME = "level_3_right_servo";
    /**
     * The vuforia license key will be used in {@link VuforiaLocalizer.Parameters}*/
    public static final String VUFORIA_LICENSE_KEY =
            "AVkhglD/////AAADmXDi5jqMNElPqdugVXJjZUwG" +
                    "CACWSA+2X9FnKUGIZ19FSiR1Vhvn5amX7bepl39L" +
                    "vukVM35/gwtVlW1sJUoakHA+Sa//eU8yZbrQ+hHC" +
                    "OMqK16bh2jFaEh2Z5oX3blfOcnY+KBhT1pZCvpn7" +
                    "uc7tozv/hEh6k0txpLHH71S2oo+jYbld1PL9lxpB" +
                    "L2l8CIqQLa4MVzVgHvlnH+47M+VgaE2nhKR//xSm" +
                    "c9Y7JP4WxP+G427hHpr/Uj0yectnEAb6zRf0ay5U" +
                    "eOPjWQfIqP8cBN5hJ8FZeI49HDazlbNoQQZpVer4" +
                    "T2pZWHKju2M6CqMTbrVNHgU+QOE5zKH/iQsljQME" +
                    "+VCvEhLrhgqs5aqF5Zm3";
    /**
     * Shows which camera should be used in {@link VuforiaLocalizer.Parameters}.
     * */
    public static final VuforiaLocalizer.CameraDirection CAMERA_DIRECTION = VuforiaLocalizer.CameraDirection.BACK;
    /**
     * One inch is equal to 25.4 mm.
     * */
    public static final float MM_PER_INCH = 25.4f;
    /**
     * The height of the targeting pictures.
     * */
    public static final float MM_TARGET_HEIGHT = (6) * MM_PER_INCH;
    /**
     * The width of the FTC playing field this year.
     * (from the center point to the outer panels)
     * */
    public static final float MM_FTC_FIELD_WIDTH = (12*6) * MM_PER_INCH;
    /**
     * The horizontal distance of the camera to the very front of the robot.
     * */
    public static final int CAMERA_FORWARD_DISPLACEMENT = 110;
    /**
     * The vertical distance of the camera to the ground.
     * */
    public static final int CAMERA_VERTICAL_DISPLACEMENT = 200;
    /**
     * The horizontal distance of the camera to the middle line of the robot. (left is positive, right is negative)
     * */
    public static final int CAMERA_LEFT_DISPLACEMENT = 0;
    public static final double COUNTS_PER_MOTOR_REV = 1478.4;
    public static final double DRIVE_GEAR_REDUCTION = 2.0;     // This is < 1.0 if geared UP
    public static final double WHEEL_DIAMETER_MM = 4.0;     // For figuring circumference
    public static final double COUNTS_PER_CM = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            ((millimeterToInch(WHEEL_DIAMETER_MM) * Math.PI) / 2.54);
    public static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (millimeterToInch(WHEEL_DIAMETER_MM) * Math.PI);
    public static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    public static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    public static final String LABEL_SILVER_MINERAL = "Silver Mineral";
    public static final String MODEL_ASSET_NAME = "RoverRuckus";
    public static final String VICTORY_MUSIC_PATH = "../";
    /**
     * Represents the minimum confidence used in {@link TFObjectDetector}.
     * */
    public static final double MINIMUM_CONFIDENCE = 0.8;

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

    /**
     * Provides an easier way to write a message on an instance of {@link Telemetry}, and
     * than refresh it.*/
    public static void writeMessageRefresh(String caption, String text, Telemetry t) {
        t.addData(caption, text);
        t.update();
    }

    /**
     * Provides an easier way to convert target name in
     * format {@link String} to a new {@link NavigationTargetName}.
     * */
    public static NavigationTargetName parseNavigationTargetName(String nameStr) {
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
                throw new IllegalArgumentException(nameStr);
        }
    }

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
     * */
    public enum Position {
        Left,
        Center,
        Right,
        Unknown
    }

    /**
     * Represents a group of strings can be used in navigation target detecting.
     * */
    public enum NavigationTargetName {
        BlueRover,
        RedFootprint,
        FrontCraters,
        BackSpace
    }
}
