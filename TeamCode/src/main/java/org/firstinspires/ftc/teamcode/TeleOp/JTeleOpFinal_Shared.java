package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotorSimple;

public abstract class JTeleOpFinal_Shared {
    public static final String LEFT_DRIVE_NAME = "left_drive";
    public static final String RIGHT_DRIVE_NAME = "right_drive";

    public static final String LEVEL_1_LEFT_SERVO_NAME = "level_1_left_servo";
    public static final String LEVEL_1_RIGHT_SERVO_NAME = "level_1_right_servo";
    public static final String LEVEL_2_LEFT_SERVO_NAME = "level_2_left_servo";
    public static final String LEVEL_2_RIGHT_SERVO_NAME = "level_2_right_servo";
    public static final String LEVEL_3_LEFT_SERVO_NAME = "level_3_left_servo";
    public static final String LEVEL_3_RIGHT_SERVO_NAME = "level_3_right_servo";

    public enum Direction {
        Forward,
        Backward,
        TurnLeft,
        TurnRight,
        Unknown
    }

    @Deprecated
    public static void driveTogether(JTeleOpFinal_Facade facade, Direction direction,
                                     double power) {
        driveSeparated(facade, direction, power, power);
    }

    public static void driveSeparated(JTeleOpFinal_Facade facade, Direction direction,
                                      double leftPower, double rightPower) {
        Direction leftDriveDirection;
        Direction rightDriveDirection;

        switch (direction) {
            case Forward:
                leftDriveDirection = Direction.Forward;
                rightDriveDirection = Direction.Backward;
                break;
            case Backward:
                leftDriveDirection = Direction.Backward;
                rightDriveDirection = Direction.Forward;
                break;
            case TurnLeft:
                leftDriveDirection = Direction.Forward;
                rightDriveDirection = Direction.Forward;
                break;
            case TurnRight:
                leftDriveDirection = Direction.Backward;
                rightDriveDirection = Direction.Backward;
                break;
            default:
                throw new IllegalArgumentException("direction not acceptable");
        }

        facade.leftDrive.setDirection(leftDriveDirection == Direction.Forward ?
                DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);
        facade.rightDrive.setDirection(rightDriveDirection == Direction.Forward ?
                DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);

        facade.leftDrive.setPower(leftPower);
        facade.rightDrive.setPower(rightPower);
    }
}
