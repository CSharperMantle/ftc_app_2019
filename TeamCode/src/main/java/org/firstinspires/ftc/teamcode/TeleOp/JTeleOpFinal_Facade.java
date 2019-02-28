package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.JTeamCode_Shared;

import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.DRIVE_HAND_LEVEL_1_LEFT;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.DRIVE_HAND_LEVEL_1_RIGHT;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.DRIVE_LEFT_NAME;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.DRIVE_RIGHT_NAME;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.SERVO_HAND_LEVEL_2_LEFT_NAME;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.SERVO_HAND_LEVEL_2_RIGHT_NAME;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.SERVO_HAND_LEVEL_3_LEFT_NAME;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.SERVO_HAND_LEVEL_3_RIGHT_NAME;
import static org.firstinspires.ftc.teamcode.JTeamCode_Shared.Direction;


public final class JTeleOpFinal_Facade {
    public final DcMotor leftDrive;
    public final DcMotor rightDrive;
    public final DcMotor level1LeftDrive;
    public final DcMotor level1RightDrive;

    public final Servo level2LeftServo;
    public final Servo level2RightServo;
    public final Servo level3LeftServo;
    public final Servo level3RightServo;

    public JTeleOpFinal_Facade(HardwareMap hardwareMap) {
        this.leftDrive = hardwareMap.get(DcMotor.class, DRIVE_LEFT_NAME);
        this.rightDrive = hardwareMap.get(DcMotor.class, DRIVE_RIGHT_NAME);
        this.level1LeftDrive = hardwareMap.get(DcMotor.class, DRIVE_HAND_LEVEL_1_LEFT);
        this.level1RightDrive = hardwareMap.get(DcMotor.class, DRIVE_HAND_LEVEL_1_RIGHT);

        this.level2LeftServo = hardwareMap.get(Servo.class, SERVO_HAND_LEVEL_2_LEFT_NAME);
        this.level2RightServo = hardwareMap.get(Servo.class, SERVO_HAND_LEVEL_2_RIGHT_NAME);
        this.level3LeftServo = hardwareMap.get(Servo.class, SERVO_HAND_LEVEL_3_LEFT_NAME);
        this.level3RightServo = hardwareMap.get(Servo.class, SERVO_HAND_LEVEL_3_RIGHT_NAME);
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

    public void closeDevice() {
        this.leftDrive.close();
        this.rightDrive.close();

        this.level1LeftDrive.close();
        this.level1RightDrive.close();
        this.level2LeftServo.close();
        this.level2RightServo.close();
        this.level3LeftServo.close();
        this.level3RightServo.close();
    }
}
