package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.JTeamCode_Shared;

import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.MOTOR_ESCALATOR;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.MOTOR_HAND_LEVEL_1_LEFT;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.MOTOR_HAND_LEVEL_1_RIGHT;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.MOTOR_HAND_LEVEL_2;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.DRIVE_LEFT_NAME;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.DRIVE_RIGHT_NAME;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.Direction;

/**
 * This class contains several methods to access hardware devices quickly.
 * Users can also directly access the original hardware objects.
 */
public final class JTeleOpFinal_Facade {
    public final DcMotor leftDrive;
    public final DcMotor rightDrive;
    public final DcMotor level1LeftDrive;
    public final DcMotor level1RightDrive;
    public final DcMotor level2Drive;
    public final DcMotor escalatorDrive;

    public JTeleOpFinal_Facade(HardwareMap hardwareMap) {
        this.leftDrive = hardwareMap.get(DcMotor.class, DRIVE_LEFT_NAME);
        this.rightDrive = hardwareMap.get(DcMotor.class, DRIVE_RIGHT_NAME);
        this.level1LeftDrive = hardwareMap.get(DcMotor.class, MOTOR_HAND_LEVEL_1_LEFT);
        this.level1RightDrive = hardwareMap.get(DcMotor.class, MOTOR_HAND_LEVEL_1_RIGHT);
        this.level2Drive = hardwareMap.get(DcMotor.class, MOTOR_HAND_LEVEL_2);
        this.escalatorDrive = hardwareMap.get(DcMotor.class, MOTOR_ESCALATOR);
        escalatorDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    @Deprecated
    public void driveTogether(JTeamCode_Shared.Direction direction,
                                     double power) {
        driveSeparated(direction, power, power);
    }

    public void driveSeparated(JTeamCode_Shared.Direction direction,
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

        this.leftDrive.setDirection(leftDriveDirection == Direction.Forward ?
                DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);
        this.rightDrive.setDirection(rightDriveDirection == Direction.Forward ?
                DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);

        this.leftDrive.setPower(leftPower);
        this.rightDrive.setPower(rightPower);
    }

    public void stopAllDrives() {
        this.leftDrive.setPower(0);
        this.rightDrive.setPower(0);
    }

    public void moveRoboticArmLevel1(Direction direction, double power) {
        Direction leftArmDirection;
        Direction rightArmDirection;

        switch (direction) {
            case Forward:
                leftArmDirection = Direction.Forward;
                rightArmDirection = Direction.Backward;
                break;
            case Backward:
                leftArmDirection = Direction.Backward;
                rightArmDirection = Direction.Forward;
                break;
            default:
                // Don't have this operation
                throw new IllegalArgumentException("direction not acceptable");
        }

        this.level1LeftDrive.setDirection(leftArmDirection == Direction.Forward ?
                DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);
        this.level1RightDrive.setDirection(rightArmDirection == Direction.Forward ?
                DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);

        this.level1LeftDrive.setPower(power);
        this.level1RightDrive.setPower(power);
    }

    public void closeDevice() {
        this.leftDrive.close();
        this.rightDrive.close();

        this.level1LeftDrive.close();
        this.level1RightDrive.close();
        this.level2Drive.close();
    }
}
