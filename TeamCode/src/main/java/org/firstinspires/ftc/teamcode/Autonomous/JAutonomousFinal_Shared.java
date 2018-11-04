package org.firstinspires.ftc.teamcode.Autonomous;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.Parameters;

public abstract class JAutonomousFinal_Shared {
    /**
     * The vuforia license key will be used in {@link Parameters}*/
    public static final String vuforiaLicenseKey =
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
    public static final VuforiaLocalizer.CameraDirection cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

    /**
     * One inch is equal to 25.4 mm.
     * */
    public static final float mmPerInch = 25.4f;

    /**
     * The width of the FTC playing field this year.
     * (from the center point to the outer panels)
     * */
    public static final float mmFTCFieldWidth = (12*6) * mmPerInch;

    /**
     * The height of the targeting pictures.
     * */
    public static final float mmTargetHeight = (6) * mmPerInch;

    /**
     * The horizontal distance of the camera to the very front of the robot.
     * */
    public static final int cameraForwardDisplacement = 110;

    /**
     * The vertical distance of the camera to the ground.
     * */
    public static final int cameraVerticalDisplacement = 200;

    /**
     * The horizontal distance of the camera to the middle line of the robot. (left is positive, right is negative)
     * */
    public static final int cameraLeftDisplacement = 0;

    /**
     * Converts an value in mm to inch.
     */
    public static float millimeterToInch(float mm) {
        return mm / mmPerInch;
    }

    /**
     * Converts an value in inch to mm.
     */
    public static float inchToMillimeter(float inch) {
        return inch * mmPerInch;
    }

    public static void writeMessageRefresh(String caption, String text, Telemetry t) {
        t.addData(caption, text);
        t.update();
    }

    /**
     * Represents a group of numbers that means position.
     * */
    enum Position {
        LEFT,
        CENTER,
        RIGHT
    }
}
